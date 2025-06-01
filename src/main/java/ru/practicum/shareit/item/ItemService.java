package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper mapper;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ItemDto createItem(ItemCreateDto itemDto, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("There is no user with id=" + userId);
        }
        Item saved = itemRepository.save(mapper.toItem(itemDto, userId));
        return mapper.toItemDto(saved);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ItemDto update(ItemUpdateDto itemDto, long userId, long itemId) {
        Item toUpdate = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("There is no item with id=" + itemId));
        if (toUpdate.getOwner().getId() != userId) {
            throw new NotFoundException("Can't change item's owner");
        }
        updateNotNullFields(itemDto, toUpdate);
        itemRepository.save(toUpdate);
        return mapper.toItemDto(toUpdate);
    }

    @Transactional(readOnly = true)
    public ItemDto getItemById(long id, long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no item with id=" + id));
        return mapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getItemByUserId(long userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(mapper::toItemDto)
                .toList();
    }

    public List<ItemDto> searchByText(String text, long userId) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findByText(text).stream()
                .map(mapper::toItemDto)
                .toList();
    }

    private static void updateNotNullFields(ItemUpdateDto itemDto, Item toUpdate) {
        if (itemDto.getName() != null) {
            toUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            toUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            toUpdate.setAvailable(itemDto.getAvailable());
        }
    }
}
