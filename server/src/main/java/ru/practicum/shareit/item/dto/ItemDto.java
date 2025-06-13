package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Поле available обязательно")
    private Boolean available;

    private List<CommentDto> comments;

    private BookingDtoLite lastBooking;
    private BookingDtoLite nextBooking;

    private Long requestId;
}
