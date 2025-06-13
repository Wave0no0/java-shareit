package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);
    List<Item> findByRequestIdIn(List<Long> requestIds);
}
