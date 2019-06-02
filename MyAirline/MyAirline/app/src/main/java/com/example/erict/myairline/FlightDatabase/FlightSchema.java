package com.example.erict.myairline.FlightDatabase;

public class FlightSchema {

    public static final class FlightTable{
        public static final String NAME = "USERS";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String FLIGHT_NUMBER = "flight_number";
            public static final String DEPARTURE = "departure";
            public static final String DEPARTURE_TIME = "departure_time";
            public static final String ARRIVAL = "arrival";
            public static final String FLIGHT_CAPACITY = "number_of_seats";
            public static final String PRICE = "price";
            public static final String DATE_CREATED = "date_created";
        }
    }
}
