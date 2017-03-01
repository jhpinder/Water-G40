package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.Model.Report;
import edu.gatech.water_g40.R;

/**
 * Created by Vikram Kumar (Archlefirth) on 2/22/2017.
 *
 * The class that represents the main menu of the application
 *
 */
public class MainMenuActivity extends AppCompatActivity {

    private ListView listView;
    private Account current;

    public List<Report> reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Intent intent = getIntent();
        current = (Account) intent.getParcelableExtra("account_logged_in");

        try {
            FileInputStream sourcesFIS = new FileInputStream("mySources");
            ObjectInputStream sourcesOIS = new ObjectInputStream(sourcesFIS);
            reports = (List<Report>) sourcesOIS.readObject();
        } catch (Exception e) {
            reports = new ArrayList<Report>();
        }


        listView = (ListView) findViewById(R.id.main_menu_listview);
        ArrayAdapter<Report> listViewAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, reports);
        listView.setAdapter(listViewAdapter);

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

        final Button submitReportButton = (Button) findViewById(R.id.submit_report_button);
        submitReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent4380 = new Intent(MainMenuActivity.this, AddSourceActivity.class);
                myIntent4380.putExtra("account_logged_in", current);
                MainMenuActivity.this.startActivity(myIntent4380);
            }
        });
    }

}
