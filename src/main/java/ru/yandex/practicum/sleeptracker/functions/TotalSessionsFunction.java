package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;
import ru.yandex.practicum.sleeptracker.functions.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.functions.SleepAnalysisResult;

import java.util.List;

public class TotalSessionsFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        int size = sessions.size();
        return new SleepAnalysisResult<>("Общее количество сессий сна", size);
    }
}
