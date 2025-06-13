package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;

@Component
public class CommentClient extends BaseClient {

    public CommentClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(restTemplate, serverUrl + "/items");
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentDto dto) {
        return post("/" + itemId + "/comment", userId, dto);
    }
}
