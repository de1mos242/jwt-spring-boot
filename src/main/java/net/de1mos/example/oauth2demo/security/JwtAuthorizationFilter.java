package net.de1mos.example.oauth2demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtTokenReader jwtTokenReader;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConfig.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConfig.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        JwtToken jwtToken = jwtTokenReader.getAuthentication(request);
        List<? extends GrantedAuthority> autorities = Collections.emptyList();
        switch (jwtToken.getType()) {
            case ANONYMOUS:
                autorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOS"));
                break;
            case CLIENT:
                autorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOS"));
        }
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(jwtToken.getUsername(), null, autorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
