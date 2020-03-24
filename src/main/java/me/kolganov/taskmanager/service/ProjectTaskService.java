package me.kolganov.taskmanager.service;

import me.kolganov.taskmanager.domain.ProjectTask;

public interface ProjectTaskService {
    ProjectTask addTask(String projectIdentifier, ProjectTask projectTask);
}
