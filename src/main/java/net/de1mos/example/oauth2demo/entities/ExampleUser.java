package net.de1mos.example.oauth2demo.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
@Data
public class ExampleUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username_hash")
    private String usernameHash;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "real_name")
    private String realName;
}
