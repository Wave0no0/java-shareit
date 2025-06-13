package ru.practicum.shareit.booking.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BookingClient extends BaseClient {

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(restTemplate, serverUrl + "/bookings");
    }

    public ResponseEntity<Object> create(long userId, BookingCreateDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> approve(long userId, long bookingId, boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId, null); // ✅ передаём null как body
    }

    public ResponseEntity<Object> getById(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getByBooker(long userId, String state) {
        return get("?state=" + state, userId);
    }

    public ResponseEntity<Object> getByOwner(long userId, String state) {
        return get("/owner?state=" + state, userId);
    }
}
