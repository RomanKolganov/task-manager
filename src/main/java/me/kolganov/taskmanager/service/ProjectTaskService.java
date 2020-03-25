package me.kolganov.taskmanager.service;

import me.kolganov.taskmanager.domain.ProjectTask;

import java.util.List;

public interface ProjectTaskService {
    ProjectTask addTask(String projectIdentifier, ProjectTask projectTask, String username);
    List<ProjectTask> getAllByProjectIdentifier(String projectIdentifier, String username);
    ProjectTask findOne(String backlogId, String projectSequence, String username);
    ProjectTask update(String backlogId, String projectSequence, ProjectTask updatedTask, String username);
    void delete(String backlogId, String projectSequence, String username);
}
