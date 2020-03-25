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
import java.security.Principal;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final MapValidationErrorService mapValidationErrorService;

    @PostMapping("api/project")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project,
                                              BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
        if (errorMap != null)
            return errorMap;

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.saveOrUpdate(project, principal.getName()));
    }

    @GetMapping("api/project/{identifier}")
    public ResponseEntity<?> getProject(@PathVariable("identifier") String identifier, Principal principal) {
        Project project = projectService.findOne(identifier, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @GetMapping("api/project")
    public ResponseEntity<?> getAllProjects(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(
                projectService.findAll(principal.getName()));
    }

    @DeleteMapping("api/project/{identifier}")
    public ResponseEntity<?> deleteProject(@PathVariable("identifier") String identifier, Principal principal) {
        projectService.delete(identifier, principal.getName());
        return ResponseEntity.status(HttpStatus.OK)
                .body("Project with ID: '" + identifier.toUpperCase() + "' was deleted");
    }
}
