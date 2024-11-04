package com.test.JWT;

import com.test.JWT.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JWTUtilsTest {

    @InjectMocks
    private JWTUtils jwtUtils;

    @Mock
    private UserDetails userDetails;

    private String username = "testUser";
    private String token;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userDetails.getUsername()).thenReturn(username);
        token = jwtUtils.generateToken(userDetails);
    }

    @Test
    public void testGenerateToken() {
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testExtractUsername() {
        String extractedUsername = jwtUtils.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testIsTokenValid_ValidToken() {
        boolean isValid = jwtUtils.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    public void testIsTokenValid_InvalidToken() {
        String invalidToken = token + "invalid";
        boolean isValid = jwtUtils.isTokenValid(invalidToken, userDetails);
        assertFalse(isValid);
    }
}