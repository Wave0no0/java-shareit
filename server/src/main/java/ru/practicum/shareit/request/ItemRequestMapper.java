package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public abstract class ItemRequestMapper {

    @Autowired
    protected ItemMapper itemMapper;

    @Mapping(ignore = true, target = "items")
    public abstract ItemRequestDto toDto(ItemRequest request);

    @Mapping(target = "requestor.id", source = "userId")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    public abstract ItemRequest toItemRequest(ItemRequestDto dto, Long userId);

    public ItemRequestDto toDtoWithItems(ItemRequest request) {
        ItemRequestDto dto = toDto(request);

        List<ItemCreateDto> itemsDto = request.getItems() != null
                ? request.getItems().stream()
                .map(itemMapper::toCreateDto)
                .collect(Collectors.toList())
                : List.of();

        dto.setItems(itemsDto);
        return dto;
    }
}
