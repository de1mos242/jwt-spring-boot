package net.de1mos.example.oauth2demo.security;

import lombok.Data;

@Data
public class ClientCredentials {
    private String username;
    private String password;
}
