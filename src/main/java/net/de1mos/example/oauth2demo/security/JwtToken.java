package net.de1mos.example.oauth2demo.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {

    private String sessionId;

    private Long userId;

    private String username;

    private JwtTokenType type;

    public enum JwtTokenType {
        ANONYMOUS,
        CLIENT,
        REFRESH
    }
}
