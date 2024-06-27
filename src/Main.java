import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.StringJoiner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    static String url = "https://api2-1.bybit.com/contract/v5/public/instrument/kline/market";
    static String symbol = "1000BEERUSDT";

    static String timeStart = "19_06_2024 00_00";
    static String timeOut = "26_06_2024 00_00";


    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy HH_mm");
        LocalDateTime start = LocalDateTime.parse(timeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(timeOut, formatter);

        //Запускаем цикл пока начальное время меньше или равно окончанию

        while (start.isBefore(end) || start.isEqual(end)){
            String from = TimeZone.converterDatetimeToUnix(start.format(formatter));
            String to = TimeZone.converterDatetimeToUnix(start.plusMinutes(30).format(formatter));
            Map<String, String> params = Map.of(
                    "contract_type", "2",
                    "symbol", symbol,
                    "resolution", "1",
                    "from", from,
                    "to", to,
                    "_sp_category", "fbu",
                    "_sp_response_format", "portugal",
                    "showOrigin", "true"
            );

            // Строю url с параметрами
            String urlWithParams = buildUrlWithParams(url, params);

            connectBybit(urlWithParams);
            // Увлеичиваем время на 30 минут
            start = start.plusMinutes(30);
        }


    };
    public static void connectBybit(String url){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("sec-ch-ua", "\"Chromium\";v=\"124\", \"YaBrowser\";v=\"24.6\", \"Not-A.Brand\";v=\"99\", \"Yowser\";v=\"2.5\"")
                    .header("traceparent", "00-41a108ca8a374c7564415823f2bd3a53-e13417f50988e421-01")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 YaBrowser/24.6.0.0 Safari/537.36")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Referer", "https://www.bybit.com/")
                    .header("platform", "pc")
                    .header("sec-ch-ua-platform", "\"Windows\"")                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            writeToFile(responseBody, "output.txt");
            System.out.println(responseBody);
        }catch (Exception e){
            e.printStackTrace();
            ;
        }
    }

    public static String buildUrlWithParams(String url, Map<String, String> params){
        //Собираем строку используя разделитель
        StringJoiner joiner = new StringJoiner("&");

        // Проходим по каждому элементу карты params с использованием метода forEach
        params.forEach((key, value) -> {
            try {
                // Кодируем ключ параметра в формат URL

                String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
                // Кодируем значение в форат URL
                String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
                // Делаем формат ключ=значение
                joiner.add(encodedKey + "=" + encodedValue);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        // Возвращаю url с параметрами в виде строк
        return url + "?" + joiner.toString();
    };

    public static void  writeToFile(String data, String fileName){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))){
            writer.write(data);
            writer.newLine();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}
