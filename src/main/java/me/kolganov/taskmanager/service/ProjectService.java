package me.kolganov.taskmanager.service;

import me.kolganov.taskmanager.domain.Project;

import java.util.List;

public interface ProjectService {
    Project saveOrUpdate(Project project);
    Project findOne(String identifier);
    List<Project> findAll();
}
