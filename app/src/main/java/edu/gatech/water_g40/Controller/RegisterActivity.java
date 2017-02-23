package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import edu.gatech.water_g40.R;

public class RegisterActivity extends AppCompatActivity {

    /* ************************
        Widgets we will need for binding and getting information
     */
    private TextView idField;
    private EditText nameField;
    private Spinner majorSpinner;
    private Spinner standingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Button backButton = (Button) findViewById(R.id.cancel_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(myIntent);
            }
        });
    }
}
