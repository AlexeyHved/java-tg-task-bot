package com.github.alexeyhved.taskbot;

public enum Status {
    ACTIVE("Активно"),
    PLANNED("Ждём старта"),
    WAITING("Ожидает"),
    FINISHED("Завершено");

    public final String title;

    Status(String title) {
        this.title = title;
    }
}
