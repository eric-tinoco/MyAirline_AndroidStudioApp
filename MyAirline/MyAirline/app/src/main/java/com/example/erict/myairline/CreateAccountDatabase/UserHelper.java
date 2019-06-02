package com.example.erict.myairline.CreateAccountDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import com.example.erict.myairline.CreateAccountDatabase.UserSchema.UserTable;

public class UserHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "users.db";

    private SQLiteDatabase db;

    public UserHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + UserTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                UserTable.Cols.UUID + ", " +
                UserTable.Cols.DATE + ", " +
                UserTable.Cols.USERNAME + ", " +
                UserTable.Cols.PASSWORD+
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }


    public long insertUser(UserDatabase user){
        /*
        You may be asking: 'What the heck are ContentValues?'
        They are a key:value store, not unlike a hashmap, but designed
        specifically for working with SQLite.
         */
        ContentValues cv = new ContentValues();
        cv.put(UserTable.Cols.UUID, user.getUserId().toString());
        cv.put(UserTable.Cols.USERNAME, user.getUsername());
        cv.put(UserTable.Cols.PASSWORD, user.getPassword());

        try {
            cv.put(UserTable.Cols.DATE, user.getDate().getTime());
        }catch (NullPointerException e){
            cv.put(UserTable.Cols.DATE, "" );
            System.out.println("Date set to null");
        }

        db = this.getWritableDatabase();

        //name of the table, nullColumnHack, data to add
        return db.insert(UserTable.NAME, null, cv);

    }

    public boolean updateUser(String uuidString, ContentValues values){

        try {
            db = this.getWritableDatabase();
            db.update(UserTable.NAME, values, UserTable.Cols.UUID + " = ?",
                    new String[]{uuidString});
            //guess what? we are preventing SQL injection!\
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean deleteUser(String uuidString){

        try {
            db = this.getWritableDatabase();
            db.delete(UserTable.NAME, UserTable.Cols.UUID + " = ?",
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
                    UserTable.NAME,
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

    public List<UserDatabase> getUsers(){
        List<UserDatabase> users = new ArrayList<>();
        //hey guess what? You can't cast a cursor => CursorWrapper! who knew!
        // so instead we make a new one. Difficult difficult lemon difficult.
        UserCursorWrapper cursor = new UserCursorWrapper(this.queryDB(UserSchema.UserTable.NAME, null,null));

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                users.add(cursor.getUser());
                cursor.moveToNext();
            }
        }finally{
            if(cursor != null)
                cursor.close();
        }

        return users;
    }
}
