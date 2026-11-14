package pl.fitrent.reservation;

import java.time.OffsetDateTime;

public record ReservationResponse(
        Long id,
        Long equipmentId,
        String equipmentName,
        String reservedBy,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        ReservationStatus status,
        OffsetDateTime createdAt
) {
    public static ReservationResponse fromEntity(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getEquipment().getId(),
                reservation.getEquipment().getName(),
                reservation.getReservedBy(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus(),
                reservation.getCreatedAt()
        );
    }
}