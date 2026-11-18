package pl.fitrent.equipment;

import org.springframework.stereotype.Service;
import pl.fitrent.category.Category;
import pl.fitrent.category.CategoryRepository;
import pl.fitrent.common.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@Transactional
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final CategoryRepository categoryRepository;

    public EquipmentService(
            EquipmentRepository equipmentRepository,
            CategoryRepository categoryRepository
    ) {
        this.equipmentRepository = equipmentRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<EquipmentResponse> getAll(
            String search,
            Boolean active,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        String searchValue = search == null ? "" : search;

        Page<Equipment> equipmentPage;

        if (active == null) {
            equipmentPage = equipmentRepository
                    .findByNameContainingIgnoreCase(searchValue, pageable);
        } else {
            equipmentPage = equipmentRepository
                    .findByNameContainingIgnoreCaseAndActive(
                            searchValue,
                            active,
                            pageable
                    );
        }

        return equipmentPage.map(EquipmentResponse::fromEntity);
    }

    public EquipmentResponse getById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipment not found"));

        return EquipmentResponse.fromEntity(equipment);
    }

    public EquipmentResponse create(EquipmentRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Equipment equipment = new Equipment(
                request.name(),
                request.description(),
                category
        );

        if (request.active() != null) {
            equipment.setActive(request.active());
        }

        return EquipmentResponse.fromEntity(
                equipmentRepository.save(equipment)
        );
    }

    public EquipmentResponse update(Long id, EquipmentRequest request) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipment not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        equipment.setName(request.name());
        equipment.setDescription(request.description());
        equipment.setCategory(category);

        if (request.active() != null) {
            equipment.setActive(request.active());
        }

        return EquipmentResponse.fromEntity(
                equipmentRepository.save(equipment)
        );
    }

    public void delete(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipment not found"));

        equipmentRepository.delete(equipment);
    }
}