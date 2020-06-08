package com.xdd.busserverc.domain;

public class Ticket {
    private int id;
    private String fromStation;
    private String toStation;
    private String departureDate;
    private String departureTime;
    private Double price;
    private int restTicket;
    private Double servicePrice;
    private int busSpec;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getRestTicket() {
        return restTicket;
    }

    public void setRestTicket(int restTicket) {
        this.restTicket = restTicket;
    }

    public Double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(Double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public int getBusSpec() {
        return busSpec;
    }

    public void setBusSpec(int busSpec) {
        this.busSpec = busSpec;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", fromStation='" + fromStation + '\'' +
                ", toStation='" + toStation + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", price=" + price +
                ", restTicket=" + restTicket +
                ", servicePrice=" + servicePrice +
                ", busSpec=" + busSpec +
                '}';
    }
}
