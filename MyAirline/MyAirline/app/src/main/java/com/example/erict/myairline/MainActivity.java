package com.example.erict.myairline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Declare buttons from welcome page
    Button mCreateAccount;
    Button mReserveSeat;
    Button mCancelReservation;
    Button mManageSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons with the the correct ID's
        mCreateAccount = (Button) findViewById(R.id.createAccountButton);
        mReserveSeat = (Button) findViewById(R.id.reserveSeatButton);
        mCancelReservation = (Button) findViewById(R.id.cancelReservationButton);
        mManageSystem = (Button) findViewById(R.id.manageSystemButton);

        // Set Listeners to the buttons
        mCreateAccount.setOnClickListener(buttonListener);
        mReserveSeat.setOnClickListener(buttonListener);
        mCancelReservation.setOnClickListener(buttonListener);
        mManageSystem.setOnClickListener(buttonListener);
    }

    // OnClickListener function: set to listen to buttons within welcome page
    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Create an intent that will go to the next activity
            Intent intent;

            // Switch Statement: Will take in whichever button was clicked and does what it needs to do.
            switch(v.getId()){
                case R.id.createAccountButton:
                    intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                    startActivity(intent);
                    break;
                case R.id.reserveSeatButton:
                    intent = new Intent(MainActivity.this, ReserveSeatActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cancelReservationButton:

                    break;
                case R.id.manageSystemButton:

                    break;

            }
        }
    };

    // Static function to go back to main
    public static Intent MainMenu(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
