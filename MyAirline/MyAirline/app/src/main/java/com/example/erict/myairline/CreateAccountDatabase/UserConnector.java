package com.example.erict.myairline.CreateAccountDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;
import java.util.UUID;
import com.example.erict.myairline.CreateAccountDatabase.UserSchema.UserTable;

public class UserConnector {
    private static UserConnector mUserConnector;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private UserHelper mUserHelper;
    private List<UserDatabase> mUsers;

    public static UserConnector get(Context context){
        if(mUserConnector == null){
            mUserConnector = new UserConnector(context);
        }
        return mUserConnector;
    }

    private UserConnector(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new UserHelper(mContext).getWritableDatabase();
        mUserHelper = new UserHelper(mContext);
        mUsers = getUsers();
    }

    public void addUser(UserDatabase user){
        ContentValues values = getContentValues(user);

        mUserHelper.insertUser(user);

    }

    public List<UserDatabase> getUsers(){
        return  mUserHelper.getUsers();
    }

    public UserDatabase getUser(UUID id){
        UserCursorWrapper cursor = queryUsers(
                UserTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}

        );
        try{
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getUser();
        } finally {
            cursor.close();
        }
    }

    public void updateList(){
        mUsers = getUsers();
    }

    @Deprecated
    public void insertUser(UserDatabase user){
        this.addUser(user);
    }


    public void updateUser(UserDatabase user){
        String uuidString = user.getUserId().toString();
        ContentValues values = getContentValues(user);

        mUserHelper.updateUser(uuidString, values);

    }

    public void deleteTodo(UserDatabase user){
        String uuidString = user.getUserId().toString();

        mUserHelper.deleteUser(uuidString);
    }

    @Deprecated //SHould call query DB directly from the helper
    private UserCursorWrapper queryUsers(String whereClause, String[] whereArgs){
        Cursor cursor = mUserHelper.queryDB(UserSchema.UserTable.NAME,whereClause, whereArgs);

        return new UserCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(UserDatabase user){
        ContentValues values = new ContentValues();

        long date;
        if (user.getDate() == null){
            date = 0;
        } else {
            date = user.getDate().getTime();
        }

        values.put(UserSchema.UserTable.Cols.UUID, user.getUserId().toString());
        values.put(UserSchema.UserTable.Cols.DATE, date);
        values.put(UserSchema.UserTable.Cols.USERNAME, user.getUsername());
        values.put(UserSchema.UserTable.Cols.PASSWORD, user.getPassword());

        return values;
    }

    public String getLogString(){
        StringBuilder sb = new StringBuilder();
        List<UserDatabase> users = mUserHelper.getUsers();


        sb.append("Users\n");

        for(UserDatabase user : users){
            sb.append(user.toString());
        }

        return sb.toString();
    }
}
