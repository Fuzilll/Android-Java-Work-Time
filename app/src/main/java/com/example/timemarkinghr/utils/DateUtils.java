package com.example.timemarkinghr.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    private static final DateTimeFormatter formatoBanco = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static final DateTimeFormatter formatoExibicao = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault());

    public static String formatarParaExibicao(LocalDateTime dataHora) {
        return dataHora.format(formatoExibicao);
    }

    public static LocalDateTime parseDoBanco(String dataHoraString) {
        return LocalDateTime.parse(dataHoraString, formatoBanco);
    }

    public static String formatarParaBanco(LocalDateTime dataHora) {
        return dataHora.format(formatoBanco);
    }
}
