package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid BookingCreateDto dto) {
        return client.create(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long bookingId,
                                          @RequestParam boolean approved) {
        return client.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long bookingId) {
        return client.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getByBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(defaultValue = "0") @Min(0) int from,
                                              @RequestParam(defaultValue = "20") @Min(1) int size) {
        return client.getByBooker(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(defaultValue = "0") @Min(0) int from,
                                             @RequestParam(defaultValue = "20") @Min(1) int size) {
        return client.getByOwner(userId, state);
    }
}
