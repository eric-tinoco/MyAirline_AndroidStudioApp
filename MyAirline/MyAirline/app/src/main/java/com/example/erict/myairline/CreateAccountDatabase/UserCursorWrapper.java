package com.example.erict.myairline.CreateAccountDatabase;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class UserCursorWrapper extends CursorWrapper {

    public UserCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public UserDatabase getUser(){
        String uuidString = getString(getColumnIndex(UserSchema.UserTable.Cols.UUID));
        String userName = getString(getColumnIndex(UserSchema.UserTable.Cols.USERNAME));
        String password = getString(getColumnIndex(UserSchema.UserTable.Cols.PASSWORD));
        long date = getLong(getColumnIndex(UserSchema.UserTable.Cols.DATE));

        UserDatabase user = new UserDatabase(UUID.fromString(uuidString));
        user.setUsername(userName);
        user.setPassword(password);
        user.setDate(new Date(date));

        return user;
    }
}
