package pl.fitrent.user;

public record AuthResponse(
        String token,
        String username,
        UserRole role
) {
}