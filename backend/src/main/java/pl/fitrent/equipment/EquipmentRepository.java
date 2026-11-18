package pl.fitrent.equipment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Page<Equipment> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Equipment> findByNameContainingIgnoreCaseAndActive(
            String name,
            boolean active,
            Pageable pageable
    );
}