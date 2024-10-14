package org.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class Main {
    private static final String ACCESS_KEY = "2e3ebcb5-d079-4553-8cf3-40ac28d9cc05";
    private static final double LATITUDE = 55.75;
    private static final double LONGITUDE = 37.62;

    private static final int LIMIT = 10;

    public static void main(String[] args) {
        // Создаем экземпляр HttpClient
        HttpClient client = HttpClient.newHttpClient();
        // Создаем URI для отправки GET запроса
        URI uri = URI.create("https://api.weather.yandex.ru/v2/forecast?lat=" + LATITUDE + "&lon=" + LONGITUDE + "&limit=" + LIMIT);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("X-Yandex-Weather-Key", ACCESS_KEY)
                .build();
        // Отправляем GET запрос и получаем ответ
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            // Выводим весь ответ на экран
            String jsonResponse = response.body();
            System.out.println("Получены все данные от сервиса:");
            System.out.println(jsonResponse);

            // Получаем температуру
            JSONObject jsonObject = new JSONObject(jsonResponse);
            double factTemp = jsonObject.getJSONObject("fact").getDouble("temp");
            System.out.println("Отдельно вывод температуры: " + factTemp + "°C");

            // Получаем среднюю температуру за период
            JSONArray forecasts = jsonObject.getJSONArray("forecasts");
            double tempSum = 0.0;

            for (int i = 0; i < forecasts.length(); i++) {
                JSONObject forecast = forecasts.getJSONObject(i);
                tempSum += forecast.getJSONObject("parts").getJSONObject("day").getDouble("temp_avg");
            }

            double avgTemp = tempSum / forecasts.length();
            System.out.println("Вычисление средней температуры: " + avgTemp + "°C");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
