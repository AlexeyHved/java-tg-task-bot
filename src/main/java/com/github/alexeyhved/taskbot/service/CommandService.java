package com.github.alexeyhved.taskbot.service;

import com.github.alexeyhved.taskbot.dto.*;
import com.github.alexeyhved.taskbot.entity.GoalEntity;
import com.github.alexeyhved.taskbot.entity.UserEntity;
import com.github.alexeyhved.taskbot.repo.GoalRepo;
import com.github.alexeyhved.taskbot.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.github.alexeyhved.taskbot.DefaultAnswer.NotImplMsg;
import static com.github.alexeyhved.taskbot.constant.Buttons.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommandService {
    private final UserRepo userRepo;
    private final GoalRepo goalRepo;
    private final KeyboardBuilder keyboardBuilder;
    private final GoalMapper goalMapper;
    private static final String START = "/start";
    private static final String ACTUAL = "/actual";
    private static final String CURRENT = "/current";
    private static final String PLANNED = "/planned";
    private static final String WAITING = "/waiting";
    private static final String FINISHED = "/finished";
    private static final String STAT = "/stat";
    private static final String DEL_ME = "/delme";


    public List<BotApiMethod<?>> handle(MessageEntity entity, Message msg) {
        return switch (entity.getText()) {
            case START -> start(msg);
            case ACTUAL -> findActualGoals(msg);
            case CURRENT -> findCurrentGoals(msg);
            case PLANNED -> findPlannedGoals(msg);
            case WAITING -> findWaitingGoals(msg);
            case FINISHED -> findFinishedGoals(msg);
            case STAT -> getStat(msg);
            case DEL_ME -> delUserAndData(msg);
            default -> List.of(NotImplMsg(msg.getChatId()));
        };
    }

    private List<BotApiMethod<?>> getStat(Message msg) {
        Long ownerId = msg.getFrom().getId();
        int current = goalRepo.countCurrent(ownerId);
        int planned = goalRepo.countPlanned(ownerId);
        int waiting = goalRepo.countWaiting(ownerId);
        int finished = goalRepo.countFinished(ownerId);
        int all = current + planned + waiting + finished;

        ReportDto reportDto = new ReportDto(current, planned, waiting, finished, all);
        return List.of(SendMessage.builder()
                .chatId(msg.getChatId())
                .text(reportDto.toString())
                .parseMode("MarkdownV2")
                .build());
    }

    private List<BotApiMethod<?>> findWaitingGoals(Message msg) {
        List<BotApiMethod<?>> goalsList = new ArrayList<>();
        List<GoalEntity> waiting = goalRepo.findWaiting(msg.getFrom().getId());

        waiting.stream()
                .map(goalMapper::buildWaitingGoalDto)
                .map(g -> SendMessage.builder()
                        .chatId(msg.getChatId())
                        .parseMode("MarkdownV2")
                        .text(g.toString())
                        .replyMarkup(keyboardBuilder.createInlineButtonKeyboard(List.of(List.of(MENU)), g.getId()))
                        .build())
                .forEach(goalsList::add);
        return goalsList;
    }

    private List<BotApiMethod<?>> findFinishedGoals(Message msg) {
        List<BotApiMethod<?>> goalsList = new ArrayList<>();
        List<GoalEntity> finished = goalRepo.findFinished(msg.getFrom().getId());

        finished.stream()
                .map(goalMapper::buildFinishedGoalDto)
                .map(g -> SendMessage.builder()
                        .chatId(msg.getChatId())
                        .parseMode("MarkdownV2")
                        .text(g.toString())
                        .replyMarkup(keyboardBuilder.createInlineButtonKeyboard(List.of(List.of(MENU)), g.getId()))
                        .build())
                .forEach(goalsList::add);
        return goalsList;
    }

    private List<BotApiMethod<?>> delUserAndData(Message msg) {
        userRepo.deleteById(msg.getFrom().getId());
        return List.of(SendMessage.builder()
                .chatId(msg.getChatId())
                .text("Все ваши данные удалены")
                .build());
    }

    private List<BotApiMethod<?>> findPlannedGoals(Message msg) {
        List<BotApiMethod<?>> goalsList = new ArrayList<>();

        List<GoalEntity> beforeStart = goalRepo.findBeforeStart(msg.getFrom().getId());
        beforeStart.stream()
                .map(goalMapper::buildBeforeStartGoalDto)
                .sorted(Comparator.comparingDouble(BeforeStartGoalDto::getPriority).reversed())
                .map(g -> SendMessage.builder()
                        .chatId(msg.getChatId())
                        .parseMode("MarkdownV2")
                        .text(g.toString())
                        .replyMarkup(keyboardBuilder.createInlineButtonKeyboard(List.of(List.of(MENU)), g.getId()))
                        .build())
                .forEach(goalsList::add);

        return goalsList;
    }

    private List<BotApiMethod<?>> findCurrentGoals(Message msg) {
        List<BotApiMethod<?>> goalsList = new ArrayList<>();

        List<GoalEntity> goals = goalRepo.findCurrent(msg.getFrom().getId());
        goals.stream()
                .map(goalMapper::buildActiveGoalDto)
                .sorted(Comparator.comparingDouble(ActiveGoalDto::getPriority).reversed())
                .map(g -> SendMessage.builder()
                        .chatId(msg.getChatId())
                        .parseMode("MarkdownV2")
                        .text(g.toString())
                        .replyMarkup(keyboardBuilder.createInlineButtonKeyboard(List.of(List.of(MENU)), g.getId()))
                        .build())
                .forEach(goalsList::add);

        return goalsList;
    }

    private List<BotApiMethod<?>> findActualGoals(Message msg) {
        List<BotApiMethod<?>> goalsList = new ArrayList<>();
        goalsList.addAll(findCurrentGoals(msg));
        goalsList.addAll(findPlannedGoals(msg));
        goalsList.addAll(findWaitingGoals(msg));
        return goalsList;
    }


    private List<BotApiMethod<?>> start(Message msg) {
        Long userId = msg.getFrom().getId();
        String userName = msg.getFrom().getFirstName();
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        UserEntity user;

        if (optionalUser.isEmpty()) {
            user = new UserEntity();
            user.setId(userId);
            user.setName(userName);
        } else {
            user = optionalUser.get();
            user.setName(userName);
        }

        userRepo.save(user);
        return List.of(SendMessage.builder()
                .chatId(msg.getChatId())
                .text("Привет " + user.getName() + ", чтобы начать отправь описание цели одним сообщением.")
                .build());
    }
}
