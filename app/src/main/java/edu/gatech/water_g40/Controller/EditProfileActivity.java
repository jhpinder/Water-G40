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
          Set up the adapter to display the allowable majors in the spinner
         */
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Profile.legalTitles);
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

        boolean cancel = false;
        View focusView = null;

        // Store the data the user entered
        String nameString = nameField.getText().toString();
        String emailString = emailField.getText().toString();
        String homeString = homeField.getText().toString();




        if (cancelClicked == false) {
            if (nameString == null) {
                nameField.setError("Please enter a name");
                focusView = nameField;
                cancel = true;
            }
            if (!validEmail(emailString)) {
                emailField.setError("Please enter a valid email");
                focusView = emailField;
                cancel = true;
            }
            if (homeString == null) {
                homeField.setError("Please enter a valid address");
                focusView = homeField;
                cancel = true;
            }

            if (cancel == false) {
                // The user has never entered their profile data before
                if (editAccount.getProfile() == null) {
                    editAccount.setProfile(new Profile(
                            nameString,
                            emailString,
                            homeString,
                            (Profile.Title) titleSpinner.getSelectedItem()
                    ));
                } else {
                    // The user is updating their current profile information
                    Profile currentProfile = editAccount.getProfile();
                    currentProfile.setName(nameString);
                    currentProfile.setEmailAddress(emailString);
                    currentProfile.setHomeAddress(homeString);
                    currentProfile.setTitle((Profile.Title) titleSpinner.getSelectedItem());
                    editAccount.setProfile(currentProfile);
                }
                Intent myIntent = new Intent(EditProfileActivity.this, MainMenuActivity.class);
                EditProfileActivity.this.startActivity(myIntent);
            } else {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            }
        } else {
            Intent myIntent = new Intent(EditProfileActivity.this, MainMenuActivity.class);
            EditProfileActivity.this.startActivity(myIntent);
        }
    }

    /*
     * checks if an email is in the valid format
     *
     * @param email  the email address to verify
     */
    private boolean validEmail(String email) {
        return !(email == null || TextUtils.isEmpty(email)) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
