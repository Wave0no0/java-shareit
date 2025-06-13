package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Component
public class ItemRequestClient extends BaseClient {

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(restTemplate, serverUrl + "/requests"); // ✅ правильный порядок
    }

    public ResponseEntity<Object> create(long userId, ItemRequestDto dto) {
        return post("", userId, dto); // ✅ подходит под BaseClient.post(String, long, T)
    }

    public ResponseEntity<Object> getOwnRequests(long userId) {
        return get("", userId); // ✅ get(String, long)
    }

    public ResponseEntity<Object> getAllRequests(long userId, int from, int size) {
        Map<String, Object> params = Map.of("from", from, "size", size);
        return get("/all?from={from}&size={size}", userId, params); // ✅ get(String, long, Map)
    }

    public ResponseEntity<Object> getById(long userId, long requestId) {
        return get("/" + requestId, userId); // ✅ get(String, long)
    }
}
