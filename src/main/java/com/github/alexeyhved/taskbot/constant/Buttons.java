package com.github.alexeyhved.taskbot.constant;

public enum Buttons {
    FIND("Найти"),
    VERY_IMPORTANCE("Очень важно"),
    MIDDLE_IMPORTANCE("Умеренно"),
    NO_IMPORTANCE("Не важно"),
    MENU("▼"),
    CLOSE_MENU("▲"),
    COMPLETE("Миссия выполнена"),
    PROLONG("Продлить"),
    DEL("Удалить"),
    NEW_GOAL("Новая цель");

    public final String title;

    Buttons(String impl) {
        this.title = impl;
    }
}
