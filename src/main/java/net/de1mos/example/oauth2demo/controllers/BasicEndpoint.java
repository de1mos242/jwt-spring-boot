package net.de1mos.example.oauth2demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class BasicEndpoint {

    @GetMapping("/")
    public String getCurrentUser(Principal principal) {

        return "Principal: " + principal.toString();
    }
}
