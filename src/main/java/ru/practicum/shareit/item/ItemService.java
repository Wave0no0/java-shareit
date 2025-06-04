package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final ItemMapper mapper;
//
    public ItemDto create(long ownerId, ItemDto dto) {
        validateUser(ownerId);
        Item item = mapper.fromDto(dto, ownerId);
        return mapper.toDto(repository.save(item));
    }

    public ItemDto update(long ownerId, long itemId, ItemDto patch) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (item.getOwnerId() != ownerId) {
            throw new NotFoundException("Only owner can update the item");
        }
        mapper.update(item, patch);
        return mapper.toDto(repository.save(item));
    }

    public ItemDto getById(long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        return mapper.toDto(item);
    }

    public List<ItemDto> getByOwner(long ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) return List.of();
        String lower = text.toLowerCase();
        return repository.findAll().stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(lower)
                        || i.getDescription().toLowerCase().contains(lower))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    private void validateUser(long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("User not found");
        }
    }
}
