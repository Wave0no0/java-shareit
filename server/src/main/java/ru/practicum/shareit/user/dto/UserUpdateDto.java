package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserUpdateDto {
    private String name;
    @Email
    private String email;
}
