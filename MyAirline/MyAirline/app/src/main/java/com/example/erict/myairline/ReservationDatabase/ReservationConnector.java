package com.example.erict.myairline.ReservationDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;
import java.util.UUID;

public class ReservationConnector {
    private static ReservationConnector mReservationConnector;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private ReservationHelper mReservationHelper;
    private List<ReservationDatabase> mReservation;

    public static ReservationConnector get(Context context){
        if(mReservationConnector == null){
            mReservationConnector = new ReservationConnector(context);
        }
        return mReservationConnector;
    }

    private ReservationConnector(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ReservationHelper(mContext).getWritableDatabase();
        mReservationHelper = new ReservationHelper(mContext);
        mReservation = getReservations();
    }

    public void addReservation(ReservationDatabase reservation){
        ContentValues values = getContentValues(reservation);

        mReservationHelper.insertReservation(reservation);

    }

    public List<ReservationDatabase> getReservations(){
        return  mReservationHelper.getReservations();
    }

    public ReservationDatabase getReservation(UUID id){
        ReservationCursorWrapper cursor = queryReservations(
                ReservationSchema.ReservationTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}

        );
        try{
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getReservation();
        } finally {
            cursor.close();
        }
    }

    public void updateList(){
        mReservation = getReservations();
    }

    @Deprecated
    public void insertReservation(ReservationDatabase reservation){
        this.addReservation(reservation);
    }


    public void updateReservation(ReservationDatabase reservation){
        String uuidString = reservation.getReservationId().toString();
        ContentValues values = getContentValues(reservation);

        mReservationHelper.updateReservation(uuidString, values);

    }

    public void deleteTodo(ReservationDatabase reservation){
        String uuidString = reservation.getReservationId().toString();

        mReservationHelper.deleteReservation(uuidString);
    }

    @Deprecated //SHould call query DB directly from the helper
    private ReservationCursorWrapper queryReservations(String whereClause, String[] whereArgs){
        Cursor cursor = mReservationHelper.queryDB(ReservationSchema.ReservationTable.NAME,whereClause, whereArgs);

        return new ReservationCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(ReservationDatabase reservation){
        ContentValues values = new ContentValues();

        long date;
        if (reservation.getDate() == null){
            date = 0;
        } else {
            date = reservation.getDate().getTime();
        }

        values.put(ReservationSchema.ReservationTable.Cols.UUID, reservation.getReservationId().toString());
        values.put(ReservationSchema.ReservationTable.Cols.USER, reservation.getUser());
        values.put(ReservationSchema.ReservationTable.Cols.FLIGHT_NUMBER, reservation.getFlightNumber());
        values.put(ReservationSchema.ReservationTable.Cols.DEPARTURE_NAME, reservation.getDepartureName());
        values.put(ReservationSchema.ReservationTable.Cols.DEPARTURE_TIME, reservation.getDepartureTime());
        values.put(ReservationSchema.ReservationTable.Cols.ARRIVAL_NAME, reservation.getArrivalName());
        values.put(ReservationSchema.ReservationTable.Cols.NUM_TICKETS, reservation.getNumberTickets());
        values.put(ReservationSchema.ReservationTable.Cols.TOTAL_PRICE, reservation.getTotalPrice());
        values.put(ReservationSchema.ReservationTable.Cols.DATE_CREATED, date);

        return values;
    }

    public String getLogString(){
        StringBuilder sb = new StringBuilder();
        List<ReservationDatabase> reservations = mReservationHelper.getReservations();


        sb.append("Reservation\n");

        for(ReservationDatabase reservation : reservations){
            sb.append(reservation.toString());
        }

        return sb.toString();
    }

}
