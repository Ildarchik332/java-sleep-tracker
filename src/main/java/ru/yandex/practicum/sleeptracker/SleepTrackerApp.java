package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.domain.SleepingSession;
import ru.yandex.practicum.sleeptracker.functions.*;
import ru.yandex.practicum.sleeptracker.parser.SleepLogParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SleepTrackerApp {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Пожалуйста, укажите путь к файлу лога в качестве аргумента командной строки.");
            return;
        }

        Path filePath = Paths.get(args[0]);
        List<SleepingSession> sessions;

        try {
            System.out.println("Загрузка данных из файла: " + filePath.toAbsolutePath());
            sessions = SleepLogParser.parse(filePath);
            System.out.println("Успешно загружено сессий: " + sessions.size());
        } catch (RuntimeException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return;
        }

        List<SleepAnalysisFunction> functions = new ArrayList<>();

        functions.add(new TotalSessionsFunction());
        functions.add(new MinDurationFunction());
        functions.add(new MaxDurationFunction());
        functions.add(new AverageDurationFunction());
        functions.add(new BadQualitySessionsCountFunction());
        functions.add(new SleeplessNightsFunction());
        functions.add(new ChronotypeFunction());


        functions.stream()
                .map(function -> function.apply(sessions))
                .forEach(System.out::println);
    }
}