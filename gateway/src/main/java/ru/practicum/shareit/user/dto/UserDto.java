package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 128, message = "Name must be at most 128 characters")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Size(max = 128, message = "Email must be at most 128 characters")
    private String email;
}
