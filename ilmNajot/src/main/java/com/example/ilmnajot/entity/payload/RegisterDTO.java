package com.example.ilmnajot.entity.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class RegisterDTO {
    @NotNull(message = "fullName cannot be blank, please provider fullName")
    private String fullName;

    @NotNull(message = "username cannot be blank, please provider username")
    private String email;

    @Size(min = 8, max = 30, message = "your password should be at least 8")
    @NotNull(message = "password cannot be blank, please provider password")
    private String password;

}
