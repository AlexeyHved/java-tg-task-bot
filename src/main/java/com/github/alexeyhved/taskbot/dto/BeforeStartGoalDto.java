package com.github.alexeyhved.taskbot.dto;

import com.github.alexeyhved.taskbot.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BeforeStartGoalDto {
    private Long id;
    private String title;
    private Status status;
    private Double priority;
    private Double importance;
    private Integer daysBeforeStart;
    private Integer hoursBeforeStart;
    private Integer minutesBeforeStart;
    private Integer secsBeforeStart;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("*").append(TxtMapper.coverSymbols(title)).append("*").append("\n").append("\\-".repeat(3)).append("\n");
        if (secsBeforeStart > 0) {
            sb.append("До старта: ");
        }
        if (daysBeforeStart > 0) {
            sb.append(daysBeforeStart).append(" ");
            String daysStr = String.valueOf(daysBeforeStart);
            if (daysStr.endsWith("11") || daysStr.endsWith("12") || daysStr.endsWith("13") || daysStr.endsWith("14"))
                sb.append("дней");
            else if (daysStr.endsWith("1")) sb.append("день");
            else if (daysStr.endsWith("2") || daysStr.endsWith("3") || daysStr.endsWith("4")) sb.append("дня");
            else sb.append("дней");
        }
        if (secsBeforeStart > 0) {
            sb.append(" ").append(hoursBeforeStart < 10 ? "0" + hoursBeforeStart : hoursBeforeStart).append(":")
                    .append(minutesBeforeStart < 10 ? "0" + minutesBeforeStart : minutesBeforeStart).append(":")
                    .append(secsBeforeStart < 10 ? "0" + secsBeforeStart : secsBeforeStart);
        }

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
