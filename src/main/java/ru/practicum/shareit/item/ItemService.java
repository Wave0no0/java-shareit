package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper mapper;

    @Transactional
    public ItemDto create(long ownerId, ItemDto dto) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Item item = mapper.fromDto(dto, owner);
        return mapper.toDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto update(long ownerId, long itemId, ItemDto patch) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Only owner can update the item");
        }
        mapper.update(item, patch);
        return mapper.toDto(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public ItemDto getById(long userId, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        ItemDto dto = mapper.toDto(item);

        dto.setComments(commentRepository.findByItemId(itemId).stream()
                .map(mapper::toCommentDto)
                .collect(Collectors.toList()));

        // Показываем бронирования только владельцу
        if (item.getOwner().getId().equals(userId)) {
            List<Booking> bookings = bookingRepository.findByItem_IdIn(List.of(itemId));
            LocalDateTime now = LocalDateTime.now();

            dto.setLastBooking(bookings.stream()
                    .filter(b -> b.getStart().isBefore(now))
                    .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                    .max(Comparator.comparing(Booking::getEnd))
                    .map(mapper::toBookingDtoLite)
                    .orElse(null));

            dto.setNextBooking(bookings.stream()
                    .filter(b -> b.getStart().isAfter(now))
                    .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                    .min(Comparator.comparing(Booking::getStart))
                    .map(mapper::toBookingDtoLite)
                    .orElse(null));
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getByOwner(long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());

        List<Booking> bookings = bookingRepository.findByItem_IdIn(itemIds);
        Map<Long, List<Booking>> bookingsByItem = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                .collect(Collectors.groupingBy(b -> b.getItem().getId()));

        return items.stream()
                .map(item -> {
                    ItemDto dto = mapper.toDto(item);

                    dto.setComments(commentRepository.findByItemId(item.getId()).stream()
                            .map(mapper::toCommentDto)
                            .collect(Collectors.toList()));

                    List<Booking> itemBookings = bookingsByItem.getOrDefault(item.getId(), List.of());
                    LocalDateTime now = LocalDateTime.now();

                    dto.setLastBooking(itemBookings.stream()
                            .filter(b -> b.getStart().isBefore(now))
                            .max(Comparator.comparing(Booking::getEnd))
                            .map(mapper::toBookingDtoLite)
                            .orElse(null));

                    dto.setNextBooking(itemBookings.stream()
                            .filter(b -> b.getStart().isAfter(now))
                            .min(Comparator.comparing(Booking::getStart))
                            .map(mapper::toBookingDtoLite)
                            .orElse(null));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) return List.of();
        String lower = text.toLowerCase();
        return itemRepository.findAll().stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(lower)
                        || i.getDescription().toLowerCase().contains(lower))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentDto dto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean hasUsed = bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndBefore(
                userId, itemId, BookingStatus.APPROVED, LocalDateTime.now()
        );

        if (!hasUsed) {
            throw new ValidationException("User has not used this item");
        }

        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        comment.setText(dto.getText());

        return mapper.toCommentDto(commentRepository.save(comment));
    }

    private void validateUser(long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("User not found");
        }
    }
}
