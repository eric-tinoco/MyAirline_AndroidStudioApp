package com.example.erict.myairline;

import android.content.DialogInterface;
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

import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {

    public static final String TAG = "CreateAccount_Log";

    // Declare layout items
    EditText mUsernameEditText;
    EditText mEnterPassword;
    Button mJoinButton;

    // Declare UserConnector
    UserConnector userConnector;

    // Declare a list of UserDatabase objects
    List<UserDatabase> users;

    // Create an alert system
    AlertDialog.Builder alertBuilder;
    AlertDialog mAlertDialog;
    String alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize button and add listener
        mJoinButton = (Button) findViewById(R.id.createAccountButton);
        mJoinButton.setOnClickListener(buttonListener);

        // Initialize Connector and list
        userConnector = UserConnector.get(this);
        userConnector.updateList();
        users = userConnector.getUsers();

        // If list is empty, add these default users
        if(users.size() <= 0){
            CreateUsers("alice5", "csumb100");
            CreateUsers("brian77", "123ABC");
            CreateUsers("chris21", "CHRIS21");
            CreateUsers("admin2", "admin2");
        }

        // Initialize alert
        alertBuilder = new AlertDialog.Builder(this);
        mAlertDialog = alertBuilder.create();
        alert = "";
    }

    // Button listener for creating account
    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Initialize input fields
            mUsernameEditText = (EditText) findViewById(R.id.usernameEditText);
            mEnterPassword = (EditText) findViewById(R.id.passwordEditText);

            // If empty, display error
            if(IsEmpty(mUsernameEditText) || IsEmpty(mEnterPassword)){
                Log.i(TAG, "no username or password set");
                ToastMaker("All fields must be set");
            }else{
                // Update connector list
                userConnector.updateList();
                users = userConnector.getUsers();

                // Turn input into string type
                String username = mUsernameEditText.getText().toString();
                String password = mEnterPassword.getText().toString();

                // Check if username and password qualify, at leastthree alpha and one numerical
                if(checkUsername(username) && checkPassword(password)){
                    // Declare a new User
                    UserDatabase newUser = new UserDatabase();

                    // Set their values
                    newUser.setUsername(username);
                    newUser.setPassword(password);

                    // Add new users into the data base
                    userConnector.addUser(newUser);
                    userConnector.updateUser(newUser);

                    // Display successful addition
                    ToastMaker("User "+ username + " was added successfully!");

                    // Go back to main menu
                    startActivity(MainActivity.MainMenu(CreateAccountActivity.this));
                }
                else{
                    // display error
                    alertBuilder.setMessage(alert);
                    alertBuilder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                            startActivity(MainActivity.MainMenu(CreateAccountActivity.this));
                        }
                    });
                    alertBuilder.show();
                }
            }
        }
    };

    // Function to create users and add them to the database
    private void CreateUsers(String un, String pw){
        UserDatabase newUser = new UserDatabase();
        newUser.setUsername(un);
        newUser.setPassword(pw);
        userConnector.addUser(newUser);
        userConnector.updateUser(newUser);
    }

    // Checks if input matches restrictions
    public boolean checkUsername(String username){
        // To check if username is already taken
        for(UserDatabase user : users){
            if(user.getUsername().equals(username)){
                alert = "Username - " + username + " - is already taken. Please choose a different username.";
                return false;
            }
        }
        boolean inputVal = InputValidation(username);

        if(inputVal == false){
            alert = "Input Error: Username must contain at least three alpha characters and at least one digit.";
        }

        return inputVal;
    }

    // Checks if input matches restrictions
    public boolean checkPassword(String password){
        boolean inputVal = InputValidation(password);

        if(inputVal == false){
            alert = "Input Error: Password must contain at least three alpha characters and at least one digit.";
        }

        return inputVal;
    }

    // Checks if input matches restrictions
    public static boolean InputValidation(String input){
        int characterCount = 0;
        boolean hasDigit = false;

        for(char c : input.toCharArray()){
            if(Character.isLetter(c)){
                characterCount++;
            }
            if(Character.isDigit(c)){
                hasDigit = true;
            }
        }

        if(characterCount >= 3 && hasDigit){
            return true;
        }
        else{
            return false;
        }
    }

    // Toast master,
    private void ToastMaker(String message){
        Toast t = Toast.makeText(this.getApplicationContext(),message,Toast.LENGTH_LONG );
        t.show();
    }
    // Is Empty,
    private boolean IsEmpty(EditText textToCheck){
        return textToCheck.getText().toString().trim().length() == 0;
    }
}
