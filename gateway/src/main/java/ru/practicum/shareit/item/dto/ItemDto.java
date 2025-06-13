package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Available status must be provided")
    private Boolean available;

    private Long requestId;

}
