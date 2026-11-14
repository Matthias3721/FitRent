package pl.fitrent.equipment;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public List<EquipmentResponse> getAll() {
        return equipmentService.getAll();
    }

    @GetMapping("/{id}")
    public EquipmentResponse getById(@PathVariable Long id) {
        return equipmentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipmentResponse create(
            @Valid @RequestBody EquipmentRequest request
    ) {
        return equipmentService.create(request);
    }

    @PutMapping("/{id}")
    public EquipmentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentRequest request
    ) {
        return equipmentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        equipmentService.delete(id);
    }
}