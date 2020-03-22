package me.kolganov.taskmanager.service;

import me.kolganov.taskmanager.domain.Project;

public interface ProjectService {
    Project saveOrUpdate(Project project);
    Project findProject(String identifier);
}
