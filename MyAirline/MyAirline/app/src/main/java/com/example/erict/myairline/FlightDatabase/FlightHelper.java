package com.example.erict.myairline.FlightDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class FlightHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "flights.db";

    private SQLiteDatabase db;

    public FlightHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + FlightSchema.FlightTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                FlightSchema.FlightTable.Cols.UUID + ", " +
                FlightSchema.FlightTable.Cols.FLIGHT_NUMBER + ", " +
                FlightSchema.FlightTable.Cols.DEPARTURE + ", " +
                FlightSchema.FlightTable.Cols.DEPARTURE_TIME + ", " +
                FlightSchema.FlightTable.Cols.ARRIVAL + ", " +
                FlightSchema.FlightTable.Cols.FLIGHT_CAPACITY + ", " +
                FlightSchema.FlightTable.Cols.PRICE + ", " +
                FlightSchema.FlightTable.Cols.DATE_CREATED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }


    public long insertFlight(FlightDatabase flight){
        /*
        You may be asking: 'What the heck are ContentValues?'
        They are a key:value store, not unlike a hashmap, but designed
        specifically for working with SQLite.
         */
        ContentValues cv = new ContentValues();
        cv.put(FlightSchema.FlightTable.Cols.UUID, flight.getFlightId().toString());
        cv.put(FlightSchema.FlightTable.Cols.FLIGHT_NUMBER, flight.getFlightNumber());
        cv.put(FlightSchema.FlightTable.Cols.DEPARTURE, flight.getDeparture());
        cv.put(FlightSchema.FlightTable.Cols.DEPARTURE_TIME, flight.getDepartureTime());
        cv.put(FlightSchema.FlightTable.Cols.ARRIVAL, flight.getArrival());
        cv.put(FlightSchema.FlightTable.Cols.FLIGHT_CAPACITY, flight.getFlightCapacity());
        cv.put(FlightSchema.FlightTable.Cols.PRICE, flight.getPrice());

        try {
            cv.put(FlightSchema.FlightTable.Cols.DATE_CREATED, flight.getDate().getTime());
        }catch (NullPointerException e){
            cv.put(FlightSchema.FlightTable.Cols.DATE_CREATED, "" );
            System.out.println("Date set to null");
        }

        db = this.getWritableDatabase();

        //name of the table, nullColumnHack, data to add
        return db.insert(FlightSchema.FlightTable.NAME, null, cv);

    }

    public boolean updateFlight(String uuidString, ContentValues values){

        try {
            db = this.getWritableDatabase();
            db.update(FlightSchema.FlightTable.NAME, values, FlightSchema.FlightTable.Cols.UUID + " = ?",
                    new String[]{uuidString});
            //guess what? we are preventing SQL injection!\
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean deleteFlight(String uuidString){

        try {
            db = this.getWritableDatabase();
            db.delete(FlightSchema.FlightTable.NAME, FlightSchema.FlightTable.Cols.UUID + " = ?",
                    new String[]{uuidString});
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Cursor queryDB(String DBName, String whereClause, String[] whereArgs){
        db = this.getWritableDatabase();
        try {
            return db.query(
                    FlightSchema.FlightTable.NAME,
                    null, //Which columns to select. Null == all columns
                    whereClause,
                    whereArgs,// There castle... why are we talking like this?
                    null, //groupBy: we are ignoring this
                    null, //having: we are ignoring this
                    null  //orderBy: we are ignoring this
            );
        } catch (Exception e){
            return null;
        }

    }

    public List<FlightDatabase> getFlights(){
        List<FlightDatabase> flights = new ArrayList<>();
        //hey guess what? You can't cast a cursor => CursorWrapper! who knew!
        // so instead we make a new one. Difficult difficult lemon difficult.
        FlightCursorWrapper cursor = new FlightCursorWrapper(this.queryDB(FlightSchema.FlightTable.NAME, null,null));

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                flights.add(cursor.getFlight());
                cursor.moveToNext();
            }
        }finally{
            if(cursor != null)
                cursor.close();
        }

        return flights;
    }
}
