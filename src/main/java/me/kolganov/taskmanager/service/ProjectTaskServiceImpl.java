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
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectRepository projectRepository;

    @Override
    public ProjectTask addTask(String projectIdentifier, ProjectTask projectTask, String username) {
        return projectRepository.findByIdentifierAndProjectLeader(projectIdentifier.toUpperCase(), username)
                .map(Project::getBacklog)
                .map(backlog -> {
                    projectTask.setBacklog(backlog);
                    Integer backlogSequence = backlog.getPTSequence();
                    backlogSequence++;
                    backlog.setPTSequence(backlogSequence);
                    projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backlogSequence);
                    projectTask.setProjectIdentifier(backlog.getProjectIdentifier());

                    if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                        projectTask.setPriority(3);
                    }

                    if (projectTask.getStatus() == null || "".equals(projectTask.getStatus())) {
                        projectTask.setStatus("TODO");
                    }
                    return projectTaskRepository.save(projectTask);
                }).orElseThrow(() -> new ProjectNotFoundException("Project dose not exist"));
    }

    @Override
    public List<ProjectTask> getAllByProjectIdentifier(String projectIdentifier, String username) {
        projectRepository.findByIdentifierAndProjectLeader(projectIdentifier.toUpperCase(), username)
                .orElseThrow(() -> new ProjectNotFoundException(String.format("Project ID '%s' dose not exist", projectIdentifier.toUpperCase())));

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier.toUpperCase());
    }

    @Override
    public ProjectTask findOne(String backlogId, String projectSequence, String username) {
        return getValidProjectTask(backlogId, projectSequence, username);
    }

    @Override
    public ProjectTask update(String backlogId, String projectSequence, ProjectTask updatedTask, String username) {
        ProjectTask projectTask = getValidProjectTask(backlogId, projectSequence, username);

        updatedTask.setProjectSequence(projectSequence.toUpperCase());
        updatedTask.setId(projectTask.getId());
        BeanUtils.copyProperties(updatedTask, projectTask);
        return projectTaskRepository.save(projectTask);
    }

    @Override
    public void delete(String backlogId, String projectSequence, String username) {
        ProjectTask projectTask = getValidProjectTask(backlogId, projectSequence, username);
        projectTaskRepository.delete(projectTask);
    }

    private ProjectTask getValidProjectTask(String backlogId, String projectSequence, String username) {
        return projectRepository.findByIdentifierAndProjectLeader(backlogId.toUpperCase(), username)
                .map(Project::getBacklog)
                .map(backlog -> backlog.getProjectTasks().stream()
                        .filter(task -> projectSequence.equals(task.getProjectSequence()))
                        .findFirst()
                        .orElseThrow(() -> new ProjectNotFoundException(String.format("Task ID '%s' dose not exist in Project ID '%s'", projectSequence.toUpperCase(), backlogId.toUpperCase()))))
                .orElseThrow(() -> new ProjectIdException(String.format("Project ID '%s' dose not exist", backlogId.toUpperCase())));
    }
}
