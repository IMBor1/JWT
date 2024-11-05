package com.test.JWT.model;

import com.test.JWT.repository.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    private String password;


    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isAccountNonLocked = true;

    public User(String name, String email, Role role) {

    }

    public void setRole(Role role) {
        this.role = role;
    }
}

