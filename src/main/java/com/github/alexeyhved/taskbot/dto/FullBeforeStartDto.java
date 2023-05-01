package com.github.alexeyhved.taskbot.dto;

import com.github.alexeyhved.taskbot.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.github.alexeyhved.taskbot.Const.DT;
import static com.github.alexeyhved.taskbot.constant.Buttons.*;
import static com.github.alexeyhved.taskbot.dto.TxtMapper.coverSymbols;

@AllArgsConstructor
@Getter
@Setter
public class FullBeforeStartDto {
    private Long id;
    private String title;
    private Status status;
    private Double priority;
    private Double importance;
    private Integer daysBeforeStart;
    private Integer hoursBeforeStart;
    private Integer minutesBeforeStart;
    private Integer secsBeforeStart;
    private LocalDateTime createdOn;
    private LocalDateTime deadline;

    @Override
    public String toString() {
        String importanceStr = NO_IMPORTANCE.title;
        if (importance == 1.5) importanceStr = MIDDLE_IMPORTANCE.title;
        if (importance == 2.0) importanceStr = VERY_IMPORTANCE.title;

        StringBuilder sb = new StringBuilder();
        sb.append("*").append(TxtMapper.coverSymbols(title)).append("*").append("\n").append("\\-".repeat(3)).append("\n");
        sb.append("Приоритет: ").append(importanceStr).append("\n");
        sb.append("Статус: ").append(status.title).append("\n");
        sb.append("Старт: ").append(createdOn.format(DT)).append("\n");
        sb.append("Дэдлайн: ").append(deadline.format(DT));
        sb.append("\n").append(buildDefStatusBar(priority));
        return sb.toString();
    }

    private String buildDefStatusBar(double priority) {
        StringBuilder sb = new StringBuilder();
        int intPriority = (int) priority;

        sb.append("▱".repeat(intPriority / 7));
        sb.append("▰".repeat(14 - intPriority / 7));
        sb.append(" *").append(TxtMapper.coverSymbols(String.format("%.2f", priority))).append("*");
        return sb.toString();
    }
}
