package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.Model.Profile;
import edu.gatech.water_g40.R;

/**
 * Created by Vikram Kumar (Archlefirth) on 2/22/2017.
 *
 * This class represents the profile editing screen.
 * Here a user can enter/change their name, home address, email address and user type
 *
 */

public class EditProfileActivity extends AppCompatActivity {

    /* ************************
        Widgets we will need for binding and getting information
     */
    private EditText nameField;
    private EditText emailField;
    private EditText homeField;
    private Spinner titleSpinner;

    boolean cancelClicked = false;

    // The current account to make changes to
    protected Account editAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent intent = getIntent();
        editAccount = (Account) intent.getSerializableExtra("account_logged_in");

        /**
         * Grab the dialog widgets so we can get info for later
         */
        nameField = (EditText) findViewById(R.id.name_text);
        emailField = (EditText) findViewById(R.id.email_text);
        homeField = (EditText) findViewById(R.id.home_text);
        titleSpinner = (Spinner) findViewById(R.id.title_spinner);

        /*
         * Create a dummy profile to grab the 'legalTitles' enum array
         */
        Profile dummy = new Profile("enter new name" , "test@test.com", "NA");

        /*
          Set up the adapter to display the allowable majors in the spinner
         */
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dummy.legalTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleSpinner.setAdapter(adapter);

        final Button cancelButton = (Button) findViewById(R.id.cancel_button_profile);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelClicked = true;
                Intent myIntent = new Intent(EditProfileActivity.this, MainMenuActivity.class);
                EditProfileActivity.this.startActivity(myIntent);
            }
        });

        final Button saveChangesButton = (Button) findViewById(R.id.save_profile_changes_button);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSaveChanges();
            }
        });

    }

    /**
     * Attempts to save the changes to the profile by the user
     */
    private void attemptSaveChanges() {
        nameField.setError(null);
        emailField.setError(null);
        homeField.setError(null);

        boolean error = false;
        View focusView = null;

        // Store the data the user entered
        String nameString = nameField.getText().toString();
        String emailString = emailField.getText().toString();
        String homeString = homeField.getText().toString();
        Profile.Title userType = (Profile.Title) titleSpinner.getSelectedItem();


        if (!cancelClicked) {
            if (nameString.length() == 0) {
                nameField.setError("Please enter a name");
                focusView = nameField;
                error = true;
            } else if (!validName(nameString)) {
                nameField.setError("Please enter a valid name");
                focusView = nameField;
                error = true;
            }

            if (homeString.length() == 0) {
                homeField.setError("Please enter a home address");
                focusView = homeField;
                error = true;
            } else if (!validAddress(homeString)) {
                homeField.setError("Please enter a valid address");
                focusView = nameField;
                error = true;
            }

            if (emailString.length() == 0) {
                emailField.setError("Please enter an email");
                focusView = emailField;
                error = true;
            }
            if (!validEmail(emailString)) {
                emailField.setError("Please enter a valid email");
                focusView = emailField;
                error = true;
            }
        }

        if (!cancelClicked) {
            if (error) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                if (editAccount.getProfile() == null) {
                    // The user has never entered any profile info
                    editAccount.setProfile(new Profile(
                            nameString,
                            emailString,
                            homeString,
                            userType
                    ));
                } else {
                    // The user is updating their current profile information
                    Profile currentProfile = editAccount.getProfile();
                    currentProfile.setName(nameString);
                    currentProfile.setEmailAddress(emailString);
                    currentProfile.setHomeAddress(homeString);
                    currentProfile.setTitle(userType);
                    editAccount.setProfile(currentProfile);
                }
            }
        } else {
            /*
            * The user clicked the cancel button during editing
            * so return to the main menu
            */
            Intent myIntent = new Intent(EditProfileActivity.this, MainMenuActivity.class);
            EditProfileActivity.this.startActivity(myIntent);
        }
    }

    /*
     * checks if a name is in the valid format
     *
     * @param email  the email address to verify
     */
    //TODO: Implement actual validation criteria
    private boolean validName(String name) {
        return (name.length() > 0);
    }

    /*
     * checks if a home address is in the valid format
     *
     * @param email  the email address to verify
     */
    //TODO: Implement actual validation criteria
    private boolean validAddress(String home) { return home.length() > 0; }

    /*
     * checks if an email is in the valid format
     *
     * @param email  the email address to verify
     */
    private boolean validEmail(String email) {
        return !(email == null || TextUtils.isEmpty(email)) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
