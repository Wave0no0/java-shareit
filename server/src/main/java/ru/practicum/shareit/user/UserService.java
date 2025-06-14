package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getById(long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return mapper.toDto(user);
    }

    public UserDto create(UserDto userDto) {
        boolean emailExists = repository.findAll().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(userDto.getEmail()));
        if (emailExists) {
            throw new IllegalArgumentException("Email уже используется другим пользователем");
        }

        User user = mapper.fromDto(userDto);
        return mapper.toDto(repository.save(user));
    }

    public UserDto update(long id, UserDto userDto) {
        User existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (userDto.getEmail() != null &&
                !userDto.getEmail().equalsIgnoreCase(existing.getEmail()) &&
                repository.findAll().stream()
                        .anyMatch(u -> u.getEmail().equalsIgnoreCase(userDto.getEmail()))) {
            throw new IllegalArgumentException("Email уже используется другим пользователем");
        }

        if (userDto.getName() != null) {
            existing.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existing.setEmail(userDto.getEmail());
        }

        return mapper.toDto(repository.save(existing));
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
