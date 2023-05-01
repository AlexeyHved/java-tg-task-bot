package com.github.alexeyhved.taskbot.repo;

import com.github.alexeyhved.taskbot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
}
