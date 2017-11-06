package net.de1mos.example.oauth2demo.repository;

import net.de1mos.example.oauth2demo.entities.ExampleUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao extends CrudRepository<ExampleUser, Long> {
    ExampleUser findByUsernameHash(String username);
}
