package com.example.erict.myairline.ReservationDatabase;

import android.database.Cursor;
import android.database.CursorWrapper;
import java.util.Date;
import java.util.UUID;

public class ReservationCursorWrapper extends CursorWrapper {

    public ReservationCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public ReservationDatabase getReservation(){
        String uuidString = getString(getColumnIndex(ReservationSchema.ReservationTable.Cols.UUID));
        String user = getString(getColumnIndex(ReservationSchema.ReservationTable.Cols.USER));
        String flightNumber = getString(getColumnIndex(ReservationSchema.ReservationTable.Cols.FLIGHT_NUMBER));
        String departureName = getString(getColumnIndex(ReservationSchema.ReservationTable.Cols.DEPARTURE_NAME));
        String departureTime = getString(getColumnIndex(ReservationSchema.ReservationTable.Cols.DEPARTURE_TIME));
        String arrivalName = getString(getColumnIndex(ReservationSchema.ReservationTable.Cols.ARRIVAL_NAME));
        int numTickets = getInt(getColumnIndex(ReservationSchema.ReservationTable.Cols.NUM_TICKETS));
        double totalPrice = getDouble(getColumnIndex(ReservationSchema.ReservationTable.Cols.TOTAL_PRICE));
        long date = getLong(getColumnIndex(ReservationSchema.ReservationTable.Cols.DATE_CREATED));

        ReservationDatabase reservation = new ReservationDatabase(UUID.fromString(uuidString));
        reservation.setUser(user);
        reservation.setFlightNumber(flightNumber);
        reservation.setDepartureName(departureName);
        reservation.setDepartureTime(departureTime);
        reservation.setArrivalName(arrivalName);
        reservation.setNumberTickets(numTickets);
        reservation.setTotalPrice(totalPrice);
        reservation.setDate(new Date(date));

        return reservation;
    }
}
