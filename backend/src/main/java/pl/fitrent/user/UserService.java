package pl.fitrent.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.fitrent.common.exception.ConflictException;
import pl.fitrent.security.JwtService;
import pl.fitrent.common.exception.BadRequestException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username is already taken");
        }

        User user = new User(
                request.username(),
                passwordEncoder.encode(request.password()),
                UserRole.USER
        );

        return UserResponse.fromEntity(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new pl.fitrent.common.exception.BadRequestException(
                        "Invalid username or password"
                ));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new pl.fitrent.common.exception.BadRequestException(
                    "Invalid username or password"
            );
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole()
        );
    }
}