package org.example.analysis;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.example.flight.Flight;

import java.util.*;

/**
 * класс для анаиза данных об авиабилетах/рейсах
 */
public class FlightAnalyzer {

    private final List<Flight> flights;

    /**
     * конструктор класса
     *
     * @param ticketsArray - массив билетов на рейсы в формате json
     */
    public FlightAnalyzer(JsonArray ticketsArray) {
        this.flights = parseFlights(ticketsArray);
    }

    /**
     * анализирует данные о рейсах и выводит результаты
     */
    public void runAnalysis() {
        printMinFlightTimeBetweenCities();
        printPriceDifference();
    }

    private List<Flight> parseFlights(JsonArray ticketsArray) {
        List<Flight> flights = new ArrayList<>();

        Gson gson = new Gson();
        for (int i = 0; i < ticketsArray.size(); i++) {
            Flight flight = gson.fromJson(ticketsArray.get(i), Flight.class);
            flights.add(flight);
        }

        return flights;
    }

    /**
     * выводит минимальное время полета для каждой авиакомпании по маршруту из VVO в TLV
     * если данные не были получены, то выводит сообщение "No data"
     */
    private void printMinFlightTimeBetweenCities() {
        Map<String, String> minFlightTimes = calculateMinFlightTimeBetweenCities();

        System.out.println("Min flight time for each air company for VVO to TLV route:");

        if (minFlightTimes.isEmpty()) {
            System.out.println("No data");
        } else {
            for (Map.Entry<String, String> entry : minFlightTimes.entrySet()) {
                String carrier = entry.getKey();
                String flightTime = entry.getValue();
                System.out.println(carrier + ": " + flightTime);
            }
        }
    }

    /**
     * рассчитывает минимальное время полета для каждой авиакомпании
     * на маршруте из VVO в TLV
     *
     * @return map, ключ - название авиакомпании,
     * значение - минимальное время полета в формате: часы минуты
     */
    private Map<String, String> calculateMinFlightTimeBetweenCities() {
        Map<String, Integer> minFlightTimes = new HashMap<>();

        for (Flight flight : flights) {
            // проверка рейса на соответствие маршруту
            if ("VVO".equals(flight.getOrigin()) && "TLV".equals(flight.getDestination())) {
                String carrier = flight.getCarrier();
                int flightTimeInMinutes = calculateFlightTimeInMinutes(flight);

                if (!minFlightTimes.containsKey(carrier) ||
                        flightTimeInMinutes < minFlightTimes.get(carrier)) {
                    minFlightTimes.put(carrier, flightTimeInMinutes);
                }
            }
        }

        // перевод времени полета обратно в строку для вывода
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : minFlightTimes.entrySet()) {
            result.put(entry.getKey(), convertMinutesToHoursAndMinutes(entry.getValue()));
        }

        return result;
    }

    private void printPriceDifference() {
        double priceDifference = calculatePriceDifference();
        System.out.printf("Difference between the average price and the median for the flight " +
                "between Vladivostok and Tel Aviv: %.2f rubles%n", priceDifference);
    }

    private String convertMinutesToHoursAndMinutes(int minutes) {
        int flightHours = minutes / 60;
        int flightMinutes = minutes % 60;

        return String.format("%d hours %d minutes", flightHours, flightMinutes);
    }

    private int calculateFlightTimeInMinutes(Flight flight) {
        String departureTime = flight.getDepartureTime();
        String arrivalTime = flight.getArrivalTime();

        int departureHour = Integer.parseInt(departureTime.split(":")[0]);
        int departureMinute = Integer.parseInt(departureTime.split(":")[1]);

        int arrivalHour = Integer.parseInt(arrivalTime.split(":")[0]);
        int arrivalMinute = Integer.parseInt(arrivalTime.split(":")[1]);

        return (arrivalHour * 60 + arrivalMinute) - (departureHour * 60 + departureMinute);
    }

    private double calculateAverage(List<Integer> prices) {
        if (prices.isEmpty()) {
            return 0.0;
        }

        int sum = 0;
        for (int price : prices) {
            sum += price;
        }

        return (double) sum / prices.size();
    }


    private double calculateMedian(List<Integer> prices) {
        Collections.sort(prices);

        int size = prices.size();
        if (size % 2 == 0) {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            return prices.get(size / 2);
        }
    }

    private double calculatePriceDifference() {
        List<Integer> prices = new ArrayList<>();
        for (Flight flight : flights) {
            if ("VVO".equals(flight.getOrigin()) && "TLV".equals(flight.getDestination())) {
                prices.add(flight.getPrice());
            }
        }

        if (prices.isEmpty()) {
            return 0.0;  // возвращаем 0, если нет данных для расчета
        }

        double averagePrice = calculateAverage(prices);
        double medianPrice = calculateMedian(prices);

        return averagePrice - medianPrice;
    }
}
