package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.analysis.FlightAnalyzer;
import org.example.flight.Flight;
import org.example.util.FileReader;


/**
 * класс, запускающий приложение
 */
public class AppRunner {

    /**
     * точка входа в приложение, принимает аргумент командной строки - путь к json-файлу с данными
     *
     * @param args аргумент командной строки, ожидается один аргумент - путь к json-файлу
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

            Gson gson = new Gson();
            Flight[] flights = gson.fromJson(ticketsArray, Flight[].class);

            FlightAnalyzer flightAnalyzer = new FlightAnalyzer(ticketsArray);

            flightAnalyzer.runAnalysis();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * парсит json-строку и возвращает массив билетов
     *
     * @param jsonContent json-строка с данными о рейсах
     * @return массив билетов на рейсы
     */
    private static JsonArray parseJson(String jsonContent) {
        JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();
        return jsonObject.getAsJsonArray("tickets");
    }
}
