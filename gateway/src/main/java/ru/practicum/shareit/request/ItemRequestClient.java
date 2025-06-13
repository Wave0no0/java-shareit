package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Component
public class ItemRequestClient extends BaseClient {

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate rest) {
        super(serverUrl + "/requests", rest);
    }

    public ResponseEntity<Object> create(long userId, ItemRequestDto dto) {
        return post("", dto, userId, Object.class);
    }

    public ResponseEntity<Object> getOwnRequests(long userId) {
        return get("", userId, Object.class);
    }

    public ResponseEntity<Object> getAllRequests(long userId) {
        return get("/all", userId, Object.class);
    }

    public ResponseEntity<Object> getById(long userId, long requestId) {
        return get("/" + requestId, userId, Object.class);
    }
}
