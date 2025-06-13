package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getById_shouldReturnBooking() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setStatus(BookingStatus.APPROVED);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item");
        itemDto.setDescription("Desc");
        itemDto.setAvailable(true);
        dto.setItem(itemDto);

        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setName("Booker");
        userDto.setEmail("booker@email.com");
        dto.setBooker(userDto);

        Mockito.when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(dto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.status").value(dto.getStatus().toString()))
                .andExpect(jsonPath("$.booker.id").value(2))
                .andExpect(jsonPath("$.item.id").value(1));
    }
}
