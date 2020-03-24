package me.kolganov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import me.kolganov.taskmanager.domain.Backlog;
import me.kolganov.taskmanager.domain.Project;
import me.kolganov.taskmanager.exceptions.ProjectIdException;
import me.kolganov.taskmanager.repository.BacklogRepository;
import me.kolganov.taskmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final BacklogRepository backlogRepository;

    @Override
    public Project saveOrUpdate(Project project) {
        try {
            project.setIdentifier(project.getIdentifier().toUpperCase());

            if (project.getId() == null) {
                project.setBacklog(Backlog.builder()
                        .project(project)
                        .projectIdentifier(project.getIdentifier().toUpperCase())
                        .PTSequence(0)
                        .projectTasks(new ArrayList<>())
                        .build());
            }

            if (project.getId() != null) {
                backlogRepository.findByProjectIdentifier(project.getIdentifier().toUpperCase())
                        .ifPresent(project::setBacklog);
            }

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getIdentifier().toUpperCase() + "' already exist");
        }
    }

    @Override
    public Project findOne(String identifier) {
        return projectRepository.findByIdentifier(identifier.toUpperCase())
                .orElseThrow(() -> new ProjectIdException("Project ID '" + identifier.toUpperCase() + "' dose not exist"));
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(String identifier) {
        projectRepository.findByIdentifier(identifier.toUpperCase())
                .orElseThrow(() -> new ProjectIdException("Project ID '" + identifier.toUpperCase() + "' dose not exist"));

        projectRepository.deleteByIdentifier(identifier.toUpperCase());
    }
}
