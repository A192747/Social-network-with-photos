package org.oril.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Имя не может быть пустым!")
    private String name;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Поле для пароля не может быть пустым!")
    private String password;
}
