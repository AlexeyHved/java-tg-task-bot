package com.github.alexeyhved.taskbot.dto;

import com.github.alexeyhved.taskbot.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FinishedGoalDto {
    private Long id;
    private String title;
    private Status status;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("*").append(TxtMapper.coverSymbols(title)).append("*").append("\n");
        sb.append("\\-".repeat(3)).append("\n");
        sb.append("Статус: ").append(status.title).append("\n");
        return sb.toString();
    }
}
