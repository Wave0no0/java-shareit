package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

@Component
public class ItemRequestMapper {

    public ItemRequest toEntity(ItemRequestDto dto, User requestor) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
    }

    public ItemRequestDto toDto(ItemRequest entity) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setCreated(entity.getCreated());
        return dto;
    }
}
