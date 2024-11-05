package com.test.JWT;

import com.test.JWT.model.Role;
import com.test.JWT.model.User;
import com.test.JWT.repository.UserRepository;
import com.test.JWT.service.OurUserDetailedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OurUserDetailedServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OurUserDetailedService ourUserDetailedService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setRole(Role.USER);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UserDetails userDetails = ourUserDetailedService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("invalidUser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            ourUserDetailedService.loadUserByUsername("invalidUser");
        });
    }
}
