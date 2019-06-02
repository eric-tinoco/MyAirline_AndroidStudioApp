package com.example.erict.myairline.FlightDatabase;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class FlightCursorWrapper extends CursorWrapper {
    public FlightCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public FlightDatabase getFlight(){
        String uuidString = getString(getColumnIndex(FlightSchema.FlightTable.Cols.UUID));
        String flightNumber = getString(getColumnIndex(FlightSchema.FlightTable.Cols.FLIGHT_NUMBER));
        String departure = getString(getColumnIndex(FlightSchema.FlightTable.Cols.DEPARTURE));
        String departureTime = getString(getColumnIndex(FlightSchema.FlightTable.Cols.DEPARTURE_TIME));
        String arrival = getString(getColumnIndex(FlightSchema.FlightTable.Cols.ARRIVAL));
        int flightCapacity = getInt(getColumnIndex(FlightSchema.FlightTable.Cols.FLIGHT_CAPACITY));
        double price = getDouble(getColumnIndex(FlightSchema.FlightTable.Cols.PRICE));
        long date = getLong(getColumnIndex(FlightSchema.FlightTable.Cols.DATE_CREATED));

        FlightDatabase flight = new FlightDatabase(UUID.fromString(uuidString));
        flight.setFlightNumber(flightNumber);
        flight.setDeparture(departure);
        flight.setDepartureTime(departureTime);
        flight.setArrival(arrival);
        flight.setFlightCapacity(flightCapacity);
        flight.setPrice(price);
        flight.setDate(new Date(date));

        return flight;
    }

}
