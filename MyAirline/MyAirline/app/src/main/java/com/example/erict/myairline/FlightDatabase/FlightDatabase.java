package com.example.erict.myairline.FlightDatabase;

import java.util.Date;
import java.util.UUID;

public class FlightDatabase {
    private UUID mFlightId;
    private String mFlightNumber;
    private String mDeparture;
    private String mArrival;
    private String mDepartureTime;
    private int mFlightCapacity;
    private double mPrice;
    private Date mDate;

    public FlightDatabase(){
        this(UUID.randomUUID());
    }

    public FlightDatabase(UUID id){
        mFlightId = id;
        mDate = new Date();
    }

    public UUID getFlightId() {
        return mFlightId;
    }

    public void setFlightId(UUID flightId) {
        mFlightId = flightId;
    }

    public String getFlightNumber() {
        return mFlightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        mFlightNumber = flightNumber;
    }

    public String getDeparture() {
        return mDeparture;
    }

    public void setDeparture(String departure) {
        mDeparture = departure;
    }

    public String getArrival() {
        return mArrival;
    }

    public void setArrival(String arrival) {
        mArrival = arrival;
    }

    public String getDepartureTime() {
        return mDepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        mDepartureTime = departureTime;
    }

    public int getFlightCapacity() {
        return mFlightCapacity;
    }

    public void setFlightCapacity(int flightCapacity) {
        mFlightCapacity = flightCapacity;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String toString(){
        return mFlightNumber + " - " + mDeparture + " - " + mArrival + " - " + mDepartureTime + " - " + mFlightCapacity + " seats - $" + String.format("%.2f", mPrice);
    }
}
