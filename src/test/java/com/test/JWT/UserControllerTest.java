package com.test.JWT;

import com.test.JWT.controller.UserController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    @Test
    public void testUser() {
        UserController userController = new UserController();
        Model model = Mockito.mock(Model.class);
        OAuth2User principal = Mockito.mock(OAuth2User.class);

        Mockito.when(principal.getAttribute("name")).thenReturn("John Doe");
        Mockito.when(principal.getAttribute("email")).thenReturn("john.doe@example.com");

        String viewName = userController.user(principal, model);

        Mockito.verify(model).addAttribute("name", "John Doe");
        Mockito.verify(model).addAttribute("email", "john.doe@example.com");
        assertEquals("user", viewName);
    }
}
