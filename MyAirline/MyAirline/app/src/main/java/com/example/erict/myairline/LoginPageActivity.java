package com.example.erict.myairline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.erict.myairline.CreateAccountDatabase.UserConnector;
import com.example.erict.myairline.CreateAccountDatabase.UserDatabase;
import com.example.erict.myairline.FlightDatabase.FlightConnector;
import com.example.erict.myairline.FlightDatabase.FlightDatabase;
import com.example.erict.myairline.ReservationDatabase.ReservationConnector;
import com.example.erict.myairline.ReservationDatabase.ReservationDatabase;

import java.util.List;

public class LoginPageActivity extends AppCompatActivity {

    // Declare location in which intent information will come in
    public static String mFlightNum;
    public static String mTickets;
    public static String mActivity;
    public static final String resultKeyPage1 = "com.example.erict.myairline.reservation";
    public static final String resultKeyPage2 = "com.example.erict.myairline.tickets";
    public static final String resultKeyPage3 = "com.example.erict.myairline.activity";

    // Declare layout objects
    EditText mUsername;
    EditText mPassword;
    Button mLoginButton;

    // Creates basic requirements for user data base retrieval
    UserConnector userConnector;
    List<UserDatabase> users;
    UserDatabase user;

    // Creates basic requirements for flight data base retrieval
    FlightConnector flightConnector;
    List<FlightDatabase> flights;
    FlightDatabase flight;

    // Creates basic requirements for reservation data base retrieval
    ReservationConnector mReservationConnector;
    List<ReservationDatabase> reservations;
    ReservationDatabase newReservation;

    // Declare alert system
    AlertDialog.Builder alertBuilder;
    AlertDialog mAlertDialog;
    String alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Intilize button and set listener
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(buttonListener);

        // Initialize basic stuff for user data retrieval
        userConnector = UserConnector.get(this);
        userConnector.updateList();
        users = userConnector.getUsers();

        // Initialize basic stuff for flights data retrieval
        flightConnector = FlightConnector.get(this);
        flightConnector.updateList();
        flights = flightConnector.getFlights();

        // Initialize basic stuff for reservation data retrieval
        mReservationConnector = ReservationConnector.get(this);
        mReservationConnector.updateList();
        reservations = mReservationConnector.getReservations();

        // Initialize alert system
        alertBuilder = new AlertDialog.Builder(this);
        mAlertDialog = alertBuilder.create();
        alert = "";
    }

    // declare a button listener
    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;  // Declare an intent to move around later

            // Initialize input layout objects
            mUsername = (EditText) findViewById(R.id.loginUsername);
            mPassword = (EditText) findViewById(R.id.loginPassword);

            // If input objects are empty, display error
            if(IsEmpty(mUsername) || IsEmpty(mPassword)){
                ToastMaker("All fields must be set");
            }
            else{
                // set values in input, to string type
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                // Check if user exists
                if(UserExist(username, password)){
                    for(FlightDatabase fd : flights){
                        if(fd.getFlightNumber().equalsIgnoreCase(mFlightNum)){
                            flight = fd;
                        }
                    }
                    // initialize a new reservation
                    newReservation = new ReservationDatabase();

                    // Update list
                    mReservationConnector.updateList();
                    reservations = mReservationConnector.getReservations();

                    // Set values to the new reservation
                    newReservation.setUser(user.getUsername());
                    newReservation.setFlightNumber(flight.getFlightNumber());
                    newReservation.setDepartureName(flight.getDeparture());
                    newReservation.setDepartureTime(flight.getDepartureTime());
                    newReservation.setArrivalName(flight.getArrival());
                    newReservation.setNumberTickets(Integer.parseInt(mTickets));
                    newReservation.setTotalPrice(flight.getPrice() * (double)Integer.parseInt(mTickets));

                    // Set alert system for confirmation
                    alert = newReservation.toString();
                    alertBuilder.setMessage(alert);

                    // Setting up the window, positive and negative options, if confirmed is clicked, save data into reservation data base
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mReservationConnector.addReservation(newReservation);
                            mReservationConnector.updateReservation(newReservation);
                            mAlertDialog.dismiss();
                            startActivity(MainActivity.MainMenu(LoginPageActivity.this));
                        }
                    });
                    // if user cancels reservation, new window pops up to confirm cancelation
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mAlertDialog.dismiss();
                            alert = "Are you sure you want to cancel?";
                            alertBuilder.setMessage(alert);

                            // if user clicks yes then dont save data, and go back to main screen
                            alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAlertDialog.dismiss();
                                    startActivity(MainActivity.MainMenu(LoginPageActivity.this));
                                }
                            });
                            // if no, dismiss window and user stays in login screen
                            alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAlertDialog.dismiss();
                                }
                            });

                            alertBuilder.show();
                        }
                    });
                    alertBuilder.show();
                }
                else{
                    // if user does not exist, display message
                    alertBuilder.setMessage(alert);
                    alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                            startActivity(MainActivity.MainMenu(LoginPageActivity.this));
                        }
                    });
                    alertBuilder.show();
                }
            }
        }
    };

    // checks to see if the user exists or not
    private boolean UserExist(String username, String password){
        if(!CreateAccountActivity.InputValidation(username) || !CreateAccountActivity.InputValidation(password)){
            alert = "Username and Password must contain at least three alpha and at least one digit";
        }else{
            // if user does exists, find in for loop and give to user variable
            for(UserDatabase u : users){
                if(u.getUsername().equals(username) && u.getPassword().equals(password)){
                    user = u;
                    return true;
                }
            }
        }
        return false;
    }

    // This function is called from ReserveSeatActivity. It's for reserving a a flight
    public static Intent ReserveInto(Context context, String flightNumber, String ticket, String activity){
        Intent intent = new Intent(context, LoginPageActivity.class);
        intent.putExtra(resultKeyPage1, flightNumber);
        intent.putExtra(resultKeyPage2, ticket);
        intent.putExtra(resultKeyPage3, activity);
        mFlightNum = intent.getStringExtra(resultKeyPage1);
        mTickets = intent.getStringExtra(resultKeyPage2);
        mActivity = intent.getStringExtra(resultKeyPage2);
        return intent;
    }

    // ToastMaker,
    private void ToastMaker(String message){
        Toast t = Toast.makeText(this.getApplicationContext(),message,Toast.LENGTH_LONG );
        t.show();
    }

    // IsEmpty,
    private boolean IsEmpty(EditText textToCheck){
        return textToCheck.getText().toString().trim().length() == 0;
    }
}
