package me.kolganov.taskmanager.service;

import me.kolganov.taskmanager.domain.Project;

import java.util.List;

public interface ProjectService {
    Project saveOrUpdate(Project project, String username);
    Project findOne(String identifier, String username);
    List<Project> findAll(String username);
    void delete(String identifier, String username);
}
