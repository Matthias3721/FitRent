package pl.fitrent.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.fitrent.category.Category;
import pl.fitrent.common.exception.ConflictException;
import pl.fitrent.equipment.Equipment;
import pl.fitrent.equipment.EquipmentRepository;
import pl.fitrent.common.exception.BadRequestException;

import static org.mockito.Mockito.mock;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void shouldRejectOverlappingReservation() {
        Equipment equipment = mock(Equipment.class);

        when(equipment.getId()).thenReturn(1L);
        when(equipment.isActive()).thenReturn(true);

        OffsetDateTime start =
                OffsetDateTime.parse("2026-12-01T10:00:00+01:00");

        OffsetDateTime end =
                OffsetDateTime.parse("2026-12-01T12:00:00+01:00");

        ReservationRequest request = new ReservationRequest(
                1L,
                "Mateusz",
                start,
                end
        );

        when(equipmentRepository.findById(1L))
                .thenReturn(Optional.of(equipment));

        when(reservationRepository
                .existsByEquipmentIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                        1L,
                        ReservationStatus.CONFIRMED,
                        end,
                        start
                ))
                .thenReturn(true);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> reservationService.create(request)
        );

        assertEquals(
                "Equipment is already reserved in this time range",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectReservationForInactiveEquipment() {
        Equipment equipment = mock(Equipment.class);

        when(equipmentRepository.findById(1L))
                .thenReturn(Optional.of(equipment));

        when(equipment.isActive()).thenReturn(false);

        ReservationRequest request = new ReservationRequest(
                1L,
                "Mateusz",
                OffsetDateTime.parse("2026-12-01T10:00:00+01:00"),
                OffsetDateTime.parse("2026-12-01T12:00:00+01:00")
        );

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> reservationService.create(request)
        );

        assertEquals(
                "Equipment is not active",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectReservationWhenEndTimeIsBeforeStartTime() {
        Equipment equipment = mock(Equipment.class);

        when(equipmentRepository.findById(1L))
                .thenReturn(Optional.of(equipment));

        when(equipment.isActive()).thenReturn(true);

        ReservationRequest request = new ReservationRequest(
                1L,
                "Mateusz",
                OffsetDateTime.parse("2026-12-01T12:00:00+01:00"),
                OffsetDateTime.parse("2026-12-01T10:00:00+01:00")
        );

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> reservationService.create(request)
        );

        assertEquals(
                "End time must be after start time",
                exception.getMessage()
        );
    }
}