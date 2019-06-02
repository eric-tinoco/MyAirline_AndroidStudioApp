package com.example.erict.myairline.FlightDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;
import java.util.UUID;

public class FlightConnector {
    private static FlightConnector mFlightConnector;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private FlightHelper mFlightHelper;
    private List<FlightDatabase> mFlight;

    public static FlightConnector get(Context context){
        if(mFlightConnector == null){
            mFlightConnector = new FlightConnector(context);
        }
        return mFlightConnector;
    }

    private FlightConnector(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new FlightHelper(mContext).getWritableDatabase();
        mFlightHelper = new FlightHelper(mContext);
        mFlight = getFlights();
    }

    public void addFlight(FlightDatabase flight){
        ContentValues values = getContentValues(flight);

        mFlightHelper.insertFlight(flight);

    }

    public List<FlightDatabase> getFlights(){
        return  mFlightHelper.getFlights();
    }

    public FlightDatabase getUser(UUID id){
        FlightCursorWrapper cursor = queryUsers(
                FlightSchema.FlightTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}

        );
        try{
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getFlight();
        } finally {
            cursor.close();
        }
    }

    public void updateList(){
        mFlight = getFlights();
    }

    @Deprecated
    public void insertFlight(FlightDatabase user){
        this.addFlight(user);
    }


    public void updateFlight(FlightDatabase flight){
        String uuidString = flight.getFlightId().toString();
        ContentValues values = getContentValues(flight);

        mFlightHelper.updateFlight(uuidString, values);

    }

    public void deleteTodo(FlightDatabase flight){
        String uuidString = flight.getFlightId().toString();

        mFlightHelper.deleteFlight(uuidString);
    }

    @Deprecated //SHould call query DB directly from the helper
    private FlightCursorWrapper queryUsers(String whereClause, String[] whereArgs){
        Cursor cursor = mFlightHelper.queryDB(FlightSchema.FlightTable.NAME,whereClause, whereArgs);

        return new FlightCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(FlightDatabase flight){
        ContentValues values = new ContentValues();

        long date;
        if (flight.getDate() == null){
            date = 0;
        } else {
            date = flight.getDate().getTime();
        }

        values.put(FlightSchema.FlightTable.Cols.UUID, flight.getFlightId().toString());
        values.put(FlightSchema.FlightTable.Cols.FLIGHT_NUMBER, flight.getFlightNumber());
        values.put(FlightSchema.FlightTable.Cols.DEPARTURE, flight.getDeparture());
        values.put(FlightSchema.FlightTable.Cols.DEPARTURE_TIME, flight.getDepartureTime());
        values.put(FlightSchema.FlightTable.Cols.ARRIVAL, flight.getArrival());
        values.put(FlightSchema.FlightTable.Cols.FLIGHT_CAPACITY, flight.getFlightCapacity());
        values.put(FlightSchema.FlightTable.Cols.PRICE, flight.getPrice());
        values.put(FlightSchema.FlightTable.Cols.DATE_CREATED, date);

        return values;
    }

    public String getLogString(){
        StringBuilder sb = new StringBuilder();
        List<FlightDatabase> flights = mFlightHelper.getFlights();


        sb.append("Flight No. - Departure - Arrival - Departure Time - Flight Capacity - Price\n");

        for(FlightDatabase flight : flights){
            sb.append(flight.toString()).append("\n");
        }

        return sb.toString();
    }

    public String getFilterlogStrings(String departure, String arrival, int ticket){
        StringBuilder sb = new StringBuilder();
        List<FlightDatabase> flights = mFlightHelper.getFlights();


        sb.append("Flight No. - Departure - Arrival - Departure Time - Flight Capacity - Price\n");

        for(FlightDatabase fd : flights){
            if(fd.getDeparture().equalsIgnoreCase(departure) && fd.getArrival().equalsIgnoreCase(arrival) && fd.getFlightCapacity() >= ticket){
                sb.append(fd.toString()).append("\n");
            }
        }

        return sb.toString();
    }
}
