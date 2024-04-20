package org.oril.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Имя не может быть пустым!")
    private String name;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Поле для пароля не может быть пустым!")
    private String password;
    @NotNull
    @NotEmpty(message = "Роль не может быть пустой!")
    private Roles role;
}

