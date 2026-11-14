package pl.fitrent.common.exception;

public record ApiError(
        int status,
        String error,
        String message
) {
}