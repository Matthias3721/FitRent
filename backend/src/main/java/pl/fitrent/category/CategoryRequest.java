package pl.fitrent.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name can have at most 100 characters")
        String name,

        @Size(max = 500, message = "Description can have at most 500 characters")
        String description

) {
}