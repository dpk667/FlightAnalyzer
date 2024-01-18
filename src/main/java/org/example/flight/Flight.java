package org.example.flight;

public class Flight {

    private String origin;
    private String destination;
    private String carrier;
    private int stops;
    private int price;
    private String departureTime;
    private String arrivalTime;

    public Flight(String origin, String destination, String carrier,
                  int stops, int price, String departureTime, String arrivalTime) {
        this.origin = origin;
        this.destination = destination;
        this.carrier = carrier;
        this.stops = stops;
        this.price = price;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getCarrier() {
        return carrier;
    }

    public int getStops() {
        return stops;
    }
    public int getPrice() {
        return price;
    }
    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }
}
