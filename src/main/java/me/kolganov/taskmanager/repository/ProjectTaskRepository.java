package me.kolganov.taskmanager.repository;

import me.kolganov.taskmanager.domain.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {
}
