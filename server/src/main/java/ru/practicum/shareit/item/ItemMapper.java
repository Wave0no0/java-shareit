package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.BookingDtoLite;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.request.ItemRequest;

@Component
public class ItemMapper {

    public Item fromDto(ItemDto dto, User owner, ItemRequest request) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(owner);
        item.setRequest(request);
        return item;
    }

    public void update(Item item, ItemDto patch) {
        if (patch.getName() != null) item.setName(patch.getName());
        if (patch.getDescription() != null) item.setDescription(patch.getDescription());
        if (patch.getAvailable() != null) item.setAvailable(patch.getAvailable());
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }

    public BookingDtoLite toBookingDtoLite(Booking booking) {
        BookingDtoLite dto = new BookingDtoLite();
        dto.setId(booking.getId());
        dto.setBookerId(booking.getBooker().getId());
        return dto;
    }

    public ItemDto toDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        if (item.getRequest() != null) {
            dto.setRequestId(item.getRequest().getId());
        }
        return dto;
    }
}
