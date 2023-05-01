package com.github.alexeyhved.taskbot.dto;

import com.github.alexeyhved.taskbot.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.github.alexeyhved.taskbot.Const.DT;
import static com.github.alexeyhved.taskbot.constant.Buttons.*;

@AllArgsConstructor
@Getter
@Setter
public class FullFinishedGoalDto {
    private Long id;
    private String title;
    private Status status;
    private Double importance;
    private LocalDateTime createdOn;
    private LocalDateTime deadline;

    @Override
    public String toString() {
        String importanceStr = NO_IMPORTANCE.title;
        if (importance == 1.5) importanceStr = MIDDLE_IMPORTANCE.title;
        if (importance == 2.0) importanceStr = VERY_IMPORTANCE.title;

        StringBuilder sb = new StringBuilder();

        sb.append("*").append(TxtMapper.coverSymbols(title)).append("*").append("\n");
        sb.append("\\-".repeat(3)).append("\n");
        sb.append("Приоритет: ").append(importanceStr).append("\n");
        sb.append("Статус: ").append(status.title).append("\n");
        sb.append("Старт: ").append(createdOn.format(DT)).append("\n");
        sb.append("Дэдлайн: ").append(deadline.format(DT));

        return sb.toString();
    }
}
