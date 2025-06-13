package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Component
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(restTemplate, serverUrl + API_PREFIX);
    }

    public ResponseEntity<Object> create(long userId, ItemDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> update(long userId, long itemId, ItemDto dto) {
        return patch("/" + itemId, userId, dto);
    }

    public ResponseEntity<Object> getById(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getByOwner(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> search(String text, long userId) {
        return get("/search?text={text}", userId, Map.of("text", text));
    }
}
