package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public User fromDto(UserDto dto) {
        User user = new User();
        user.setId(dto.getId()); // может быть null при создании
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }
}
