package net.de1mos.example.oauth2demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class JwtTokenReader {

    private ObjectMapper mapper;

    public JwtTokenReader(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JwtToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConfig.HEADER_STRING);
        if (token != null) {
            // parse the token.
            String claim = Jwts.parser()
                    .setSigningKey(SecurityConfig.SECRET.getBytes())
                    .parseClaimsJws(token.replace(SecurityConfig.TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
            if (claim != null) {
                try {
                    return mapper.readValue(claim, JwtToken.class);
                } catch (IOException e) {
                    return null;
                }
            }
            return null;
        }
        return null;
    }
}
