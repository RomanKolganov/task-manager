package me.kolganov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import me.kolganov.taskmanager.domain.Project;
import me.kolganov.taskmanager.exceptions.ProjectIdException;
import me.kolganov.taskmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    @Override
    public Project saveOrUpdate(Project project) {
        try {
            project.setIdentifier(project.getIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getIdentifier().toUpperCase() + "' already exist");
        }
    }

    @Override
    public Project findProject(String identifier) {
        return projectRepository.findByIdentifier(identifier.toUpperCase())
                .orElseThrow(() -> new ProjectIdException("Project ID '" + identifier.toUpperCase() + "' dose not exist"));
    }
}
