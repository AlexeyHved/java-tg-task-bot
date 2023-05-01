package com.github.alexeyhved.taskbot.dto;

import com.github.alexeyhved.taskbot.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.github.alexeyhved.taskbot.constant.Buttons.*;
import static com.github.alexeyhved.taskbot.dto.TxtMapper.coverSymbols;

@AllArgsConstructor
@Getter
@Setter
public class WaitingGoalDto {
    private Long id;
    private String title;
    private Status status;
    /*private Double priority;
    private Double importance;
    private Integer daysBeforeDeadline;
    private Integer hoursBeforeDeadline;
    private Integer minutesBeforeDeadline;
    private Integer secsBeforeDeadline;*/

    @Override
    public String toString() {
        /*String importanceStr = NO_IMPORTANCE.title;
        if (importance == 1.5) importanceStr = MIDDLE_IMPORTANCE.title;
        if (importance == 2.0) importanceStr = VERY_IMPORTANCE.title;*/

        StringBuilder sb = new StringBuilder();

        sb.append("*").append(coverSymbols(title)).append("*").append("\n");
        //sb.append(buildDefStatusBar(priority)).append("\n");
        sb.append("\\-".repeat(3)).append("\n");
        sb.append("Статус: ").append(status.title).append("\n");
        sb.append("Дэдлайн уже прошел и цель пока на паузе");
        //sb.append("Приоритет: ").append(importanceStr).append("\n");

        /*if (secsBeforeDeadline > 0) {
            sb.append("До дедлайна: ");
        }
        if (daysBeforeDeadline > 0) {
            sb.append(daysBeforeDeadline).append(" ");
            String daysStr = String.valueOf(daysBeforeDeadline);
            if (daysStr.endsWith("11") || daysStr.endsWith("12") || daysStr.endsWith("13") || daysStr.endsWith("14"))
                sb.append("дней");
            else if (daysStr.endsWith("1")) sb.append("день");
            else if (daysStr.endsWith("2") || daysStr.endsWith("3") || daysStr.endsWith("4")) sb.append("дня");
            else sb.append("дней");
        }
        if (secsBeforeDeadline > 0) {
            sb.append(" ").append(hoursBeforeDeadline < 10 ? "0" + hoursBeforeDeadline : hoursBeforeDeadline).append(":")
                    .append(minutesBeforeDeadline < 10 ? "0" + minutesBeforeDeadline : minutesBeforeDeadline).append(":")
                    .append(secsBeforeDeadline < 10 ? "0" + secsBeforeDeadline : secsBeforeDeadline);
        }*/

        //sb.append("\n").append(buildDefStatusBar(priority));
        return sb.toString();
    }

    private String buildDefStatusBar(double priority) {
        StringBuilder sb = new StringBuilder();
        int intPriority = (int) priority;

        sb.append("▰".repeat(intPriority / 7));
        sb.append("▱".repeat(14 - intPriority / 7));
        sb.append(" *").append(String.format("%.2f", priority)).append("*");
        return sb.toString();
    }
}
