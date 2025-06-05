package ru.practicum.shareit.item;

import ru.practicum.shareit.item.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);

    Optional<Item> findById(long id);

    List<Item> findAll();

    List<Item> findByOwnerId(long ownerId);
}
