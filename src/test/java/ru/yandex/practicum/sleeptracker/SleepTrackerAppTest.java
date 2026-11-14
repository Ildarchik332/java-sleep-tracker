package ru.yandex.practicum.sleeptracker;


import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.domain.Chronotype;
import ru.yandex.practicum.sleeptracker.domain.SleepQuality;
import ru.yandex.practicum.sleeptracker.domain.SleepingSession;
import ru.yandex.practicum.sleeptracker.functions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepTrackerAppTest {
    @Test
    void testMinDuration() {
        MinDurationFunction function = new MinDurationFunction();
        SleepAnalysisResult<?> result = function.apply(createTestSessions());
        assertEquals(360L, result.getValue());
    }

    @Test
    void testMinDuration_EmptyList() {
        MinDurationFunction function = new MinDurationFunction();
        SleepAnalysisResult<?> result = function.apply(new ArrayList<>());
        assertEquals(0L, result.getValue());
    }

    @Test
    void testMaxDuration() {
        MaxDurationFunction function = new MaxDurationFunction();
        SleepAnalysisResult<?> result = function.apply(createTestSessions());
        assertEquals(600L, result.getValue());
    }

    @Test
    void testMaxDuration_EmptyList() {
        MaxDurationFunction function = new MaxDurationFunction();
        SleepAnalysisResult<?> result = function.apply(new ArrayList<>());
        assertEquals(0L, result.getValue());
    }

    @Test
    void testAverageDuration() {
        AverageDurationFunction function = new AverageDurationFunction();
        SleepAnalysisResult<?> result = function.apply(createTestSessions());
        assertEquals(480.0, result.getValue());
    }

    @Test
    void testAverageDuration_EmptyList() {
        AverageDurationFunction function = new AverageDurationFunction();
        SleepAnalysisResult<?> result = function.apply(new ArrayList<>());
        assertEquals(0.0, result.getValue());
    }

    @Test
    void testBadQualitySessionsCount() {
        BadQualitySessionsCountFunction function = new BadQualitySessionsCountFunction();
        SleepAnalysisResult<?> result = function.apply(createTestSessions());
        assertEquals(1L, result.getValue());
    }

    @Test
    void testBadQualitySessionsCount_NoBadSessions() {

        List<SleepingSession> goodSessions = List.of(
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), SleepQuality.NORMAL)
        );
        BadQualitySessionsCountFunction function = new BadQualitySessionsCountFunction();
        SleepAnalysisResult<?> result = function.apply(goodSessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    void testSleeplessNights_AllNightsSlept() {
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 7, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 0),
                        LocalDateTime.of(2025, 10, 4, 7, 0), SleepQuality.GOOD)
        );
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals(1L, result.getValue());
    }

    @Test
    void testSleeplessNights_NoSessions() {
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult<?> result = function.apply(new ArrayList<>());
        assertEquals(0L, result.getValue());
    }

    @Test
    void testSleeplessNights_DaySessionsOnly() {
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 14, 0),
                        LocalDateTime.of(2025, 10, 1, 16, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 14, 0),
                        LocalDateTime.of(2025, 10, 2, 16, 0), SleepQuality.NORMAL)
        );
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals(2L, result.getValue());
    }

    @Test
    void testSleeplessNights_EarlyMorningSession() {
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 2, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0), SleepQuality.GOOD)
        );
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals(1L, result.getValue());
    }

    private List<SleepingSession> createTestSessions() {
        return List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 5, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 21, 0),
                        LocalDateTime.of(2025, 10, 4, 7, 0), SleepQuality.BAD)
        );
    }

    @Test
    void testChronotype_Owl() {
        ChronotypeFunction function = new ChronotypeFunction();
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 45),
                        LocalDateTime.of(2025, 10, 3, 10, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 22, 30),
                        LocalDateTime.of(2025, 10, 4, 8, 0), SleepQuality.GOOD)
        );
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals(Chronotype.OWL, result.getValue());
    }

    @Test
    void testChronotype_Lark() {
        ChronotypeFunction function = new ChronotypeFunction();
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 21, 30),
                        LocalDateTime.of(2025, 10, 2, 6, 30), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 21, 45),
                        LocalDateTime.of(2025, 10, 3, 6, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 30),
                        LocalDateTime.of(2025, 10, 4, 9, 30), SleepQuality.GOOD)
        );
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals(Chronotype.LARK, result.getValue());
    }

    @Test
    void testChronotype_Tie() {
        ChronotypeFunction function = new ChronotypeFunction();
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 21, 30),
                        LocalDateTime.of(2025, 10, 3, 6, 30), SleepQuality.GOOD)
        );
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void testChronotype_DaySleepOnly() {
        ChronotypeFunction function = new ChronotypeFunction();
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 14, 0),
                        LocalDateTime.of(2025, 10, 1, 16, 0), SleepQuality.GOOD)
        );
        SleepAnalysisResult<?> result = function.apply(sessions);
        assertEquals(Chronotype.PIGEON, result.getValue());
    }
}