package me.kolganov.taskmanager.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsernameAlreadyExistsExceptionResponse {
    private String username;
}
