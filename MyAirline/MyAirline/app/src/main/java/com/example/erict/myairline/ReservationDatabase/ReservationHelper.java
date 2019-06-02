package com.example.erict.myairline.ReservationDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ReservationHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "reservation.db";

    private SQLiteDatabase db;

    public ReservationHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + ReservationSchema.ReservationTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ReservationSchema.ReservationTable.Cols.UUID + ", " +
                ReservationSchema.ReservationTable.Cols.USER + ", " +
                ReservationSchema.ReservationTable.Cols.FLIGHT_NUMBER + ", " +
                ReservationSchema.ReservationTable.Cols.DEPARTURE_NAME + ", " +
                ReservationSchema.ReservationTable.Cols.DEPARTURE_TIME + ", " +
                ReservationSchema.ReservationTable.Cols.ARRIVAL_NAME + ", " +
                ReservationSchema.ReservationTable.Cols.NUM_TICKETS + ", " +
                ReservationSchema.ReservationTable.Cols.TOTAL_PRICE + ", " +
                ReservationSchema.ReservationTable.Cols.DATE_CREATED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }


    public long insertReservation(ReservationDatabase reservation){
        /*
        You may be asking: 'What the heck are ContentValues?'
        They are a key:value store, not unlike a hashmap, but designed
        specifically for working with SQLite.
         */
        ContentValues cv = new ContentValues();
        cv.put(ReservationSchema.ReservationTable.Cols.UUID, reservation.getReservationId().toString());
        cv.put(ReservationSchema.ReservationTable.Cols.USER, reservation.getUser());
        cv.put(ReservationSchema.ReservationTable.Cols.FLIGHT_NUMBER, reservation.getFlightNumber());
        cv.put(ReservationSchema.ReservationTable.Cols.DEPARTURE_NAME, reservation.getDepartureName());
        cv.put(ReservationSchema.ReservationTable.Cols.DEPARTURE_TIME, reservation.getDepartureTime());
        cv.put(ReservationSchema.ReservationTable.Cols.ARRIVAL_NAME, reservation.getArrivalName());
        cv.put(ReservationSchema.ReservationTable.Cols.NUM_TICKETS, reservation.getNumberTickets());
        cv.put(ReservationSchema.ReservationTable.Cols.TOTAL_PRICE, reservation.getTotalPrice());

        try {
            cv.put(ReservationSchema.ReservationTable.Cols.DATE_CREATED, reservation.getDate().getTime());
        }catch (NullPointerException e){
            cv.put(ReservationSchema.ReservationTable.Cols.DATE_CREATED, "" );
            System.out.println("Date set to null");
        }

        db = this.getWritableDatabase();

        //name of the table, nullColumnHack, data to add
        return db.insert(ReservationSchema.ReservationTable.NAME, null, cv);

    }

    public boolean updateReservation(String uuidString, ContentValues values){

        try {
            db = this.getWritableDatabase();
            db.update(ReservationSchema.ReservationTable.NAME, values, ReservationSchema.ReservationTable.Cols.UUID + " = ?",
                    new String[]{uuidString});
            //guess what? we are preventing SQL injection!\
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean deleteReservation(String uuidString){

        try {
            db = this.getWritableDatabase();
            db.delete(ReservationSchema.ReservationTable.NAME, ReservationSchema.ReservationTable.Cols.UUID + " = ?",
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
                    ReservationSchema.ReservationTable.NAME,
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

    public List<ReservationDatabase> getReservations(){
        List<ReservationDatabase> reservation = new ArrayList<>();
        //hey guess what? You can't cast a cursor => CursorWrapper! who knew!
        // so instead we make a new one. Difficult difficult lemon difficult.
        ReservationCursorWrapper cursor = new ReservationCursorWrapper(this.queryDB(ReservationSchema.ReservationTable.NAME, null,null));

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                reservation.add(cursor.getReservation());
                cursor.moveToNext();
            }
        }finally{
            if(cursor != null)
                cursor.close();
        }

        return reservation;
    }

}
