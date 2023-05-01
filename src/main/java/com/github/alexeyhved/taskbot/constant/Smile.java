package com.github.alexeyhved.taskbot.constant;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Smile {
    private static final List<String> questionEmojis = new ArrayList<>(List.of("🤔", "🤨", "🧐"));

    public static String random() {
        Collections.shuffle(questionEmojis);
        return questionEmojis.get(0);
    }
}
