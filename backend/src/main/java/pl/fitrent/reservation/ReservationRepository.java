package pl.fitrent.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByEquipmentIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
            Long equipmentId,
            ReservationStatus status,
            OffsetDateTime endTime,
            OffsetDateTime startTime
    );
}