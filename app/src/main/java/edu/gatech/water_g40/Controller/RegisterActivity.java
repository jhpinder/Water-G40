package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.R;

import static edu.gatech.water_g40.Controller.LoginActivity.accountHashtable;

public class RegisterActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private LoginActivity.UserLoginTask mAuthTask = null;

    // UI references.
    private EditText userEditField;
    private EditText passEditField;
    private EditText passVerifEditField;

    boolean cancelClicked = false;

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
        backButton.setOnClickListener(new View.OnClickListener() {
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
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        userEditField.setError(null);
        passEditField.setError(null);
        passVerifEditField.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Store the data the user entered
        String userString = userEditField.getText().toString();
        String passString = passEditField.getText().toString();
        String passConfirmString = passVerifEditField.getText().toString();

        if (cancelClicked) {
            // Check for a valid password, if the user entered one.
            if (passString.length() == 0 && !isPasswordValid(passString)) {
                passEditField.setError("The password is invalid");
                focusView = passEditField;
                cancel = true;
            } else if (passConfirmString.length() == 0 && !isPasswordValid(passConfirmString)) {
                passVerifEditField.setError("Please retype your password");
                focusView = passVerifEditField;
                cancel = true;
            } else if (!isPassMatch(passString, passConfirmString)) {
                passVerifEditField.setError("The passwords do not match");
                focusView = passVerifEditField;
                cancel = true;
            }

            //verify the username
            if (userString.length() == 0) {
                userEditField.setError("This field is required");
                focusView = userEditField;
                cancel = true;
            } else if (!isUsernameValid(userString)) {
                userEditField.setError("The username is too short");
                focusView = userEditField;
                cancel = true;
            }


            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // The account will be registered and added to the list of valid accounts
                accountHashtable.put(userString, new Account(userString, passString));
                // The user will now be returned to the login screen so they can login with their new account
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(myIntent);
            }
        } else {
            Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            RegisterActivity.this.startActivity(myIntent);
        }
    }

    /*
    * Checks if the username is valid
    *
    * @param username   the username entered into the text field
     */
    // TODO add actual vaidity requirements
    private boolean isUsernameValid(String username) { return username.length() > 2; }


    /*
    * Checks if the password is valid
    *
    * @param password   the username entered into the text field
     */
    // TODO add actual vaidity requirements
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
