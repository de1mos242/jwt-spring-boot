package net.de1mos.example.oauth2demo.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.de1mos.example.oauth2demo.repository.UserSessionDao;
import net.de1mos.example.oauth2demo.repository.UsersDao;
import net.de1mos.example.oauth2demo.security.AnonymousAuthenticationProvider;
import net.de1mos.example.oauth2demo.security.JwtAuthenticationAnonymousFilter;
import net.de1mos.example.oauth2demo.security.JwtAuthenticationClientFilter;
import net.de1mos.example.oauth2demo.security.JwtAuthorizationFilter;
import net.de1mos.example.oauth2demo.security.JwtTokenReader;
import net.de1mos.example.oauth2demo.services.ExampleUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String SIGN_UP_URL = "/users/sign-up";

    @Autowired
    private ExampleUserDetailService exampleUserDetailService;

    @Autowired
    private AnonymousAuthenticationProvider anonymousAuthenticationProvider;

    @Autowired
    private UserSessionDao userSessionDao;

    @Autowired
    private JwtTokenReader jwtTokenReader;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private ObjectMapper objectsMapper;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(exampleUserDetailService).passwordEncoder(bCryptPasswordEncoder());
        auth.authenticationProvider(anonymousAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationAnonymousFilter jwtAuthenticationAnonymousFilter = getJwtAuthenticationAnonymousFilter();

        JwtAuthenticationClientFilter jwtAuthenticationClientFilter =
                getJwtAuthenticationClientFilter();
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(SIGN_UP_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(jwtAuthenticationAnonymousFilter)
                .addFilter(jwtAuthenticationClientFilter)
                .addFilter(new JwtAuthorizationFilter(authenticationManager()));
    }

    private JwtAuthenticationClientFilter getJwtAuthenticationClientFilter() throws Exception {
        JwtAuthenticationClientFilter jwtAuthenticationClientFilter =
                new JwtAuthenticationClientFilter(authenticationManager(),
                        jwtTokenReader,
                        usersDao,
                        objectsMapper,
                        userSessionDao);
        jwtAuthenticationClientFilter.setFilterProcessesUrl("/token/gain-client");
        return jwtAuthenticationClientFilter;
    }

    private JwtAuthenticationAnonymousFilter getJwtAuthenticationAnonymousFilter() throws Exception {
        JwtAuthenticationAnonymousFilter jwtAuthenticationAnonymousFilter =
                new JwtAuthenticationAnonymousFilter(authenticationManager(), userSessionDao, objectsMapper);
        jwtAuthenticationAnonymousFilter.setFilterProcessesUrl("/token/gain-anonymous");
        return jwtAuthenticationAnonymousFilter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
