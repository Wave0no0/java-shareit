package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserClient extends BaseClient {

    public UserClient(@Value("${shareit-server.url}") String serverUrl) {
        super(new RestTemplate());
        this.rest.setUriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/users"));
    }

    public ResponseEntity<Object> create(UserDto dto) {
        return post("", 0, dto);
    }

    public ResponseEntity<Object> getById(long userId) {
        return get("/" + userId, userId);
    }

    public ResponseEntity<Object> getAll() {
        return get("", 0);
    }

    public ResponseEntity<Object> update(long userId, UserDto dto) {
        return patch("/" + userId, userId, dto);
    }

    public ResponseEntity<Object> delete(long userId) {
        return super.delete("/" + userId, userId);
    }
}
