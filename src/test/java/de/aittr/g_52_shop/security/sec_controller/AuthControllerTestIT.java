package de.aittr.g_52_shop.security.sec_controller;

import de.aittr.g_52_shop.domain.entity.Role;
import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.repository.UserRepository;
import de.aittr.g_52_shop.security.sec_dto.TokenResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class AuthControllerTestIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private UserRepository userRepository;

    private User testUser;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        testUser = createTestUser();
        mockUser = createMockUser();
        when(userRepository.findByUsername("Test user")).thenReturn(Optional.of(mockUser));
    }

    @Test
    public void checkSuccessWhileLogin() {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<User> request = new HttpEntity<>(testUser, headers);
        ResponseEntity<TokenResponseDto> response = restTemplate.exchange(
                "/auth/login", HttpMethod.POST, request, TokenResponseDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAccessToken());
        assertNotNull(response.getBody().getRefreshToken());
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername("Test user");
        user.setPassword("777");
        return user;
    }

    private User createMockUser() {
        Role role = new Role();
        role.setTitle("ROLE_USER");

        User user = new User();
        user.setId(777L);
        user.setActive(true);
        user.setUsername("Test user");
        user.setRoles(Set.of(role));
        user.setPassword(new BCryptPasswordEncoder().encode("777"));

        return user;
    }
}