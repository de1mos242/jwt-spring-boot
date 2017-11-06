package net.de1mos.example.oauth2demo.security;

import lombok.Data;
import net.de1mos.example.oauth2demo.entities.ExampleUser;
import net.de1mos.example.oauth2demo.entities.UserSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class AuthenticatedUserDetails implements UserDetails {

    private String username;

    private String password;

    private String sessionId;

    private Long userId;

    public AuthenticatedUserDetails(UserSession userSession) {
        this.username = userSession.getUser().getUsernameHash();
        this.userId = userSession.getUser().getId();
        this.sessionId = userSession.getSessionId();
        this.password = userSession.getUser().getPasswordHash();
    }

    public AuthenticatedUserDetails(ExampleUser user) {
        this.username = user.getUsernameHash();
        this.userId = user.getId();
        this.password = user.getPasswordHash();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
