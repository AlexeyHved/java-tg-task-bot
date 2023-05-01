package com.github.alexeyhved.taskbot.service;

import com.github.alexeyhved.taskbot.Status;
import com.github.alexeyhved.taskbot.constant.Buttons;
import com.github.alexeyhved.taskbot.entity.GoalEntity;
import com.github.alexeyhved.taskbot.entity.UserEntity;
import com.github.alexeyhved.taskbot.repo.GoalRepo;
import com.github.alexeyhved.taskbot.repo.TmpRepo;
import com.github.alexeyhved.taskbot.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import static com.github.alexeyhved.taskbot.Const.DEADLINE;

@Component
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepo goalRepo;
    private final UserRepo userRepo;
    private final KeyboardBuilder keyboardBuilder;

    public Optional<String> createGoal(CallbackQuery callback) {
        Optional<UserEntity> optionalUser = userRepo.findById(callback.getFrom().getId());
        if (optionalUser.isEmpty()) {
            return Optional.of("Ошибка, ваши данные не найдены, возможно стоит перезапустить бота");
        }
        GoalEntity goal = new GoalEntity();
        String title = callback.getMessage().getReplyToMessage().getText();
        goal.setCreatedOn(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)));
        goal.setTitle(title);
        goal.setOwner(optionalUser.get());
        goal.setStatus(Status.WAITING);
        TmpRepo.msgIdVsGoal.put(callback.getMessage().getMessageId(), goal);
        return Optional.empty();
    }

    public void importanceToYear(CallbackQuery callback) {
        GoalEntity goal = TmpRepo.msgIdVsGoal.get(callback.getMessage().getMessageId());
        double importance;
        switch (Buttons.valueOf(callback.getData())) {
            case VERY_IMPORTANCE -> importance = 2.0;
            case MIDDLE_IMPORTANCE -> importance = 1.5;
            default -> importance = 1.0;
        }
        goal.setImportance(importance);
        TmpRepo.msgIdVsGoal.put(callback.getMessage().getMessageId(), goal);
    }

    public void setStartYear(CallbackQuery callback, String data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(data).append(":");
        TmpRepo.msgIdVsStartDate.put(callback.getMessage().getMessageId(), stringBuilder);
    }

    public GoalEntity setDeadlineYear(CallbackQuery callback, String data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(data).append(":");
        TmpRepo.msgIdVsDeadline.put(callback.getMessage().getMessageId(), stringBuilder);
        GoalEntity goal = TmpRepo.msgIdVsGoal.get(callback.getMessage().getMessageId());
        if (goal == null) {
            goal = goalRepo.findByOwnerId(callback.getFrom().getId());
        }
        return goal;
    }

    public int setStartMonth(CallbackQuery callback, String data) {
        GregorianCalendar calendar = new GregorianCalendar();
        StringBuilder stringBuilder = TmpRepo.msgIdVsStartDate.get(callback.getMessage().getMessageId());

        switch (Month.valueOf(data)) {
            case JANUARY -> {
                stringBuilder.append("01").append(":");
                calendar.set(Calendar.MONTH, 0);
            }
            case FEBRUARY -> {
                stringBuilder.append("02").append(":");
                calendar.set(Calendar.MONTH, 1);
            }
            case MARCH -> {
                stringBuilder.append("03").append(":");
                calendar.set(Calendar.MONTH, 2);
            }
            case APRIL -> {
                stringBuilder.append("04").append(":");
                calendar.set(Calendar.MONTH, 3);
            }
            case MAY -> {
                stringBuilder.append("05").append(":");
                calendar.set(Calendar.MONTH, 4);
            }
            case JUNE -> {
                stringBuilder.append("06").append(":");
                calendar.set(Calendar.MONTH, 5);
            }
            case JULY -> {
                stringBuilder.append("07").append(":");
                calendar.set(Calendar.MONTH, 6);
            }
            case AUGUST -> {
                stringBuilder.append("08").append(":");
                calendar.set(Calendar.MONTH, 7);
            }
            case SEPTEMBER -> {
                stringBuilder.append("09").append(":");
                calendar.set(Calendar.MONTH, 8);
            }
            case OCTOBER -> {
                stringBuilder.append("10").append(":");
                calendar.set(Calendar.MONTH, 9);
            }
            case NOVEMBER -> {
                stringBuilder.append("11").append(":");
                calendar.set(Calendar.MONTH, 10);
            }
            case DECEMBER -> {
                stringBuilder.append("12").append(":");
                calendar.set(Calendar.MONTH, 11);
            }
        }
        TmpRepo.msgIdVsStartDate.put(callback.getMessage().getMessageId(), stringBuilder);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public InlineKeyboardMarkup setDeadlineMonth(CallbackQuery callback, String datum) {
        GregorianCalendar calendar = new GregorianCalendar();
        StringBuilder stringBuilder = TmpRepo.msgIdVsDeadline.get(callback.getMessage().getMessageId());

        switch (Month.valueOf(datum)) {
            case JANUARY -> {
                stringBuilder.append("01").append(":");
                calendar.set(Calendar.MONTH, 0);
            }
            case FEBRUARY -> {
                stringBuilder.append("02").append(":");
                calendar.set(Calendar.MONTH, 1);
            }
            case MARCH -> {
                stringBuilder.append("03").append(":");
                calendar.set(Calendar.MONTH, 2);
            }
            case APRIL -> {
                stringBuilder.append("04").append(":");
                calendar.set(Calendar.MONTH, 3);
            }
            case MAY -> {
                stringBuilder.append("05").append(":");
                calendar.set(Calendar.MONTH, 4);
            }
            case JUNE -> {
                stringBuilder.append("06").append(":");
                calendar.set(Calendar.MONTH, 5);
            }
            case JULY -> {
                stringBuilder.append("07").append(":");
                calendar.set(Calendar.MONTH, 6);
            }
            case AUGUST -> {
                stringBuilder.append("08").append(":");
                calendar.set(Calendar.MONTH, 7);
            }
            case SEPTEMBER -> {
                stringBuilder.append("09").append(":");
                calendar.set(Calendar.MONTH, 8);
            }
            case OCTOBER -> {
                stringBuilder.append("10").append(":");
                calendar.set(Calendar.MONTH, 9);
            }
            case NOVEMBER -> {
                stringBuilder.append("11").append(":");
                calendar.set(Calendar.MONTH, 10);
            }
            case DECEMBER -> {
                stringBuilder.append("12").append(":");
                calendar.set(Calendar.MONTH, 11);
            }
        }
        TmpRepo.msgIdVsDeadline.put(callback.getMessage().getMessageId(), stringBuilder);
        GoalEntity goal = TmpRepo.msgIdVsGoal.get(callback.getMessage().getMessageId());

        int actualDay = 1;
        if (Month.valueOf(datum).equals(LocalDateTime.now().getMonth())) {
            if (goal.getStart().isAfter(LocalDateTime.now())) {
                actualDay = goal.getStart().getDayOfMonth();
            } else {
                actualDay = LocalDateTime.now().getDayOfMonth();
            }
        }
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<List<String>> daysKeyboard = keyboardBuilder.buildDeadlineDaysKeyboard(actualMaximum, actualDay);

        return keyboardBuilder.createInlineStringKeyboard(daysKeyboard, DEADLINE);
    }

    public GoalEntity setStartDay(CallbackQuery callback, String data) {
        StringBuilder stringBuilder = TmpRepo.msgIdVsStartDate.get(callback.getMessage().getMessageId());
        GoalEntity goal = TmpRepo.msgIdVsGoal.get(callback.getMessage().getMessageId());

        stringBuilder.append(data);
        String[] split = stringBuilder.toString().split(":");
        LocalDate localDate = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        LocalDateTime startDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));

        goal.setStart(startDateTime);
        TmpRepo.msgIdVsGoal.put(callback.getMessage().getMessageId(), goal);
        TmpRepo.msgIdVsStartDate.clear();

        return goal;
    }

    public GoalEntity setDeadline(CallbackQuery callback, String datum) {

        StringBuilder deadlineSb = TmpRepo.msgIdVsDeadline.get(callback.getMessage().getMessageId());
        GoalEntity goal = TmpRepo.msgIdVsGoal.get(callback.getMessage().getMessageId());

        deadlineSb.append(datum);
        String[] split = deadlineSb.toString().split(":");
        LocalDate localDate = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        LocalDateTime deadLine = LocalDateTime.of(localDate, LocalTime.of(23, 59, 59));

        goal.setDeadLine(deadLine);
        goal.setStatus(Status.ACTIVE);
        goalRepo.save(goal);
        TmpRepo.msgIdVsGoal.clear();
        TmpRepo.msgIdVsDeadline.clear();

        return goal;
    }

    public String setCompleteGoal(long id) {
        Optional<GoalEntity> optionalGoal = goalRepo.findById(id);
        String msg;
        if (optionalGoal.isPresent()) {
            GoalEntity goal = optionalGoal.get();
            goal.setDeadLine(LocalDateTime.now());
            goal.setStatus(Status.FINISHED);
            goalRepo.save(goal);
            msg = "Поздравляем, цель достигнута";
        } else {
            msg = "Ошибка, ваши данные не найдены, возможно стоит перезапустить бота";
        }
        return msg;
    }

    public String deleteGoal(long id) {
        Optional<GoalEntity> optionalGoal = goalRepo.findById(id);
        String msg;
        if (optionalGoal.isPresent()) {
            goalRepo.deleteById(id);
            msg = "Удалено";
        } else {
            msg = "Ошибка, ваши данные не найдены, возможно стоит перезапустить бота";
        }
        return msg;
    }

    public Optional<GoalEntity> prolongGoal(CallbackQuery callback, long id) {
        Optional<GoalEntity> optionalGoal = goalRepo.findById(id);

        if (optionalGoal.isEmpty()) {
            return Optional.empty();
        }

        GoalEntity goal = optionalGoal.get();
        goal.setStatus(Status.ACTIVE);
        TmpRepo.msgIdVsGoal.put(callback.getMessage().getMessageId(), goal);
        return optionalGoal;
    }

    public Optional<GoalEntity> findById(long id) {
        return goalRepo.findById(id);
    }
}
