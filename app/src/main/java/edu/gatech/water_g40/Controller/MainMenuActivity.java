package edu.gatech.water_g40.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    protected Account current;


    public ArrayList<? extends Parcelable> reports = new ArrayList<Report>();
    public ArrayList<? extends Parcelable> getReports() {
        return reports;
    }

    public ArrayList<? extends Parcelable> users = new ArrayList<Account>();
    public ArrayList<? extends Parcelable> getUsers() {
        return users;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Intent intent = getIntent();
        current = (Account) intent.getParcelableExtra("account_logged_in");
//        try {
//            FileOutputStream fileOutputStream = openFileOutput("mySources",
//                    Context.MODE_PRIVATE);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//            objectOutputStream.writeObject(reports);
//            objectOutputStream.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


        try {
            FileInputStream sourcesFIS = openFileInput("mySources");
            ObjectInputStream sourcesOIS = new ObjectInputStream(sourcesFIS);
            reports = (ArrayList<Report>) sourcesOIS.readObject();
        } catch (Exception e) {
            reports = new ArrayList<Report>();
            e.printStackTrace();
        }

        try {
            FileInputStream usersFIS = openFileInput("myUsers");
            ObjectInputStream usersOIS = new ObjectInputStream(usersFIS);
            users = (ArrayList<Account>) usersOIS.readObject();
        } catch (Exception e) {
            users = new ArrayList<Account>();
            e.printStackTrace();
        }
        //reports.add(new Report());

        listView = (ListView) findViewById(R.id.main_menu_listview);
        final ArrayAdapter<Report> listViewAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, reports);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(MainMenuActivity.this, ViewSourceActivity.class);
                myIntent.putExtra("current_report", (Parcelable) listView.getItemAtPosition(position));
                myIntent.putExtra("account_logged_in", (Parcelable) current);
                myIntent.putExtra("previous", "main_menu");
                MainMenuActivity.this.startActivity(myIntent);
            }
        });

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
                myIntent.putExtra("account_logged_in", (Parcelable) current);
                MainMenuActivity.this.startActivity(myIntent);
            }
        });

        final Button submitReportButton = (Button) findViewById(R.id.submit_report_button);
        submitReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent4380 = new Intent(MainMenuActivity.this, AddSourceActivity.class);
                myIntent4380.putExtra("account_logged_in", (Parcelable) current);
                myIntent4380.putExtra("last_id", reports.size());
                MainMenuActivity.this.startActivity(myIntent4380);
            }
        });

        final Button deleteAllButton = (Button) findViewById(R.id.delete_all);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileOutputStream fileOutputStream = openFileOutput("mySources",
                            Context.MODE_PRIVATE);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    reports = new ArrayList<Report>();
                    objectOutputStream.writeObject(reports);
                    objectOutputStream.close();
                    listViewAdapter.clear();
                    listViewAdapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        final Button mapViewButton = (Button) findViewById(R.id.main_menu_map);
        mapViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenuActivity.this, MapViewActivity.class);
                myIntent.putExtra("account_logged_in", (Parcelable) current);
                myIntent.putParcelableArrayListExtra("reports", reports);
                MainMenuActivity.this.startActivity(myIntent);
            }
        });
    }

}
