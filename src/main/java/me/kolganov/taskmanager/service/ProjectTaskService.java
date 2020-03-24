package me.kolganov.taskmanager.service;

import me.kolganov.taskmanager.domain.ProjectTask;

import java.util.List;

public interface ProjectTaskService {
    ProjectTask addTask(String projectIdentifier, ProjectTask projectTask);
    List<ProjectTask> getAllByProjectIdentifier(String projectIdentifier);
    ProjectTask findOne(String backlogId, String projectSequence);
    ProjectTask update(String backlogId, String projectSequence, ProjectTask updatedTask);
    void delete(String backlogId, String projectSequence);
}
