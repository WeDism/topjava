package ru.javawebinar.topjava.web.converter.formatter_factory;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import ru.javawebinar.topjava.web.converter.CustomDateFormatter;
import ru.javawebinar.topjava.web.converter.CustomTimeFormatter;
import ru.javawebinar.topjava.web.converter.annotation.CustomDateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomDateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<CustomDateTimeFormat> {
    public static final CustomDateFormatter CUSTOM_DATE_FORMATTER = new CustomDateFormatter();
    public static final CustomTimeFormatter CUSTOM_TIME_FORMATTER = new CustomTimeFormatter();

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(List.of(LocalDate.class, LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation, fieldType);
    }

    private Formatter<?> getFormatter(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return switch (annotation.type()) {
            case DATE -> CUSTOM_DATE_FORMATTER;
            case TIME -> CUSTOM_TIME_FORMATTER;
        };
    }
}
