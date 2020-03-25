package me.kolganov.taskmanager.repository;

import me.kolganov.taskmanager.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
}
