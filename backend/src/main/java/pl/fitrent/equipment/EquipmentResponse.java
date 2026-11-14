package pl.fitrent.equipment;

public record EquipmentResponse(
        Long id,
        String name,
        String description,
        boolean active,
        Long categoryId,
        String categoryName
) {
    public static EquipmentResponse fromEntity(Equipment equipment) {
        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getDescription(),
                equipment.isActive(),
                equipment.getCategory().getId(),
                equipment.getCategory().getName()
        );
    }
}