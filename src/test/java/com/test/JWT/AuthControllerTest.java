package com.test.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.JWT.model.AuthRequest;
import com.test.JWT.model.Role;
import com.test.JWT.model.User;
import com.test.JWT.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private String validUsername = "user";
    private String validPassword = "password";

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername(validUsername);
        user.setPassword(validPassword);
        user.setRole(Role.USER);

        user.setPassword(new BCryptPasswordEncoder().encode(validPassword));
        userRepository.save(user);
    }

    @Test
    public void testLoginSuccess() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(validUsername);
        authRequest.setPassword(validPassword);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("invalidUser");
        authRequest.setPassword("invalidPassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testAccessUserEndpoint() throws Exception {
        mockMvc.perform(get("/api/profile"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "moderator", roles = {"MODERATOR"})
    @Test
    public void testAccessModeratorEndpoint() throws Exception {
        mockMvc.perform(get("/api/moderate"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", roles = {"SUPER_ADMIN"})
    @Test
    public void testAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testAccessAdminEndpointForbiddenForUser() throws Exception {
        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isForbidden());
    }
}