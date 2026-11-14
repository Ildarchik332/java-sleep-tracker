package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.util.List;

public class MinDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sleepingSessions) {
        long minDuration = sleepingSessions.stream().mapToLong(SleepingSession::getDurationInMinutes)
                .min()
                .orElse(0);
        return new SleepAnalysisResult<>("Минимальная продолжительность сессии (мин)", minDuration);
    }
}
