package me.kolganov.taskmanager.rest;

import lombok.RequiredArgsConstructor;
import me.kolganov.taskmanager.domain.Project;
import me.kolganov.taskmanager.service.MapValidationErrorService;
import me.kolganov.taskmanager.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final MapValidationErrorService mapValidationErrorService;

    @PostMapping("api/project")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {
        ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
        if (errorMap != null)
            return errorMap;

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.saveOrUpdate(project));
    }
}
