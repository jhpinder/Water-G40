package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.R;

/**
 * Created by Vikram Kumar (Archlefirth) on 2/22/2017.
 *
 * The class that represents the main menu of the application
 *
 */
public class MainMenuActivity extends AppCompatActivity {

    private Account current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Intent intent = getIntent();
        current = (Account) intent.getParcelableExtra("account_logged_in");

        final Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainMenuActivity.this, LoginActivity.class);
                MainMenuActivity.this.startActivity(myIntent);
            }
        });

        final Button editProfileButton = (Button) findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainMenuActivity.this, EditProfileActivity.class);
                myIntent.putExtra("account_logged_in", current);
                MainMenuActivity.this.startActivity(myIntent);
            }
        });
    }

}
