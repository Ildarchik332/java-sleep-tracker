package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.domain.Chronotype;
import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction {

    private static final String CHRONOTYPE = "Хронотип пользователя";
    private static final LocalTime OWL_SLEEP = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKE = LocalTime.of(9, 0);
    private static final LocalTime LARK_SLEEP = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKE = LocalTime.of(7, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>(CHRONOTYPE, Chronotype.PIGEON);
        }

        List<SleepingSession> nightSessions = sessions.stream()
                .filter(this::isNightSession)
                .toList();

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult<>(CHRONOTYPE, Chronotype.PIGEON);
        }

        Map<Chronotype, Long> typeCounts = nightSessions.stream()
                .map(this::determineType)
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        long owls = typeCounts.getOrDefault(Chronotype.OWL, 0L);
        long larks = typeCounts.getOrDefault(Chronotype.LARK, 0L);
        long pigeons = typeCounts.getOrDefault(Chronotype.PIGEON, 0L);

        long maxCount = Math.max(owls, Math.max(larks, pigeons));

        Chronotype resultType;
        if (owls == maxCount && larks != maxCount && pigeons != maxCount) {
            resultType = Chronotype.OWL;
        } else if (larks == maxCount && owls != maxCount && pigeons != maxCount) {
            resultType = Chronotype.LARK;
        } else {
            resultType = Chronotype.PIGEON;
        }

        return new SleepAnalysisResult<>(CHRONOTYPE, resultType);
    }

    private boolean isNightSession(SleepingSession session) {
        LocalTime startTime = session.getStartTime().toLocalTime();
        LocalTime endTime = session.getEndTime().toLocalTime();

        if (session.getStartTime().toLocalDate().isBefore(session.getEndTime().toLocalDate())) {
            return true;
        }

        return startTime.isBefore(NIGHT_END) && endTime.isAfter(LocalTime.MIDNIGHT);
    }

    private Chronotype determineType(SleepingSession session) {
        LocalTime sleepTime = session.getStartTime().toLocalTime();
        LocalTime wakeTime = session.getEndTime().toLocalTime();

        if (sleepTime.isAfter(OWL_SLEEP) && wakeTime.isAfter(OWL_WAKE)) {
            return Chronotype.OWL;
        } else if (sleepTime.isBefore(LARK_SLEEP) && wakeTime.isBefore(LARK_WAKE)) {
            return Chronotype.LARK;
        } else {
            return Chronotype.PIGEON;
        }
    }
}
