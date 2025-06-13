package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeAndDeserializeBookingDto() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        BookingDto dto = new BookingDto();
        dto.setId(42L);
        dto.setStatus(BookingStatus.APPROVED);
        dto.setStart(now);
        dto.setEnd(now.plusDays(1));
        UserDto userDto = new UserDto();
        userDto.setId(7L);
        userDto.setName("Booker");
        userDto.setEmail("booker@email.com");
        dto.setBooker(userDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(3L);
        itemDto.setName("Item");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);
        dto.setItem(itemDto);

        String json = objectMapper.writeValueAsString(dto);
        BookingDto deserialized = objectMapper.readValue(json, BookingDto.class);

        assertThat(deserialized.getId()).isEqualTo(dto.getId());
        assertThat(deserialized.getStatus()).isEqualTo(dto.getStatus());
        assertThat(deserialized.getStart()).isEqualTo(dto.getStart());
        assertThat(deserialized.getEnd()).isEqualTo(dto.getEnd());
        assertThat(deserialized.getBooker().getId()).isEqualTo(dto.getBooker().getId());
        assertThat(deserialized.getItem().getName()).isEqualTo(dto.getItem().getName());
    }
}
