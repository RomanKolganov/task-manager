package me.kolganov.taskmanager.repository;

import me.kolganov.taskmanager.domain.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {
    List<ProjectTask> findByProjectIdentifierOrderByPriority(String projectIdentifier);
}
