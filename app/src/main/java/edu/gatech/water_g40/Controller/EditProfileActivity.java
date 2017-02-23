package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent intent = getIntent();

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

    }

}
