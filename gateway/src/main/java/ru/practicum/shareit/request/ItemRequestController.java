package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid ItemRequestDto dto) {
        return client.create(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return client.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return client.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long requestId) {
        return client.getById(userId, requestId);
    }
}
