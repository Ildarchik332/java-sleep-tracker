package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.util.List;

public class AverageDurationFunction implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        double avgDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .average()
                .orElse(0.0);

        double roundedAvg = Math.round(avgDuration * 100.0) / 100.0;

        return new SleepAnalysisResult<>("Средняя продолжительность сессии (мин)", roundedAvg);
    }
}
