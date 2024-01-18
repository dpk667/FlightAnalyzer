package org.example.analysis;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.flight.Flight;

import java.util.*;


/**
 * класс для анализа данных о полетах
 */
public class FlightAnalyzer {

    private final List<Flight> flights;

    public FlightAnalyzer(JsonArray ticketsArray) {
        this.flights = parseFlights(ticketsArray);
    }

    public void runAnalysis() {
        printMinFlightTimes();
        printPriceDifference();
    }

    /**
     * преобразует json массив билетов в список полетов
     *
     * @param ticketsArray массив json объектов с записями о билетах
     * @return список полетов
     */
    private List<Flight> parseFlights(JsonArray ticketsArray) {
        List<Flight> flights = new ArrayList<>();

        for (JsonElement ticketElement : ticketsArray) {
            JsonObject ticketObject = ticketElement.getAsJsonObject();
            Flight flight = new Flight(
                    ticketObject.get("origin").getAsString(),
                    ticketObject.get("destination").getAsString(),
                    ticketObject.get("carrier").getAsString(),
                    ticketObject.get("stops").getAsInt(),
                    ticketObject.get("price").getAsInt(),
                    ticketObject.get("departure_time").getAsString(),
                    ticketObject.get("arrival_time").getAsString()
            );
            flights.add(flight);
        }

        return flights;
    }

    private void printMinFlightTimes() {
        Map<String, String> minFlightTimes = calculateMinFlightTimes();

        System.out.println();
        System.out.println("Min flight time for each air company:");

        if (minFlightTimes.isEmpty()) {
            System.out.println("No data");
        } else {
            for (Map.Entry<String, String> entry : minFlightTimes.entrySet()) {
                String carrier = entry.getKey();
                String minTime = entry.getValue();
                System.out.println(carrier + ": " + minTime);
            }
        }
    }


    private void printPriceDifference() {
        double priceDifference = calculatePriceDifference();
        System.out.printf("Difference between the average price and the median for the flight " +
                "between Vladivostok and Tel Aviv: %.2f rubles%n", priceDifference);
    }

    private Map<String, String> calculateMinFlightTimes() {
        Map<String, String> minFlightTimes = new HashMap<>();

        for (Flight flight : flights) {
            String carrier = flight.getCarrier();
            String flightTime = calculateFlightTime(flight);

            if (!minFlightTimes.containsKey(carrier) || flightTime.compareTo(minFlightTimes.get(carrier)) < 0) {
                minFlightTimes.put(carrier, flightTime);
            }
        }

        return minFlightTimes;
    }


    private String calculateFlightTime(Flight flight) {
        String departureTime = flight.getDepartureTime();
        String arrivalTime = flight.getArrivalTime();

        int departureHour = Integer.parseInt(departureTime.split(":")[0]);
        int departureMinute = Integer.parseInt(departureTime.split(":")[1]);

        int arrivalHour = Integer.parseInt(arrivalTime.split(":")[0]);
        int arrivalMinute = Integer.parseInt(arrivalTime.split(":")[1]);

        int flightTimeInMinutes = (arrivalHour * 60 + arrivalMinute) - (departureHour * 60 + departureMinute);

        int flightHours = flightTimeInMinutes / 60;
        int flightMinutes = flightTimeInMinutes % 60;

        return String.format("%d hours %d minutes", flightHours, flightMinutes);
    }



    /**
     * вычисляет разницу между средней ценой и медианой для полетов между Владивостоком и Тель-Авивом
     *
     * @return разница между средней ценой и медианой
     */
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

    private double calculateAverage(List<Integer> prices) {
        return prices.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
    }

    /**
     * вычисляет медиану из списка цен
     *
     * @param prices список цен
     * @return медиана
     */
    private double calculateMedian(List<Integer> prices) {
        Collections.sort(prices);

        int size = prices.size();
        if (size % 2 == 0) {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            return prices.get(size / 2);
        }
    }
}
