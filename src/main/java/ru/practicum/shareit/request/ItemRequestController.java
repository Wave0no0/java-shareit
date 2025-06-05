package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {

    private final UserRepository userRepository;

    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private long requestIdCounter = 1;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody ItemRequestDto dto) {
        validateUser(userId);

        ItemRequest request = new ItemRequest();
        request.setId(requestIdCounter++);
        request.setDescription(dto.getDescription());
        request.setRequestorId(userId);
        request.setCreated(LocalDateTime.now());
        requests.put(request.getId(), request);

        return toDto(request);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long requestId) {
        validateUser(userId);

        ItemRequest request = requests.get(requestId);
        if (request == null) {
            throw new NotFoundException("Request not found");
        }

        return toDto(request);
    }

    @GetMapping
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        validateUser(userId);

        return new ArrayList<>(requests.values()).stream()
                .filter(r -> Objects.equals(r.getRequestorId(), userId))
                .map(this::toDto)
                .toList();
    }

    private void validateUser(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User not found");
        }
    }

    private ItemRequestDto toDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequestorId(request.getRequestorId());
        dto.setCreated(request.getCreated());
        return dto;
    }
}
