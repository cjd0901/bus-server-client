package com.xdd.busserverc.domain;

public class Bus {
    private int id;
    private String busSpec;
    private Double Price;
    private String position;
    private String licensePlate;
    private int rentState;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusSpec() {
        return busSpec;
    }

    public void setBusSpec(String busSpec) {
        this.busSpec = busSpec;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getRentState() {
        return rentState;
    }

    public void setRentState(int rentState) {
        this.rentState = rentState;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id=" + id +
                ", busSpec='" + busSpec + '\'' +
                ", Price=" + Price +
                ", position='" + position + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", rentState=" + rentState +
                ", userId=" + userId +
                '}';
    }
}
