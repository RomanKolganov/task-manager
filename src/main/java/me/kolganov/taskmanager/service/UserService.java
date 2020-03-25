package me.kolganov.taskmanager.service;

import me.kolganov.taskmanager.domain.AppUser;

public interface UserService {
    AppUser saveUser(AppUser user);
}
