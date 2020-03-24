package me.kolganov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import me.kolganov.taskmanager.domain.Project;
import me.kolganov.taskmanager.domain.ProjectTask;
import me.kolganov.taskmanager.exceptions.ProjectIdException;
import me.kolganov.taskmanager.exceptions.ProjectNotFoundException;
import me.kolganov.taskmanager.repository.BacklogRepository;
import me.kolganov.taskmanager.repository.ProjectRepository;
import me.kolganov.taskmanager.repository.ProjectTaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectTaskServiceImpl implements ProjectTaskService {
    private final BacklogRepository backlogRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectRepository projectRepository;

    @Override
    public ProjectTask addTask(String projectIdentifier, ProjectTask projectTask) {
        return backlogRepository.findByProjectIdentifier(projectIdentifier.toUpperCase())
                .map(b -> {
                    projectTask.setBacklog(b);
                    Integer backlogSequence = b.getPTSequence();
                    backlogSequence++;
                    b.setPTSequence(backlogSequence);
                    projectTask.setProjectSequence(b.getProjectIdentifier() + "-" + backlogSequence);
                    projectTask.setProjectIdentifier(b.getProjectIdentifier());

                    if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                        projectTask.setPriority(3);
                    }

                    if (projectTask.getStatus() == null || "".equals(projectTask.getStatus())) {
                        projectTask.setStatus("TODO");
                    }
                    return projectTaskRepository.save(projectTask);
                })
                .orElseThrow(() -> new ProjectNotFoundException("Project dose not exist"));
    }

    @Override
    public List<ProjectTask> getAllByProjectIdentifier(String projectIdentifier) {
        projectRepository.findByIdentifier(projectIdentifier)
                .orElseThrow(() -> new ProjectIdException(String.format("Project ID '%s' dose not exist", projectIdentifier.toUpperCase())));

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
    }

    @Override
    public ProjectTask findOne(String backlogId, String projectSequence) {
        return getValidProjectTask(backlogId, projectSequence);
    }

    @Override
    public ProjectTask update(String backlogId, String projectSequence, ProjectTask updatedTask) {
        ProjectTask projectTask = getValidProjectTask(backlogId, projectSequence);

        updatedTask.setProjectSequence(projectSequence.toUpperCase());
        updatedTask.setId(projectTask.getId());
        BeanUtils.copyProperties(updatedTask, projectTask);
        return projectTaskRepository.save(projectTask);
    }

    @Override
    public void delete(String backlogId, String projectSequence) {
        ProjectTask projectTask = getValidProjectTask(backlogId, projectSequence);
        projectTaskRepository.delete(projectTask);
    }

    private ProjectTask getValidProjectTask(String backlogId, String projectSequence) {
        return backlogRepository.findByProjectIdentifier(backlogId.toUpperCase())
                .map(backlog -> backlog.getProjectTasks().stream()
                        .filter(task -> projectSequence.equals(task.getProjectSequence()))
                        .findFirst()
                        .orElseThrow(() -> new ProjectNotFoundException(String.format("Task ID '%s' dose not exist in Project ID '%s'", projectSequence.toUpperCase(), backlogId.toUpperCase()))))
                .orElseThrow(() -> new ProjectIdException(String.format("Project ID '%s' dose not exist", backlogId.toUpperCase())));
    }
}
