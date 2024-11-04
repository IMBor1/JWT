package com.test.JWT.service;

import com.test.JWT.model.User;
import com.test.JWT.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class AuthenticationService {
private final PasswordEncoder passwordEncoder;
    private int failedAttempts = 0;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private UserRepository userRepository;

    public void authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            failedAttempts++;
            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountNonLocked(false);
                userRepository.save(user);
            }
            throw new BadCredentialsException("Invalid credentials");
        }
        failedAttempts = 0;
    }
}
