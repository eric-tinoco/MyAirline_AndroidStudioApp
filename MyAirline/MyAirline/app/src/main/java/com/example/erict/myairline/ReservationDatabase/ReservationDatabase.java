package com.example.erict.myairline.ReservationDatabase;

import java.util.Date;
import java.util.UUID;

public class ReservationDatabase {
    private static int mReservationNumber = 999;

    private UUID mReservationId;
    private String mUser;
    private String mFlight;
    private String mDepartureName;
    private String mDepartureTime;
    private String mArrivalName;
    private int mNumberTickets;
    private int mReservationNum;
    private double mTotalPrice;
    private Date mDate;

    public ReservationDatabase(){
        this(UUID.randomUUID());
    }

    public ReservationDatabase(UUID id){
        mReservationId = id;
        mDate = new Date();
        setReservationNum(mReservationNumber++);
    }

    public static int getmReservationNumber() {
        return mReservationNumber;
    }

    public static void setmReservationNumber(int mReservationNumber) {
        ReservationDatabase.mReservationNumber = mReservationNumber;
    }

    public UUID getReservationId() {
        return mReservationId;
    }

    public void setReservationId(UUID reservationId) {
        mReservationId = reservationId;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }

    public String getFlightNumber() {
        return mFlight;
    }

    public void setFlightNumber(String flight) {
        mFlight = flight;
    }

    public String getDepartureName() {
        return mDepartureName;
    }

    public void setDepartureName(String departureName) {
        mDepartureName = departureName;
    }

    public String getDepartureTime() {
        return mDepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        mDepartureTime = departureTime;
    }

    public String getArrivalName() {
        return mArrivalName;
    }

    public void setArrivalName(String arrivalName) {
        mArrivalName = arrivalName;
    }

    public int getNumberTickets() {
        return mNumberTickets;
    }

    public void setNumberTickets(int numberTickets) {
        mNumberTickets = numberTickets;
    }

    public int getReservationNum() {
        return mReservationNum;
    }

    public void setReservationNum(int reservationNum) {
        mReservationNum = reservationNum;
    }

    public double getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        mTotalPrice = totalPrice;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String toString(){
        return "Username: " + getUser() + "\n" +
               "Flight number: " + getFlightNumber() + "\n" +
               "Departure: " + getDepartureName() + ", " + getDepartureTime() + "\n" +
               "Arrival: " + getArrivalName() + "\n" +
               "Number of tickets: " + getNumberTickets() + "\n" +
               "Reservation number: " + getmReservationNumber() + "\n" +
               "Total amount: $" + String.format("%.2f", getTotalPrice());
    }
}
