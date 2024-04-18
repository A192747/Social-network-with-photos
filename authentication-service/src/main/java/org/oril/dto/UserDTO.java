package org.oril.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Имя не может быть пустым!")
    private String name;
    @NotNull
    @Size(min = 1)
    @NotEmpty(message = "Поле для пароля не может быть пустым!")
    private String password;
}
