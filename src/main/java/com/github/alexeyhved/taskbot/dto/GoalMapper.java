package com.github.alexeyhved.taskbot.dto;

import com.github.alexeyhved.taskbot.Status;
import com.github.alexeyhved.taskbot.entity.GoalEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class GoalMapper {
    public ActiveGoalDto buildActiveGoalDto(GoalEntity goal) {
        ZoneId zoneId = ZoneId.systemDefault();
        long start = goal.getStart().atZone(zoneId).toEpochSecond();
        long deadline = goal.getDeadLine().atZone(zoneId).toEpochSecond();
        Double importance = goal.getImportance();
        long now = LocalDateTime.now().atZone(zoneId).toEpochSecond();

        double percent = (deadline - start) / 50D;
        double percentOffset = (now - start) / percent;
        double priority = percentOffset * importance;

        long secBeforeDeadline = deadline - now;
        int days = (int) (secBeforeDeadline / 3600 / 24);
        int hours = (int) (secBeforeDeadline / 3600 % 24);
        int minutes = (int) (secBeforeDeadline % 3600 / 60);
        int sec = (int) (secBeforeDeadline % 3600 % 60);

        return new ActiveGoalDto(goal.getId(), goal.getTitle(), Status.ACTIVE, priority, importance, days, hours, minutes, sec);
    }

    public BeforeStartGoalDto buildBeforeStartGoalDto(GoalEntity goal) {
        ZoneId zoneId = ZoneId.systemDefault();
        long createdOn = goal.getCreatedOn().atZone(zoneId).toEpochSecond();
        long start = goal.getStart().atZone(zoneId).toEpochSecond();
        Double importance = goal.getImportance();
        long now = LocalDateTime.now().atZone(zoneId).toEpochSecond();

        double percent = (start - createdOn) / 50D;
        double percentOffset = (now - createdOn) / percent;
        double priority = percentOffset * importance;

        long secBeforeStart = start - now;
        int days = (int) (secBeforeStart / 3600 / 24);
        int hours = (int) (secBeforeStart / 3600 % 24);
        int minutes = (int) (secBeforeStart % 3600 / 60);
        int sec = (int) (secBeforeStart % 3600 % 60);

        return new BeforeStartGoalDto(goal.getId(), goal.getTitle(), Status.PLANNED, priority, importance, days, hours, minutes, sec);
    }

    public FinishedGoalDto buildFinishedGoalDto(GoalEntity goal) {
        return new FinishedGoalDto(goal.getId(), goal.getTitle(), goal.getStatus());
    }

    public WaitingGoalDto buildWaitingGoalDto(GoalEntity goal) {
        return new WaitingGoalDto(goal.getId(), goal.getTitle(), Status.WAITING);
    }

    public FullBeforeStartDto buildFullBeforeStartDto(GoalEntity goal) {
        ZoneId zoneId = ZoneId.systemDefault();
        long createdOn = goal.getCreatedOn().atZone(zoneId).toEpochSecond();
        long start = goal.getStart().atZone(zoneId).toEpochSecond();
        long deadline = goal.getDeadLine().atZone(zoneId).toEpochSecond();
        Double importance = goal.getImportance();
        long now = LocalDateTime.now().atZone(zoneId).toEpochSecond();

        double percent = (start - createdOn) / 50D;
        double percentOffset = (now - createdOn) / percent;
        double priority = percentOffset * importance;

        long secBeforeStart = start - now;
        int days = (int) (secBeforeStart / 3600 / 24);
        int hours = (int) (secBeforeStart / 3600 % 24);
        int minutes = (int) (secBeforeStart % 3600 / 60);
        int sec = (int) (secBeforeStart % 3600 % 60);
        return new FullBeforeStartDto(goal.getId(), goal.getTitle(), Status.PLANNED,
                priority, importance, days, hours, minutes, sec, goal.getStart(), goal.getDeadLine());
    }

    public FullActiveGoalDto buildFullActiveGoalDto(GoalEntity goal) {
        ZoneId zoneId = ZoneId.systemDefault();
        long start = goal.getStart().atZone(zoneId).toEpochSecond();
        long deadline = goal.getDeadLine().atZone(zoneId).toEpochSecond();
        Double importance = goal.getImportance();
        long now = LocalDateTime.now().atZone(zoneId).toEpochSecond();

        double percent = (deadline - start) / 50D;
        double percentOffset = (now - start) / percent;
        double priority = percentOffset * importance;

        long secBeforeDeadline = deadline - now;
        int days = (int) (secBeforeDeadline / 3600 / 24);
        int hours = (int) (secBeforeDeadline / 3600 % 24);
        int minutes = (int) (secBeforeDeadline % 3600 / 60);
        int sec = (int) (secBeforeDeadline % 3600 % 60);

        return new FullActiveGoalDto(goal.getId(), goal.getTitle(), Status.ACTIVE,
                priority, importance, days, hours, minutes, sec, goal.getStart(), goal.getDeadLine());
    }

    public WaitingGoalDto buildFullWaitGoalDto(GoalEntity goal) {
        return new WaitingGoalDto(goal.getId(), goal.getTitle(), Status.WAITING);
    }

    public FullFinishedGoalDto buildFullFinishedGoalDto(GoalEntity goal) {
        return new FullFinishedGoalDto(goal.getId(), goal.getTitle(),
                goal.getStatus(), goal.getImportance(),
                goal.getStart(), goal.getDeadLine());

    }
}
