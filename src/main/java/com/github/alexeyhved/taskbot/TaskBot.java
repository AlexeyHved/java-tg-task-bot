package com.github.alexeyhved.taskbot;


import com.github.alexeyhved.taskbot.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class TaskBot extends TelegramLongPollingBot {
    private final String botUserName;
    private final MainService mainService;


    public TaskBot(@Value("${tg.bot.token}") String botToken,
                   @Value("${tg.bot.name}") String botUserName,
                   TelegramBotsApi telegramBotsApi,
                   MainService mainService) throws TelegramApiException {

        super(botToken);
        this.botUserName = botUserName;
        this.mainService = mainService;
        telegramBotsApi.registerBot(this);
    }
    @Override
    public void onUpdateReceived(Update update) {
        List<BotApiMethod<?>> answers = handleUpdate(update);
        answers.forEach(answer -> {
            try {
                execute(answer);
            } catch (TelegramApiException e) {
                log.warn(e.getMessage());
            }
        });
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    private List<BotApiMethod<?>> handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return mainService.handleMsg(update.getMessage());
        }
        if (update.hasCallbackQuery()) {
            return mainService.handleCallback(update.getCallbackQuery());
        }
        return Collections.emptyList();
    }
}
