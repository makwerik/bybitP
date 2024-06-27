import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeZone {
    public static void main(String[] args){
        String dateTimeString = "24_06_2024 15_30";
        System.out.println(converterDatetimeToUnix(dateTimeString));
    };
    public static String converterDatetimeToUnix(String dateTimeString){
        try {
            // Определяю формат входной строки
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy HH_mm");

            // Парсим строку
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

            // Преобразуем в UNIX
            return String.valueOf(dateTime.atZone(ZoneId.of("UTC")).toEpochSecond());
        }catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }

    };

    public static String incrementTimeBy0Minutes(String dateTimeString){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy HH_mm");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
            LocalDateTime incrementedDateTime = dateTime.plusMinutes(30);
            return incrementedDateTime.format(formatter);
        }catch (DateTimeParseException e ) {
            e.printStackTrace();
            return null;
        }

    }
}
