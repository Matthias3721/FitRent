package pl.fitrent.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.fitrent.common.exception.ConflictException;
import pl.fitrent.security.JwtService;
import pl.fitrent.common.exception.BadRequestException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRejectRegistrationWhenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest(
                "mateusz",
                "haslo123"
        );

        when(userRepository.existsByUsername("mateusz"))
                .thenReturn(true);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.register(request)
        );

        assertEquals(
                "Username is already taken",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectLoginWhenPasswordIsIncorrect() {
        User user = new User(
                "mateusz",
                "encoded-password",
                UserRole.USER
        );

        LoginRequest request = new LoginRequest(
                "mateusz",
                "zlehaslo"
        );

        when(userRepository.findByUsername("mateusz"))
                .thenReturn(java.util.Optional.of(user));

        when(passwordEncoder.matches(
                "zlehaslo",
                "encoded-password"
        )).thenReturn(false);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> userService.login(request)
        );

        assertEquals(
                "Invalid username or password",
                exception.getMessage()
        );
    }
}