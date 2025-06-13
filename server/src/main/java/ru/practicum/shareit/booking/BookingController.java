package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody @Valid BookingCreateDto dto) {
        return bookingService.create(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam boolean approved) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByOwner(userId, state);
    }
}
