package pl.fitrent.reservation;

import org.springframework.stereotype.Service;
import pl.fitrent.equipment.Equipment;
import pl.fitrent.equipment.EquipmentRepository;
import pl.fitrent.common.exception.BadRequestException;
import pl.fitrent.common.exception.ConflictException;
import pl.fitrent.common.exception.NotFoundException;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EquipmentRepository equipmentRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            EquipmentRepository equipmentRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public List<ReservationResponse> getAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();
    }

    public ReservationResponse getById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        return ReservationResponse.fromEntity(reservation);
    }

    public ReservationResponse create(ReservationRequest request) {
        Equipment equipment = equipmentRepository.findById(request.equipmentId())
                .orElseThrow(() -> new NotFoundException("Equipment not found"));

        if (!equipment.isActive()) {
            throw new ConflictException("Equipment is not active");
        }

        if (!request.endTime().isAfter(request.startTime())) {
            throw new BadRequestException("End time must be after start time");
        }

        boolean overlapping =
                reservationRepository
                        .existsByEquipmentIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                                equipment.getId(),
                                ReservationStatus.CONFIRMED,
                                request.endTime(),
                                request.startTime()
                        );

        if (overlapping) {
            throw new ConflictException(
                    "Equipment is already reserved in this time range"
            );
        }

        Reservation reservation = new Reservation(
                equipment,
                request.reservedBy(),
                request.startTime(),
                request.endTime()
        );

        return ReservationResponse.fromEntity(
                reservationRepository.save(reservation)
        );
    }

    public ReservationResponse cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        reservation.setStatus(ReservationStatus.CANCELLED);

        return ReservationResponse.fromEntity(
                reservationRepository.save(reservation)
        );
    }

    public void delete(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        reservationRepository.delete(reservation);
    }
}