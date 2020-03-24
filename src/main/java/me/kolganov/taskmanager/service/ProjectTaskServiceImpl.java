package me.kolganov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import me.kolganov.taskmanager.domain.ProjectTask;
import me.kolganov.taskmanager.repository.BacklogRepository;
import me.kolganov.taskmanager.repository.ProjectTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectTaskServiceImpl implements ProjectTaskService {
    private final BacklogRepository backlogRepository;
    private final ProjectTaskRepository projectTaskRepository;

    @Override
    public ProjectTask addTask(String projectIdentifier, ProjectTask projectTask) {
        backlogRepository.findByProjectIdentifier(projectIdentifier.toUpperCase())
                .ifPresent(b -> {
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
                });
        return projectTaskRepository.save(projectTask);
    }

    @Override
    public List<ProjectTask> getAllByProjectIdentifier(String projectIdentifier) {
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
    }
}
