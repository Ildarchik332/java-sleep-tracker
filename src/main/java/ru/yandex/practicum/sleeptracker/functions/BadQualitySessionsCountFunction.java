package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.domain.SleepQuality;
import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.util.List;

public class BadQualitySessionsCountFunction implements SleepAnalysisFunction {

    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        long badCount = sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult<>("Количество сессий с плохим качеством сна", badCount);
    }
}
