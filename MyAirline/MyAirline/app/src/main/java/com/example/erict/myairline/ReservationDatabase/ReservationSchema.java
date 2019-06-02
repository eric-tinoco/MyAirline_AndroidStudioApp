package com.example.erict.myairline.ReservationDatabase;

public class ReservationSchema {

    public static final class ReservationTable{
        public static final String NAME = "RESERVATION";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String USER = "user";
            public static final String FLIGHT_NUMBER = "flight_number";
            public static final String DEPARTURE_NAME = "departure";
            public static final String DEPARTURE_TIME = "departure_time";
            public static final String ARRIVAL_NAME = "arrival";
            public static final String NUM_TICKETS = "number_of_tickets";
            public static final String TOTAL_PRICE = "price";
            public static final String DATE_CREATED = "date_created";
        }
    }
}
