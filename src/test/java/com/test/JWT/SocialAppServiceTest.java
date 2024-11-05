package com.test.JWT;

import com.test.JWT.model.Role;
import com.test.JWT.model.User;
import com.test.JWT.repository.UserRepository;
import com.test.JWT.service.SocialAppService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class SocialAppServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SocialAppService socialAppService;

    public SocialAppServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUser_NewUser() {
        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);

        when(oAuth2User.getAttribute("email")).thenReturn("john.doe@example.com");
        when(oAuth2User.getAttribute("name")).thenReturn("John Doe");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(null);

        OAuth2User result = socialAppService.loadUser(userRequest);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testLoadUser_ExistingUser() {
        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);

        when(oAuth2User.getAttribute("email")).thenReturn("john.doe@example.com");

        User existingUser = new User("John Doe", "john.doe@example.com", Role.USER);

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(existingUser);

        OAuth2User result = socialAppService.loadUser(userRequest);

        assertNotNull(result);
        verify(userRepository, never()).save(any(User.class));
    }
}
