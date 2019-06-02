package com.example.erict.myairline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erict.myairline.CreateAccountDatabase.UserConnector;
import com.example.erict.myairline.FlightDatabase.FlightConnector;
import com.example.erict.myairline.FlightDatabase.FlightDatabase;
import com.example.erict.myairline.ReservationDatabase.ReservationConnector;
import com.example.erict.myairline.ReservationDatabase.ReservationDatabase;

import java.util.List;

public class ReserveSeatActivity extends AppCompatActivity {

    // Declare and initialize storage for incoming intent content
    public static String user;
    public static final String resultKeyPage = "com.example.erict.myairline.user";

    // Declare buttons
    Button mSearchButton;
    Button mFilterButton;
    TextView mFlightOptions;
    EditText mDeparture;
    EditText mArrival;
    EditText mTickets;
    EditText mPickedFlight;

    // Declare a flight connector
    FlightConnector mFlightConnector;

    // Declare a list of Flights
    List<FlightDatabase> flights;

    // Declare an alert system
    AlertDialog.Builder alertBuilder;
    AlertDialog mAlertDialog;
    String alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_seat);

        // Initialize TextView to show all flights
        mFlightOptions = (TextView) findViewById(R.id.flightOptions);

        // Initialize filterButton, and give it a listener
        mFilterButton = (Button) findViewById(R.id.filterButton);
        mFilterButton.setOnClickListener(buttonListener);

        // Initialize SearchButton, and give it a listener
        mSearchButton = (Button) findViewById(R.id.flightButton);
        mSearchButton.setOnClickListener(buttonListener);

        // Initialize FlightConnector
        mFlightConnector = FlightConnector.get(this);
        mFlightConnector.updateList();
        flights = mFlightConnector.getFlights();

        // If list is emtpy, add these default flights to database
        if(flights.size() <= 0){
            CreateFlight("Otter101", "Monterey", "Los Angeles", "10:00(AM)", 10, 150.0);
            CreateFlight("Otter102", "Los Angeles", "Monterey", "1:00(PM)", 10, 150.0);
            CreateFlight("Otter201", "Monterey", "Seattle", "11:00(AM)", 5, 200.5);
            CreateFlight("Otter205", "Monterey", "Seattle", "3:00(PM)", 15, 150.0);
            CreateFlight("Otter202", "Seattle", "Monterey", "2:00(PM)", 5, 200.5);
        }

        // Initialize alert system
        alertBuilder = new AlertDialog.Builder(this);
        mAlertDialog = alertBuilder.create();
        alert = "";

        // Set
        mFlightOptions.setText("Choose flights from these locations: \nMonterey, Los Angeles, Seattle.");
        //mFlightOptions.setText(mFlightConnector.getLogString());
    }

    // Declare a button listener object
    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Switch statement, for which button is pressed inside of layout
            switch (v.getId()){
                // When flight is chosen
                case R.id.flightButton:
                    // Initilize input from layout
                    mPickedFlight = (EditText) findViewById(R.id.flightName);
                    mTickets = (EditText) findViewById(R.id.numberTickets);

                    // if input layout is empty, display error
                    if(IsEmpty(mPickedFlight)){
                        ToastMaker("Input Error: Please enter the flight number.");
                    }
                    // also when tickets is empty
                    else if(IsEmpty(mTickets)){
                        ToastMaker("Input Error: Please enter number of seats.");
                    }else{

                        // set values into string type
                        String pickedFlight = mPickedFlight.getText().toString();
                        String tickets = mTickets.getText().toString();

                        // For loop through flights, if seats is greater than capacity display error. otherwise, go back to move to login screen
                        for(FlightDatabase fd : flights){
                            if(fd.getFlightNumber().equalsIgnoreCase(pickedFlight)){
                                if((fd.getFlightCapacity() < Integer.parseInt(tickets))){
                                    ToastMaker("Not enough seats. Please Try again");
                                    return;
                                }
                                else{
                                    Intent intent = LoginPageActivity.ReserveInto(ReserveSeatActivity.this, pickedFlight, tickets, "ReserveSeat");
                                    startActivity(intent);
                                }
                            }
                        }
                    }

                    break;
                    // when filter is required
                case R.id.filterButton:
                    // Get the three items to use for filtering
                    mDeparture = (EditText)findViewById(R.id.departureText);
                    mArrival = (EditText) findViewById(R.id.arrivalText);
                    mTickets = (EditText) findViewById(R.id.numberTickets);

                    // Check to see if inputs are empty
                    if(IsEmpty(mDeparture) || IsEmpty(mArrival) || IsEmpty(mTickets)){
                        ToastMaker("All fields must be set: Departure, Arrival, Number of Tickets.");
                    }else{
                        // set input in layout to string type, and int for tickets
                        String departure = mDeparture.getText().toString();
                        String arrival = mArrival.getText().toString();
                        String tickets = mTickets.getText().toString();
                        int ticket = Integer.parseInt(tickets);

                        // if asking for more than 7 tickets, alert that it's not possible to book flight
                        if(ticket >= 7){

                            alert = "Error: Not enough seats. Please Try again";
                            alertBuilder.setMessage(alert);
                            alertBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAlertDialog.dismiss();
                                }
                            });
                            alertBuilder.show();
                            return;
                        }

                        // alert when no flight exists
                        if(!CheckFlight(departure, arrival)){
                            alert = "Error: No flights available for departure: " + departure + " to " + arrival;
                            alertBuilder.setMessage(alert);
                            alertBuilder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAlertDialog.dismiss();
                                    startActivity(MainActivity.MainMenu(ReserveSeatActivity.this));
                                }
                            });
                            alertBuilder.show();
                            return;

                        }

                        // when locations aren't in database
                        if(!CheckLocation(departure) || !CheckLocation(arrival)){
                            ToastMaker("Location Does not exist");
                            return;
                        }
                        // if user picks the same location in departure and arrival
                        if(departure.equals(arrival)){
                            ToastMaker("You're already in " + departure + "...");
                            return;
                        }
                        // set text back to null, then to filtered text
                        mFlightOptions.setText(null);
                        mFlightOptions.setText(mFlightConnector.getFilterlogStrings(departure, arrival, ticket));
                    }

                    break;
            }
        }
    };

    // checks if flight exists between two parameters, departure and arrival
    private boolean CheckFlight(String dep, String arr){
        for(FlightDatabase fd : flights){
            if(fd.getDeparture().equalsIgnoreCase(dep) && fd.getArrival().equalsIgnoreCase(arr)){
                return true;
            }
        }

        return false;
    }

    // Check's to see if user types other cities then the ones restricted to now
    private boolean CheckLocation(String loc){
        boolean statement = false;

        if(loc.equalsIgnoreCase("Monterey") || loc.equalsIgnoreCase("Los Angeles") || loc.equalsIgnoreCase("Seattle")){
            statement = true;
        }
        return statement;
    }

    // Creates flight
    private void CreateFlight(String fn, String dep, String arr, String depTime, int cap, double p){
        FlightDatabase newFlight = new FlightDatabase();
        newFlight.setFlightNumber(fn);
        newFlight.setDeparture(dep);
        newFlight.setArrival(arr);
        newFlight.setDepartureTime(depTime);
        newFlight.setFlightCapacity(cap);
        newFlight.setPrice(p);
        mFlightConnector.addFlight(newFlight);
        mFlightConnector.updateFlight(newFlight);
    }

    // Toast master,
    private void ToastMaker(String message){
        Toast t = Toast.makeText(this.getApplicationContext(),message,Toast.LENGTH_LONG );
        t.show();
    }

    // IsEmpty,
    private boolean IsEmpty(EditText textToCheck){
        return textToCheck.getText().toString().trim().length() == 0;
    }
}
