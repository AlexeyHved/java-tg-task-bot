package com.github.alexeyhved.taskbot.service;


import com.github.alexeyhved.taskbot.constant.Buttons;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


@Component
public class KeyboardBuilder {
    public List<List<String>> buildStartDaysKeyboard(int daysInMonth, String data) {
        List<List<String>> buttons = new ArrayList<>();
        int i = 1;
        if (LocalDateTime.now().getMonth().name().equals(data)) {
            i = LocalDateTime.now().getDayOfMonth();
        }
        while (i < daysInMonth) {
            List<String> buttonRow = new ArrayList<>();
            while (buttonRow.size() < 7) {
                if (i < 10) {
                    buttonRow.add("0" + i);
                } else {
                    buttonRow.add(String.valueOf(i));
                }
                i++;
                if (i == daysInMonth) {
                    buttonRow.add(String.valueOf(i));
                    buttons.add(buttonRow);
                    return buttons;
                }
            }
            buttons.add(buttonRow);
        }
        return buttons;
    }

    public List<List<String>> buildDeadlineDaysKeyboard(int daysInMonth, int actualDay) {
        List<List<String>> buttons = new ArrayList<>();
        int i = actualDay;
        while (i <= daysInMonth) {
            List<String> buttonRow = new ArrayList<>();
            while (buttonRow.size() < 7) {
                if (i < 10) {
                    buttonRow.add("0" + i);
                } else {
                    buttonRow.add(String.valueOf(i));
                }
                if (i == daysInMonth) {
                    buttons.add(buttonRow);
                    return buttons;
                }
                i++;
            }
            buttons.add(buttonRow);
        }
        return buttons;
    }

    public List<List<Month>> buildMonthButtons(Month currentMonth) {
        List<List<Month>> mk = new ArrayList<>();

        while (currentMonth.getValue() < 12) {
            List<Month> mRow = new ArrayList<>();
            while (mRow.size() < 3) {
                mRow.add(currentMonth);
                currentMonth = currentMonth.plus(1);
                if (currentMonth.getValue() == 12) {
                    mRow.add(currentMonth);
                    mk.add(mRow);
                    return mk;
                }
            }
            mk.add(mRow);
        }
        return mk;
    }

    public List<List<String>> buildYearButtons(int currentYear) {
        List<List<String>> kb = new ArrayList<>();
        List<String> rowKb = new ArrayList<>();
        for (int year = currentYear; year < currentYear + 2; year++) {
            rowKb.add(String.valueOf(year));
        }
        kb.add(rowKb);
        return kb;
    }
    public InlineKeyboardMarkup createInlineButtonKeyboard(List<List<Buttons>> buttonMatrix) {
        return InlineKeyboardMarkup.builder()
                .keyboard(createRowButtons(buttonMatrix))
                .build();
    }

    public InlineKeyboardMarkup createInlineButtonKeyboard(List<List<Buttons>> buttonMatrix, Long id) {
        return InlineKeyboardMarkup.builder()
                .keyboard(createRowButtons(buttonMatrix, id))
                .build();
    }

    public InlineKeyboardMarkup createInlineMonthKeyboard(List<List<Month>> buttonMatrix) {
        return InlineKeyboardMarkup.builder()
                .keyboard(createMonthButtons(buttonMatrix))
                .build();
    }

    public InlineKeyboardMarkup createInlineMonthKeyboard(List<List<Month>> buttonMatrix, String data) {
        return InlineKeyboardMarkup.builder()
                .keyboard(createMonthButtons(buttonMatrix, data))
                .build();
    }

    public InlineKeyboardMarkup createInlineStringKeyboard(List<List<String>> buttonMatrix) {
        return InlineKeyboardMarkup.builder()
                .keyboard(createRowStringButtons(buttonMatrix))
                .build();
    }
    public InlineKeyboardMarkup createInlineStringKeyboard(List<List<String>> buttonMatrix, String data) {
        return InlineKeyboardMarkup.builder()
                .keyboard(createRowStringButtons(buttonMatrix, data))
                .build();
    }

    public ReplyKeyboardMarkup createReplyKeyboard(String... titles) {
        return ReplyKeyboardMarkup.builder()
                .oneTimeKeyboard(true)
                .keyboard(createReplyButtons(titles))
                .build();
    }

    private List<KeyboardRow> createReplyButtons(String... titles) {
        List<KeyboardRow> buttons = new ArrayList<>();
        for (String title : titles) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(KeyboardButton.builder()
                    .text(title)
                    .build());
            buttons.add(keyboardRow);
        }
        return buttons;
    }

    private List<List<InlineKeyboardButton>> createRowButtons(List<List<Buttons>> buttonMatrix) {
        List<List<InlineKeyboardButton>> listRowButtons = new ArrayList<>();

        for (List<Buttons> buttonsRow : buttonMatrix) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            for (Buttons button : buttonsRow) {
                InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder()
                        .text(button.title)
                        .callbackData(button.name())
                        .build();
                rowButtons.add(keyboardButton);
            }
            listRowButtons.add(rowButtons);
        }

        return listRowButtons;
    }

    private List<List<InlineKeyboardButton>> createRowButtons(List<List<Buttons>> buttonMatrix, Long id) {
        List<List<InlineKeyboardButton>> listRowButtons = new ArrayList<>();

        for (List<Buttons> buttonsRow : buttonMatrix) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            for (Buttons button : buttonsRow) {
                InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder()
                        .text(button.title)
                        .callbackData(id + ":" + button.name())
                        .build();
                rowButtons.add(keyboardButton);
            }
            listRowButtons.add(rowButtons);
        }

        return listRowButtons;
    }

    private List<List<InlineKeyboardButton>> createMonthButtons(List<List<Month>> buttonMatrix) {
        List<List<InlineKeyboardButton>> listRowButtons = new ArrayList<>();

        for (List<Month> buttonsRow : buttonMatrix) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            for (Month button : buttonsRow) {
                InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder()
                        .text(button.name())
                        .callbackData(button.name())
                        .build();
                rowButtons.add(keyboardButton);
            }
            listRowButtons.add(rowButtons);
        }

        return listRowButtons;
    }

    private List<List<InlineKeyboardButton>> createMonthButtons(List<List<Month>> buttonMatrix, String data) {
        List<List<InlineKeyboardButton>> listRowButtons = new ArrayList<>();

        for (List<Month> buttonsRow : buttonMatrix) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            for (Month button : buttonsRow) {
                InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder()
                        .text(button.name())
                        .callbackData(data + ":" + button.name())
                        .build();
                rowButtons.add(keyboardButton);
            }
            listRowButtons.add(rowButtons);
        }

        return listRowButtons;
    }

    private List<List<InlineKeyboardButton>> createRowStringButtons(List<List<String>> buttonMatrix) {
        List<List<InlineKeyboardButton>> listRowButtons = new ArrayList<>();

        for (List<String> buttonsRow : buttonMatrix) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            for (String button : buttonsRow) {
                InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder()
                        .text(button)
                        .callbackData(button)
                        .build();
                rowButtons.add(keyboardButton);
            }
            listRowButtons.add(rowButtons);
        }

        return listRowButtons;
    }

    private List<List<InlineKeyboardButton>> createRowStringButtons(List<List<String>> buttonMatrix, String data) {
        List<List<InlineKeyboardButton>> listRowButtons = new ArrayList<>();

        for (List<String> buttonsRow : buttonMatrix) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            for (String button : buttonsRow) {
                InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder()
                        .text(button)
                        .callbackData(data + ":" + button)
                        .build();
                rowButtons.add(keyboardButton);
            }
            listRowButtons.add(rowButtons);
        }

        return listRowButtons;
    }
}
