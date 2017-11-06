package net.de1mos.example.oauth2demo.repository;

import net.de1mos.example.oauth2demo.entities.UserSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSessionDao extends CrudRepository<UserSession, Long> {
    UserSession findBySessionId(String sessionId);
}
