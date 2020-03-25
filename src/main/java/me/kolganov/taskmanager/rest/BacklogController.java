package me.kolganov.taskmanager.rest;

import lombok.RequiredArgsConstructor;
import me.kolganov.taskmanager.domain.ProjectTask;
import me.kolganov.taskmanager.service.MapValidationErrorService;
import me.kolganov.taskmanager.service.ProjectTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class BacklogController {
    private final ProjectTaskService projectTaskService;
    private final MapValidationErrorService mapValidationErrorService;

    @PostMapping("api/backlog/{backlog_id}")
    public ResponseEntity<?> addProjectTaskToBacklog(@PathVariable("backlog_id") String backlogId,
                                                     @Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult result,
                                                     Principal principal) {
        ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
        if (errorMap != null)
            return errorMap;

        return ResponseEntity.status(HttpStatus.CREATED).body(
                projectTaskService.addTask(backlogId, projectTask, principal.getName()));
    }

    @GetMapping("api/backlog/{backlog_id}")
    public ResponseEntity<List<ProjectTask>> getBacklog(@PathVariable("backlog_id") String backlogId,
                                                        Principal principal) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(projectTaskService.getAllByProjectIdentifier(backlogId, principal.getName()));
    }

    @GetMapping("api/backlog/{backlog_id}/{project_sequence}")
    public ResponseEntity<?> getProjectTask(@PathVariable("backlog_id") String backlogId,
                                            @PathVariable("project_sequence") String projectSequence,
                                            Principal principal) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(projectTaskService.findOne(backlogId, projectSequence, principal.getName()));
    }

    @PutMapping("api/backlog/{backlog_id}/{project_sequence}")
    public ResponseEntity<?> updateTask(@PathVariable("backlog_id") String backlogId,
                                        @PathVariable("project_sequence") String projectSequence,
                                        @Valid @RequestBody ProjectTask projectTask,
                                        BindingResult result,
                                        Principal principal) {
        ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
        if (errorMap != null)
            return errorMap;

        return ResponseEntity.status(HttpStatus.OK)
                .body(projectTaskService.update(backlogId, projectSequence, projectTask, principal.getName()));
    }

    @DeleteMapping("api/backlog/{backlog_id}/{project_sequence}")
    public ResponseEntity<?> deleteTask(@PathVariable("backlog_id") String backlogId,
                                        @PathVariable("project_sequence") String projectSequence,
                                        Principal principal) {
        projectTaskService.delete(backlogId, projectSequence, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Project task TD '%s' was deleted successfully", projectSequence));
    }
}
