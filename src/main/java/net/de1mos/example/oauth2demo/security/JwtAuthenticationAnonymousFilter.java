package net.de1mos.example.oauth2demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.de1mos.example.oauth2demo.entities.UserSession;
import net.de1mos.example.oauth2demo.repository.UserSessionDao;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
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
import java.util.UUID;

public class JwtAuthenticationAnonymousFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager manager;
    private UserSessionDao userSessionDao;
    private ObjectMapper mapper;

    public JwtAuthenticationAnonymousFilter(AuthenticationManager manager,
                                            UserSessionDao userSessionDao,
                                            ObjectMapper mapper) {
        this.manager = manager;
        this.userSessionDao = userSessionDao;
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        UserSession userSession = new UserSession();
        userSession.setSessionId(UUID.randomUUID().toString());
        UserSession savedUserSession = userSessionDao.save(userSession);
        return manager.authenticate(new AnonymousAuthenticationToken(savedUserSession.getSessionId(),
                new UnauthenticatedUserDetails(savedUserSession),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String sessionId = ((UserDetails) authResult.getPrincipal()).getUsername();
        JwtToken jwtToken =
                new JwtToken(sessionId, null, sessionId, JwtToken.JwtTokenType.ANONYMOUS);
        String token = Jwts.builder()
                .setSubject(mapper.writeValueAsString(jwtToken))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConfig.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConfig.SECRET.getBytes())
                .compact();
        response.addHeader(SecurityConfig.HEADER_STRING, SecurityConfig.TOKEN_PREFIX + token);
    }
}
