package me.kolganov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import me.kolganov.taskmanager.domain.AppUser;
import me.kolganov.taskmanager.domain.Backlog;
import me.kolganov.taskmanager.domain.Project;
import me.kolganov.taskmanager.exceptions.ProjectIdException;
import me.kolganov.taskmanager.exceptions.ProjectNotFoundException;
import me.kolganov.taskmanager.repository.BacklogRepository;
import me.kolganov.taskmanager.repository.ProjectRepository;
import me.kolganov.taskmanager.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final BacklogRepository backlogRepository;
    private final UserRepository userRepository;

    @Override
    public Project saveOrUpdate(Project project, String username) {
        if (project.getId() != null) {
            projectRepository.findByIdentifierAndProjectLeader(project.getIdentifier(), username)
                    .orElseThrow(() -> new ProjectNotFoundException("Project not found in your account"));
        }

        try {
            AppUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));

            project.setIdentifier(project.getIdentifier().toUpperCase());
            project.setProjectLeader(user.getUsername());
            project.setUser(user);

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
    public Project findOne(String identifier, String username) {
        return getProjectForUser(identifier, username);
    }

    @Override
    public List<Project> findAll(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    @Override
    @Transactional
    public void delete(String identifier, String username) {
        getProjectForUser(identifier, username);
        projectRepository.deleteByIdentifier(identifier.toUpperCase());
    }

    private Project getProjectForUser(String identifier, String username) {
        Project project = projectRepository.findByIdentifier(identifier.toUpperCase())
                .orElseThrow(() -> new ProjectIdException("Project ID '" + identifier.toUpperCase() + "' dose not exist"));

        if (!project.getProjectLeader().equals(username))
            throw new ProjectNotFoundException("Project not found in your account");

        return project;
    }
}
