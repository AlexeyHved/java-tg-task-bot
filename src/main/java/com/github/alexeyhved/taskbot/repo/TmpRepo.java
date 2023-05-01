package com.github.alexeyhved.taskbot.repo;

import com.github.alexeyhved.taskbot.entity.GoalEntity;

import java.util.HashMap;
import java.util.Map;

public class TmpRepo {
    public static final Map<Integer, GoalEntity> msgIdVsGoal = new HashMap<>();
    public static final Map<Integer, StringBuilder> msgIdVsStartDate = new HashMap<>();
    public static final Map<Integer, StringBuilder> msgIdVsDeadline = new HashMap<>();
}
