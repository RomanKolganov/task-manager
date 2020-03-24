package me.kolganov.taskmanager.rest;

import lombok.RequiredArgsConstructor;
import me.kolganov.taskmanager.domain.Project;
import me.kolganov.taskmanager.service.MapValidationErrorService;
import me.kolganov.taskmanager.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@CrossOrigin
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

    @GetMapping("api/project/{identifier}")
    public ResponseEntity<?> getProject(@PathVariable("identifier") String identifier) {
        Project project = projectService.findOne(identifier);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @GetMapping("api/project")
    public ResponseEntity<?> getAllProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(
                projectService.findAll().stream().collect(Collectors.toList()));
    }

    @DeleteMapping("api/project/{identifier}")
    public ResponseEntity<?> deleteProject(@PathVariable("identifier") String identifier) {
        projectService.delete(identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Project with ID: '" + identifier.toUpperCase() + "' was deleted");
    }
}
