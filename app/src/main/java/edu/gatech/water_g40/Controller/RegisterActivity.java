package edu.gatech.water_g40.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.Model.Report;
import edu.gatech.water_g40.R;

import static edu.gatech.water_g40.Controller.LoginActivity.accountHashtable;

/**
 * Created by Vikram Kumar (Archlefirth) on 2/22/2017.
 *
 * Allows a user to register and create a new account
 *
 */
public class RegisterActivity extends AppCompatActivity {

    // UI references.
    private EditText userEditField;
    private EditText passEditField;
    private EditText passVerifEditField;


    boolean cancelClicked = false;

    /*
     * Standard activity method to initialize GUI elements and create the screen
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // The Text Fields containing the new user's account data
        userEditField = (EditText) findViewById(R.id.username_text);
        passEditField = (EditText) findViewById(R.id.enter_password);
        passVerifEditField = (EditText) findViewById(R.id.verify_password);
        passVerifEditField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        final Button backButton = (Button) findViewById(R.id.cancel_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelClicked = true;
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(myIntent);
            }
        });

        final Button enterButton = (Button) findViewById(R.id.start_registration);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    /*
     *  Attempts to register the account specified by the login form.
     *  If there are form errors (invalid email, missing fields, etc.), the
     *  errors are presented and no actual login attempt is made.
     *
     */
    private void attemptRegister() {
        // Reset errors.
        userEditField.setError(null);
        passEditField.setError(null);
        passVerifEditField.setError(null);

        boolean error = false;
        View focusView = null;

        // Store the data the user entered
        String userString = userEditField.getText().toString();
        String passString = passEditField.getText().toString();
        String passConfirmString = passVerifEditField.getText().toString();

        if (!cancelClicked) {
            //Check for a valid password,if the user entered one.
            if (passString.length() == 0 && !isPasswordValid(passString)) {
                passEditField.setError("The password is invalid");
                focusView = passEditField;
                error = true;
            } else if (passConfirmString.length() == 0 && !isPasswordValid(passConfirmString)) {
                passVerifEditField.setError("Please retype your password");
                focusView = passVerifEditField;
                error = true;
            } else if (!isPassMatch(passString, passConfirmString)) {
                passVerifEditField.setError("The passwords do not match");
                focusView = passVerifEditField;
                error = true;
            }

            //verify the username
            if (userString.length() == 0) {
                userEditField.setError("This field is required");
                focusView = userEditField;
                error = true;
            } else if (!isUsernameValid(userString)) {
                userEditField.setError("The username is too short");
                focusView = userEditField;
                error = true;
            }
        }

        if (!cancelClicked) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (error) {
                focusView.requestFocus();
            } else {
                // The account will be registered and added to the list of valid accounts
                List<Account> users = new ArrayList<Account>();
                try {
                    FileInputStream fis = openFileInput("myUsers");
                    ObjectInputStream objectInputStream = new ObjectInputStream(fis);
                    users = (List<Account>) objectInputStream.readObject();
                    Account a = new Account(userString, passString);
                    users.add(a);
                    objectInputStream.close();

                    FileOutputStream fileOutputStream = openFileOutput("myUsers", Context.MODE_PRIVATE);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(users);
                    objectOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    users = new ArrayList<Account>();
                    Account a = new Account(userString, passString);
                    users.add(a);
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("myUsers",
                                Context.MODE_PRIVATE);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(users);
                        objectOutputStream.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (IOException e) {
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("myUsers",
                                Context.MODE_PRIVATE);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(users);
                        objectOutputStream.close();
                    } catch (Exception ex) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                // The user will now be returned to the login screen so they can login with their new account
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(myIntent);
            }
        } else {
            // The account is not added to the list and the user returns to the login screen
            Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            RegisterActivity.this.startActivity(myIntent);
        }
    }


    /*
    * Checks if the username is valid
    *
    * @param username   the username entered into the text field
    * @return if the username is valid
     */
    // NOTE that password and email validity have to be the same across Activities
    private boolean isUsernameValid(String username) { return username.length() > 2; }
    /*
    * Checks if the password is valid
    *
    * @param password   the username entered into the text field
    * @return if the password is valid
     */
    // NOTE that password and email validity have to be the same across Activities
    private boolean isPasswordValid(String password) { return password.length() > 2; }

    /*
    * Checks if the passwords match
    *
    * @param pass1   the password entered into the password field
    * @param pass2   the password entered into the verify password field
     */
    private boolean isPassMatch(String pass1, String pass2) {
        return pass1.equals(pass2);
    }
}
