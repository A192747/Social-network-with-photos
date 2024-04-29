package ru.micro.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUploadHeaders {
    @NotNull()
    private int id;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Имя пользователя не может быть пустым!")
    private String name;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Роль пользователя не может быть пустой!")
    private String role;
}
