package me.kolganov.taskmanager.repository;

import me.kolganov.taskmanager.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
