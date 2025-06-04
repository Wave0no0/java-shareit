package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}
