package com.github.alexeyhved.taskbot.dto;

import com.github.alexeyhved.taskbot.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.github.alexeyhved.taskbot.dto.TxtMapper.coverSymbols;

@AllArgsConstructor
@Getter
@Setter
public class WaitingGoalDto {
    private Long id;
    private String title;
    private Status status;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("*").append(coverSymbols(title)).append("*").append("\n");
        sb.append("\\-".repeat(3)).append("\n");
        sb.append("Статус: ").append(status.title).append("\n");
        sb.append("Дэдлайн уже прошел и цель пока на паузе");
        return sb.toString();
    }
}
