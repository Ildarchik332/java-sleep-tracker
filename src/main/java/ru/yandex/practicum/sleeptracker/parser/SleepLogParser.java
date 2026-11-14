package ru.yandex.practicum.sleeptracker.parser;

import ru.yandex.practicum.sleeptracker.domain.SleepQuality;
import ru.yandex.practicum.sleeptracker.domain.SleepingSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class SleepLogParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static List<SleepingSession> parse(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .filter(line -> !line.isBlank())
                    .map(SleepLogParser::parseLine)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл лога сна: " + path.toAbsolutePath(), e);
        }
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";");

        LocalDateTime start = LocalDateTime.parse(parts[0].trim(), FORMATTER);
        LocalDateTime end = LocalDateTime.parse(parts[1].trim(), FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());

        return new SleepingSession(start, end, quality);
    }
}
