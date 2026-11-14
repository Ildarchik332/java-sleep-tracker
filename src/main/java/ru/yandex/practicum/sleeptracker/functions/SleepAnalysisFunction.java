package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.util.List;
import java.util.function.Function;

public interface SleepAnalysisFunction extends Function<List<SleepingSession>, SleepAnalysisResult<?>> {

}
