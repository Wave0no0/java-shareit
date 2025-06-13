package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid ItemDto dto) {
        return client.create(userId, dto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable long itemId,
                                         @RequestBody ItemDto dto) {
        return client.update(userId, itemId, dto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long itemId) {
        return client.getById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getByOwner(@RequestHeader("X-Sharer-User-Id") long userId) {
        return client.getByOwner(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam String text) {
        return client.search(text, userId);
    }
}
