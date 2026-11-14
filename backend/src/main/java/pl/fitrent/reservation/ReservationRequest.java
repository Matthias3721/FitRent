package pl.fitrent.reservation;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record ReservationRequest(

        @NotNull(message = "Equipment id is required")
        Long equipmentId,

        @NotBlank(message = "Reserved by is required")
        String reservedBy,

        @NotNull(message = "Start time is required")
        @Future(message = "Start time must be in the future")
        OffsetDateTime startTime,

        @NotNull(message = "End time is required")
        @Future(message = "End time must be in the future")
        OffsetDateTime endTime

) {
}