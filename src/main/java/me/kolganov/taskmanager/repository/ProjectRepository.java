package me.kolganov.taskmanager.repository;

import me.kolganov.taskmanager.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdentifier(String identifier);
}
