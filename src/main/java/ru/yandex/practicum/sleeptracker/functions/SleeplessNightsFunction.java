package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SleeplessNightsFunction implements SleepAnalysisFunction {

    private static final String NIGHT_COUNT = "Количество бессонных ночей";
    private static final LocalTime NIGHT_START = LocalTime.MIDNIGHT;
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>(NIGHT_COUNT, 0L);
        }

        SleepingSession firstSession = sessions.get(0);
        SleepingSession lastSession = sessions.get(sessions.size() - 1);

        LocalDate startDate = firstSession.getStartTime().toLocalDate();
        LocalDate potentialFirstNight;

        if (firstSession.getStartTime().toLocalTime().isBefore(LocalTime.NOON)) {
            potentialFirstNight = startDate.minusDays(1);
        } else {
            potentialFirstNight = startDate;
        }

        LocalDate endDate = lastSession.getEndTime().toLocalDate();

        long totalNights = Period.between(potentialFirstNight, endDate).getDays() + 1;


        if (totalNights <= 0) {
            return new SleepAnalysisResult<>(NIGHT_COUNT, 0L);
        }

        Set<LocalDate> sleptNights = sessions.stream()
                .filter(session -> overlapsWithNight(session.getStartTime(), session.getEndTime()))
                .map(session -> getNightDate(session.getStartTime()))
                .filter(nightDate -> !nightDate.isBefore(potentialFirstNight) && !nightDate.isAfter(endDate))
                .collect(Collectors.toSet());

        long sleeplessNights = totalNights - sleptNights.size();

        return new SleepAnalysisResult<>(NIGHT_COUNT, sleeplessNights);
    }

    /**
     * Проверяет, пересекается ли сессия сна с интервалом (00:00, 06:00).
     */
    private boolean overlapsWithNight(LocalDateTime start, LocalDateTime end) {
        LocalDate currentDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        return !currentDate.isAfter(endDate) &&
                isSessionOverlappingWithSpecificNight(start, end, currentDate) ||
                (!currentDate.equals(endDate) && isSessionOverlappingWithSpecificNight(start, end, endDate));
    }

    private boolean isSessionOverlappingWithSpecificNight(LocalDateTime sessionStart, LocalDateTime sessionEnd, LocalDate nightDate) {
        LocalDateTime nightStart = nightDate.atTime(NIGHT_START);
        LocalDateTime nightEnd = nightDate.atTime(NIGHT_END);

        return sessionStart.isBefore(nightEnd) && sessionEnd.isAfter(nightStart);
    }

    /**
     * Определяет дату ночи для сессии.
     * Если сессия началась до 00:00 (например, 23:00), то ночь относится к следующему дню.
     * Если сессия началась после 00:00 (например, 2:00), то ночь относится к текущему дню.
     */
    private LocalDate getNightDate(LocalDateTime start) {
        if (start.toLocalTime().isAfter(LocalTime.NOON)) {
            return start.toLocalDate().plusDays(1);
        } else {
            return start.toLocalDate();
        }
    }
}