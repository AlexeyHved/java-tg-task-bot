package com.github.alexeyhved.taskbot.service;

import com.github.alexeyhved.taskbot.DefaultAnswer;
import com.github.alexeyhved.taskbot.Status;
import com.github.alexeyhved.taskbot.constant.Buttons;
import com.github.alexeyhved.taskbot.constant.Smile;
import com.github.alexeyhved.taskbot.dto.GoalMapper;
import com.github.alexeyhved.taskbot.entity.GoalEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.github.alexeyhved.taskbot.Const.DEADLINE;
import static com.github.alexeyhved.taskbot.Const.START;
import static com.github.alexeyhved.taskbot.DefaultAnswer.ErrorMsg;
import static com.github.alexeyhved.taskbot.DefaultAnswer.NotImplMsg;
import static com.github.alexeyhved.taskbot.constant.Buttons.*;


@Service
@RequiredArgsConstructor
public class MainService {

    private final CommandService commandService;
    private final KeyboardBuilder keyboardBuilder;
    private final GoalService goalService;
    private final GoalMapper goalMapper;
    private static final String BOT_COMMAND = "bot_command";

    public List<BotApiMethod<?>> handleMsg(Message msg) {
        List<MessageEntity> entities = msg.getEntities();

        if (entities != null) {
            for (MessageEntity entity : entities) {
                switch (entity.getType()) {
                    case BOT_COMMAND -> {
                        return commandService.handle(entity, msg);
                    }
                }
            }
        }
        if (msg.hasText()) {
            return replyToText(msg);
        }

        return List.of(NotImplMsg(msg.getChatId()));

    }

    public List<BotApiMethod<?>> handleCallback(CallbackQuery callback) {
        Long chatId = callback.getMessage().getChatId();

        if (Arrays.stream(values()).anyMatch(b -> b.name().equals(callback.getData()))) {
            switch (Buttons.valueOf(callback.getData())) {
                case NEW_GOAL -> {
                    return createGoal(callback, chatId);
                }
                case VERY_IMPORTANCE, MIDDLE_IMPORTANCE, NO_IMPORTANCE -> {
                    return setImportance(callback);
                }
            }
        }

        String[] data = callback.getData().split(":");
        if (Arrays.stream(Month.values()).anyMatch(m -> m.name().equals(data[1]))) {
            switch (Month.valueOf(data[1])) {
                case JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER -> {
                    if (data[0].equals(START)) {
                        return setStartMonth(callback, data);
                    }
                    if (data[0].equals(DEADLINE)) {
                        return setDeadLineMonth(callback, data);
                    }
                }
            }
        }
        if (Character.isDigit(data[1].charAt(0)) && data[1].length() < 3) {
            if (data[0].equals(START)) {
                return setStartDay(callback, data);
            }
            if (data[0].equals(DEADLINE)) {
                return setDeadLine(callback, data);
            }
        }
        if (Character.isDigit(data[1].charAt(0)) && data[1].length() > 2) {
            if (data[0].equals(START)) {
                return setStartYear(callback, data);
            }
            if (data[0].equals(DEADLINE)) {
                return setDeadLineYear(callback, data);
            }
        }
        if (Arrays.stream(values()).anyMatch(b -> b.name().equals(data[1]))) {
            switch (Buttons.valueOf(data[1])) {
                case MENU -> {
                    return getOpenedMenu(callback, data);
                }
                case CLOSE_MENU -> {
                    return getClosedMenu(callback, data);
                }
                case COMPLETE -> {
                    return setComplete(callback, data);
                }
                case PROLONG -> {
                    return prolongGoal(callback, chatId, data);
                }
                case DEL -> {
                    return deleteGoal(callback, data);
                }
            }
        }
        return List.of(NotImplMsg(chatId));
    }

    private List<BotApiMethod<?>> setDeadLine(CallbackQuery callback, String[] data) {
        GoalEntity goal = goalService.setDeadline(callback, data[1]);
        InlineKeyboardMarkup inlineButtonKeyboard = keyboardBuilder.createInlineButtonKeyboard(List.of(List.of(MENU)), goal.getId());

        String goalDto = goal.getStart().isAfter(LocalDateTime.now()) ?
                goalMapper.buildBeforeStartGoalDto(goal).toString() : goalMapper.buildActiveGoalDto(goal).toString();

        return List.of(EditMessageText.builder()
                .messageId(callback.getMessage().getMessageId())
                .chatId(callback.getMessage().getChatId())
                .text(goalDto)
                .parseMode("MarkdownV2")
                .replyMarkup(inlineButtonKeyboard)
                .build());
    }

    private List<BotApiMethod<?>> setDeadLineMonth(CallbackQuery callback, String[] data) {
        InlineKeyboardMarkup inlineKeyboardMarkup = goalService.setDeadlineMonth(callback, data[1]);


        return List.of(EditMessageReplyMarkup.builder()
                .messageId(callback.getMessage().getMessageId())
                .chatId(callback.getMessage().getChatId())
                .replyMarkup(inlineKeyboardMarkup)
                .build());
    }

    private List<BotApiMethod<?>> setStartDay(CallbackQuery callback, String[] data) {
        GoalEntity goal = goalService.setStartDay(callback, data[1]);
        int currentYear = goal.getStart().getYear();
        List<List<String>> yearButtons = keyboardBuilder.buildYearButtons(currentYear);

        InlineKeyboardMarkup inlineKeyboard = keyboardBuilder.createInlineStringKeyboard(yearButtons, DEADLINE);
        return List.of(EditMessageText.builder()
                .messageId(callback.getMessage().getMessageId())
                .chatId(callback.getMessage().getChatId())
                .text("Отлично! Когда будет дедлайн?")
                .replyMarkup(inlineKeyboard)
                .build());
    }

    private List<BotApiMethod<?>> setStartMonth(CallbackQuery callback, String[] data) {
        int daysInMonth = goalService.setStartMonth(callback, data[1]);
        List<List<String>> daysKeyboard = keyboardBuilder.buildStartDaysKeyboard(daysInMonth, data[1]);
        InlineKeyboardMarkup inlineStringKeyboard = keyboardBuilder.createInlineStringKeyboard(daysKeyboard, START);

        return List.of(EditMessageReplyMarkup.builder()
                .messageId(callback.getMessage().getMessageId())
                .chatId(callback.getMessage().getChatId())
                .replyMarkup(inlineStringKeyboard)
                .build());
    }

    private List<BotApiMethod<?>> deleteGoal(CallbackQuery callback, String[] data) {
        String msg = goalService.deleteGoal(Long.parseLong(data[0]));
        return List.of(EditMessageText.builder()
                .chatId(callback.getMessage().getChatId())
                .messageId(callback.getMessage().getMessageId())
                .text(msg)
                .build());
    }

    private List<BotApiMethod<?>> prolongGoal(CallbackQuery callback, Long chatId, String[] data) {
        Optional<GoalEntity> optionalGoal = goalService.prolongGoal(callback, Long.parseLong(data[0]));
        if (optionalGoal.isEmpty()) {
            return ErrorMsg(chatId, "Ошибка, ничего не найдено");
        }
        GoalEntity goal = optionalGoal.get();
        int currentYear = goal.getStart().isAfter(LocalDateTime.now()) ?
                goal.getStart().getYear() : LocalDateTime.now().getYear();
        List<List<String>> yearButtons = keyboardBuilder.buildYearButtons(currentYear);

        InlineKeyboardMarkup inlineKeyboard = keyboardBuilder.createInlineStringKeyboard(yearButtons, DEADLINE);
        return List.of(EditMessageReplyMarkup.builder()
                .messageId(callback.getMessage().getMessageId())
                .chatId(callback.getMessage().getChatId())
                .replyMarkup(inlineKeyboard)
                .build());
    }

    private List<BotApiMethod<?>> setComplete(CallbackQuery callback, String[] data) {
        String msg = goalService.setCompleteGoal(Long.parseLong(data[0]));
        return List.of(EditMessageText.builder()
                .chatId(callback.getMessage().getChatId())
                .messageId(callback.getMessage().getMessageId())
                .text(msg)
                .build());
    }

    private List<BotApiMethod<?>> getClosedMenu(CallbackQuery callback, String[] data) {
        Optional<GoalEntity> optionalGoal = goalService.findById(Long.parseLong(data[0]));
        if (optionalGoal.isEmpty()) {
            return DefaultAnswer.ErrorMsg(callback.getMessage().getChatId(), "Ошибка, не найдено");
        }
        GoalEntity goal = optionalGoal.get();
        String goalDto;
        if (goal.getStart().isAfter(LocalDateTime.now())) {
            goalDto = goalMapper.buildBeforeStartGoalDto(goal).toString();
        } else if (goal.getStart().isBefore(LocalDateTime.now()) && goal.getDeadLine().isAfter(LocalDateTime.now())) {
            goalDto = goalMapper.buildActiveGoalDto(goal).toString();
        } else if (goal.getDeadLine().isBefore(LocalDateTime.now()) && goal.getStatus().equals(Status.ACTIVE)) {
            goalDto = goalMapper.buildWaitingGoalDto(goal).toString();
        } else {
            goalDto = goalMapper.buildFinishedGoalDto(goal).toString();
        }
        return List.of(EditMessageText.builder()
                .chatId(callback.getMessage().getChatId())
                .messageId(callback.getMessage().getMessageId())
                .text(goalDto)
                .parseMode("MarkdownV2")
                .replyMarkup(keyboardBuilder.createInlineButtonKeyboard(List.of(List.of(MENU)), Long.parseLong(data[0])))
                .build());
    }

    private List<BotApiMethod<?>> getOpenedMenu(CallbackQuery callback, String[] data) {
        Optional<GoalEntity> optionalGoal = goalService.findById(Long.parseLong(data[0]));
        if (optionalGoal.isEmpty()) {
            return DefaultAnswer.ErrorMsg(callback.getMessage().getChatId(), "Ошибка, не найдено");
        }
        List<List<Buttons>> buttonMatrix = new ArrayList<>();
        GoalEntity goal = optionalGoal.get();
        if (goal.getDeadLine().isAfter(LocalDateTime.now()) && Status.ACTIVE.equals(goal.getStatus())) {
            buttonMatrix.addAll(List.of(List.of(COMPLETE), List.of(DEL), List.of(CLOSE_MENU)));
        } else if (goal.getDeadLine().isBefore(LocalDateTime.now()) && Status.ACTIVE.equals(goal.getStatus())) {
            buttonMatrix.addAll(List.of(List.of(COMPLETE), List.of(PROLONG), List.of(DEL), List.of(CLOSE_MENU)));
        } else {
            buttonMatrix.addAll(List.of(List.of(PROLONG), List.of(DEL), List.of(CLOSE_MENU)));
        }
        String goalDto;
        if (goal.getStart().isAfter(LocalDateTime.now())) {
            goalDto = goalMapper.buildFullBeforeStartDto(goal).toString();
        } else if (goal.getStart().isBefore(LocalDateTime.now()) && goal.getDeadLine().isAfter(LocalDateTime.now())) {
            goalDto = goalMapper.buildFullActiveGoalDto(goal).toString();
        } else if (goal.getDeadLine().isBefore(LocalDateTime.now()) && goal.getStatus().equals(Status.ACTIVE)) {
            goalDto = goalMapper.buildFullWaitGoalDto(goal).toString();
        } else {
            goalDto = goalMapper.buildFullFinishedGoalDto(goal).toString();
        }
        InlineKeyboardMarkup inlineButtonKeyboard = keyboardBuilder.createInlineButtonKeyboard(buttonMatrix, Long.parseLong(data[0]));
        return List.of(EditMessageText.builder()
                .chatId(callback.getMessage().getChatId())
                .messageId(callback.getMessage().getMessageId())
                .text(goalDto)
                .parseMode("MarkdownV2")
                .replyMarkup(inlineButtonKeyboard)
                .build());
    }

    private List<BotApiMethod<?>> setDeadLineYear(CallbackQuery callback, String[] data) {
        GoalEntity goal = goalService.setDeadlineYear(callback, data[1]);
        Month currentMonth = goal.getStart().isAfter(LocalDateTime.now()) ?
                goal.getStart().getMonth() : LocalDateTime.now().getMonth();
        List<List<Month>> mk = keyboardBuilder.buildMonthButtons(currentMonth);
        InlineKeyboardMarkup inlineKeyboard = keyboardBuilder.createInlineMonthKeyboard(mk, DEADLINE);

        return List.of(EditMessageReplyMarkup.builder()
                .messageId(callback.getMessage().getMessageId())
                .chatId(callback.getMessage().getChatId())
                .replyMarkup(inlineKeyboard)
                .build());
    }

    private List<BotApiMethod<?>> setStartYear(CallbackQuery callback, String[] data) {
        goalService.setStartYear(callback, data[1]);
        Month month = LocalDateTime.now().getMonth();
        List<List<Month>> mk = keyboardBuilder.buildMonthButtons(month);
        InlineKeyboardMarkup inlineKeyboard = keyboardBuilder.createInlineMonthKeyboard(mk, START);

        return List.of(EditMessageReplyMarkup.builder()
                .messageId(callback.getMessage().getMessageId())
                .chatId(callback.getMessage().getChatId())
                .replyMarkup(inlineKeyboard)
                .build());
    }

    private List<BotApiMethod<?>> setImportance(CallbackQuery callback) {
        goalService.importanceToYear(callback);
        int currentYear = LocalDateTime.now().getYear();
        List<List<String>> yearButtons = keyboardBuilder.buildYearButtons(currentYear);
        InlineKeyboardMarkup inlineKeyboard = keyboardBuilder.createInlineStringKeyboard(yearButtons, START);
        return List.of(EditMessageText.builder()
                .messageId(callback.getMessage().getMessageId())
                .chatId(callback.getMessage().getChatId())
                .text("Ок! Укажите дату старта")
                .replyMarkup(inlineKeyboard)
                .build());
    }

    private List<BotApiMethod<?>> createGoal(CallbackQuery callback, Long chatId) {
        Optional<String> error = goalService.createGoal(callback);
        if (error.isPresent()) {
            return ErrorMsg(chatId, error.get());
        }
        InlineKeyboardMarkup inlineKeyboard = keyboardBuilder.createInlineButtonKeyboard(List.of(
                List.of(VERY_IMPORTANCE),
                List.of(MIDDLE_IMPORTANCE),
                List.of(NO_IMPORTANCE)
        ));
        return List.of(EditMessageText.builder()
                .messageId(callback.getMessage().getMessageId())
                .chatId(callback.getMessage().getChatId())
                .text("Выберем приоритет")
                .replyMarkup(inlineKeyboard)
                .build());
    }

    private List<BotApiMethod<?>> replyToText(Message message) {
        InlineKeyboardMarkup inlineKeyboard = keyboardBuilder.createInlineButtonKeyboard(List.of(
                List.of(NEW_GOAL)
        ));
        return List.of(SendMessage.builder()
                .chatId(message.getChatId())
                .replyToMessageId(message.getMessageId())
                .text(Smile.random())
                .replyMarkup(inlineKeyboard)
                .build());
    }
}
