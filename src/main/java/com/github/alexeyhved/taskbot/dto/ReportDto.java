package com.github.alexeyhved.taskbot.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReportDto {
    private int current;
    private int planned;
    private int waiting;
    private int finished;
    private int all;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Миссий выполнено: ").append(finished).append("\n");
        sb.append(buildDefStatusBar(finished)).append("\n");
        sb.append("В процессе: ").append(current).append("\n");
        sb.append(buildDefStatusBar(current)).append("\n");
        sb.append("В ожидании старта: ").append(planned).append("\n");
        sb.append(buildDefStatusBar(planned)).append("\n");
        sb.append("Всего: ").append(all);
        return sb.toString();
    }

    private String buildDefStatusBar(int count) {
        double percent = count / (all / 100d);

        StringBuilder sb = new StringBuilder();
        sb.append("▰".repeat((int)percent / 7));
        sb.append("▱".repeat(14 - (int)percent / 7));
        sb.append(" *").append(TxtMapper.coverSymbols(String.format("%.1f", percent))).append(" %").append("*");
        return sb.toString();
    }
}
