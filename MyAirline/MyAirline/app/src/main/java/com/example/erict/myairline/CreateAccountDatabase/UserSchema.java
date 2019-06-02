package com.example.erict.myairline.CreateAccountDatabase;

public class UserSchema {

    public static final class UserTable{
        public static final String NAME = "users";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String DATE = "date";
        }
    }
}
