package me.kolganov.taskmanager.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapValidationErrorServiceImpl implements MapValidationErrorService {

    @Override
    public ResponseEntity<?> validate(BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            result.getFieldErrors().forEach(e -> errorMap.put(e.getField(), e.getDefaultMessage()));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        return null;
    }
}
