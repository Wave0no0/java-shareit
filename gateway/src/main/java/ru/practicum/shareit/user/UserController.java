package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto dto) {
        return client.create(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id) {
        return client.getById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return client.getAll();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable long id,
                                         @RequestBody @Valid UserDto dto) {
        return client.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        return client.delete(id);
    }
}
