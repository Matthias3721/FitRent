package pl.fitrent.user;

public record UserResponse(
        Long id,
        String username,
        UserRole role
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}