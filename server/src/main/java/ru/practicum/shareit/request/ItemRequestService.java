package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper mapper;

    @Transactional
    public ItemRequestDto create(long userId, ItemRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemRequest request = ItemRequest.builder()
                .description(dto.getDescription())
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        return mapper.toDto(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> getOwnRequests(long userId) {
        validateUser(userId);
        List<ItemRequest> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return toResponseWithItems(requests);
    }

    @Transactional(readOnly = true)
    public List<ItemRequestResponseDto> getAllRequests(long userId) {
        validateUser(userId);
        List<ItemRequest> requests = requestRepository.findByRequestorIdNotOrderByCreatedDesc(userId);
        return toResponseWithItems(requests);
    }

    @Transactional(readOnly = true)
    public ItemRequestResponseDto getById(long userId, long requestId) {
        validateUser(userId);
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        return toResponseWithItems(List.of(request)).get(0);
    }

    private List<ItemRequestResponseDto> toResponseWithItems(List<ItemRequest> requests) {
        List<Long> ids = requests.stream().map(ItemRequest::getId).toList();

        Map<Long, List<ItemDto>> itemsByRequestId = itemRepository.findByRequestIdIn(ids).stream()
                .map(item -> {
                    ItemDto dto = new ItemDto();
                    dto.setId(item.getId());
                    dto.setName(item.getName());
                    dto.setDescription(item.getDescription());
                    dto.setAvailable(item.getAvailable());
                    dto.setRequestId(item.getRequest() != null ? item.getRequest().getId() : null);
                    return dto;
                })
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return requests.stream()
                .map(req -> {
                    ItemRequestResponseDto dto = new ItemRequestResponseDto();
                    dto.setId(req.getId());
                    dto.setDescription(req.getDescription());
                    dto.setCreated(req.getCreated());
                    dto.setItems(itemsByRequestId.getOrDefault(req.getId(), List.of()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private void validateUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
    }
}
