package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Data
public class BookingCreateDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
