package net.de1mos.example.oauth2demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.de1mos.example.oauth2demo.entities.ExampleUser;
import net.de1mos.example.oauth2demo.repository.UserSessionDao;
import net.de1mos.example.oauth2demo.repository.UsersDao;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

public class JwtAuthenticationClientFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager manager;
    private JwtTokenReader jwtTokenReader;
    private UsersDao usersDao;
    private ObjectMapper mapper;
    private UserSessionDao userSessionDao;


    public JwtAuthenticationClientFilter(AuthenticationManager manager,
                                         JwtTokenReader jwtTokenReader,
                                         UsersDao usersDao,
                                         ObjectMapper mapper,
                                         UserSessionDao userSessionDao) {
        this.manager = manager;
        this.jwtTokenReader = jwtTokenReader;
        this.usersDao = usersDao;
        this.mapper = mapper;
        this.userSessionDao = userSessionDao;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            JwtToken jwtToken = jwtTokenReader.getAuthentication(request);
            if (jwtToken == null) {
                throw new BadCredentialsException("Unauthenticated token not provided");
            }
            if (userSessionDao.findBySessionId(jwtToken.getSessionId()) == null) {
                throw new BadCredentialsException("Session not found");
            }
            ClientCredentials creds = new ObjectMapper()
                    .readValue(request.getInputStream(), ClientCredentials.class);

            return manager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            String.valueOf(creds.getUsername().hashCode()),
                            creds.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT")))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String username = ((UserDetails) authResult.getPrincipal()).getUsername();

        ExampleUser user = usersDao.findByUsernameHash(username);
        JwtToken jwtAnonAccessToken = jwtTokenReader.getAuthentication(request);
        JwtToken jwtToken =
                new JwtToken(jwtAnonAccessToken.getSessionId(),
                        user.getId(),
                        user.getUsernameHash(),
                        JwtToken.JwtTokenType.CLIENT);
        String token = Jwts.builder()
                .setSubject(mapper.writeValueAsString(jwtToken))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConfig.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConfig.SECRET.getBytes())
                .compact();
        response.addHeader(SecurityConfig.HEADER_STRING, SecurityConfig.TOKEN_PREFIX + token);
        response.sendRedirect("/");
    }
}