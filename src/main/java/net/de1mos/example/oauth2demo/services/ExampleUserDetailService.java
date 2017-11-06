package net.de1mos.example.oauth2demo.services;

import net.de1mos.example.oauth2demo.entities.ExampleUser;
import net.de1mos.example.oauth2demo.repository.UsersDao;
import net.de1mos.example.oauth2demo.security.AuthenticatedUserDetails;
import net.de1mos.example.oauth2demo.entities.UserSession;
import net.de1mos.example.oauth2demo.repository.UserSessionDao;
import net.de1mos.example.oauth2demo.security.UnauthenticatedUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ExampleUserDetailService implements UserDetailsService {

    @Autowired
    private UsersDao usersDao;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ExampleUser user = usersDao.findByUsernameHash(s);
        if (user != null) {
            return new AuthenticatedUserDetails(user);
        }
        throw new UsernameNotFoundException("Username not found");
    }
}
