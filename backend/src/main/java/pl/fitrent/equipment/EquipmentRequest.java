package pl.fitrent.equipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipmentRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "Name can have at most 150 characters")
        String name,

        @Size(max = 500, message = "Description can have at most 500 characters")
        String description,

        @NotNull(message = "Category id is required")
        Long categoryId,

        Boolean active

) {
}