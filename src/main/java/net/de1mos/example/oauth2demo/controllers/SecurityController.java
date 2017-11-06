package net.de1mos.example.oauth2demo.controllers;

import net.de1mos.example.oauth2demo.entities.ExampleUser;
import net.de1mos.example.oauth2demo.entities.UserSession;
import net.de1mos.example.oauth2demo.repository.UserSessionDao;
import net.de1mos.example.oauth2demo.repository.UsersDao;
import net.de1mos.example.oauth2demo.security.ClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping
public class SecurityController {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private UserSessionDao userSessionDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/users/sign-up")
    public UserSession signUp(@RequestBody ClientCredentials credentials,
                              Principal user) {
        ExampleUser exampleUser = new ExampleUser();
        exampleUser.setRealName(credentials.getUsername());
        exampleUser.setUsernameHash(String.valueOf(credentials.getUsername().hashCode()));
        exampleUser.setPasswordHash(encoder.encode(credentials.getPassword()));
        ExampleUser storedExampleUser = usersDao.save(exampleUser);

        UserSession userSession = userSessionDao.findBySessionId(user.getName());
        userSession.setUser(storedExampleUser);
        return userSessionDao.save(userSession);
    }
}
