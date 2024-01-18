package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.analysis.FlightAnalyzer;
import org.example.util.FileReader;

public class AppRunner {

    /**
     * метод, запускающий анализ данных о полетах
     *
     * @param args аргументы командной строки, ожидается, что первый аргумент - путь к json файлу
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java -cp <classpath> org.example.AppRunner <path_to_json_file>");
            return;
        }

        try {
            String filePath = args[0];
            System.out.println("Reading file: " + filePath);
            System.out.println("Current directory: " + System.getProperty("user.dir"));
            String jsonContent = FileReader.readFile(filePath);

            JsonArray ticketsArray = parseJson(jsonContent);

            FlightAnalyzer flightAnalyzer = new FlightAnalyzer(ticketsArray);

            flightAnalyzer.runAnalysis();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * преобразует json строку в массив билетов
     *
     * @param jsonContent json строка с данными о билетах
     * @return массив билетов
     */
    private static JsonArray parseJson(String jsonContent) {
        JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();
        return jsonObject.getAsJsonArray("tickets");
    }
}
