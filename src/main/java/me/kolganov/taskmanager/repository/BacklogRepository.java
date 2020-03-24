package me.kolganov.taskmanager.repository;

import me.kolganov.taskmanager.domain.Backlog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BacklogRepository extends JpaRepository<Backlog, Long> {
    Optional<Backlog> findByProjectIdentifier(String projectIdentifier);
}
