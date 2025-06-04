package ru.practicum.shareit.item.entity;

import lombok.Data;

@Data
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
}
