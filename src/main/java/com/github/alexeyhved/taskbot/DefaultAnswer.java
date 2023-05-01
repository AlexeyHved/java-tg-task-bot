package com.github.alexeyhved.taskbot;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public class DefaultAnswer {
    public static SendMessage NotImplMsg(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Функия не поддерживается")
                .build();
    }

    public static List<BotApiMethod<?>> ErrorMsg(Long chatId, String error) {
        return List.of(SendMessage.builder()
                .chatId(chatId)
                .text(error)
                .build());
    }
}
