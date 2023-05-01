package com.github.alexeyhved.taskbot.repo;

import com.github.alexeyhved.taskbot.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GoalRepo extends JpaRepository<GoalEntity, Long> {
    Optional<GoalEntity> findByTitle(String tittle);

    @Query("select g from GoalEntity g where g.owner.id = :ownerId and current_timestamp > g.deadLine and g.status = 'ACTIVE' order by g.deadLine")
    List<GoalEntity> findWaiting(Long ownerId);

    @Query("select g from GoalEntity g where g.owner.id = :ownerId and g.status = 'FINISHED' order by g.deadLine")
    List<GoalEntity> findFinished(Long ownerId);

    @Query("select g from GoalEntity g where g.owner.id = :ownerId and current_timestamp between g.start and g.deadLine and g.status = 'ACTIVE'")
    List<GoalEntity> findCurrent(Long ownerId);

    @Query("select g from GoalEntity g where g.owner.id = :ownerId and current_timestamp < g.start and g.status = 'ACTIVE'")
    List<GoalEntity> findBeforeStart(Long ownerId);

    @Query("select count(g) from GoalEntity g where g.owner.id = :ownerId and current_timestamp between g.start and g.deadLine and g.status = 'ACTIVE'")
    Integer countCurrent(Long ownerId);

    @Query("select count(g) from GoalEntity g where g.owner.id = :ownerId and current_timestamp < g.start and g.status = 'ACTIVE'")
    Integer countPlanned(Long ownerId);

    @Query("select count(g) from GoalEntity g where g.owner.id = :ownerId and current_timestamp > g.deadLine and g.status = 'ACTIVE'")
    Integer countWaiting(Long ownerId);

    @Query("select count(g) from GoalEntity g where g.owner.id = :ownerId and g.status = 'FINISHED'")
    Integer countFinished(Long ownerId);

    GoalEntity findByOwnerId(Long id);
}
