package edu.gatech.water_g40.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Provider;
import java.security.acl.Permission;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.Model.Report;
import edu.gatech.water_g40.R;

public class AddSourceActivity extends AppCompatActivity {

    private Spinner typeSpinner;
    private Spinner conditionSpinner;
    private EditText editLat;
    private EditText editLon;

    private double lat = 0;
    private double lon = 0;

    protected Account current;

    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_source);
        Intent intent = getIntent();
        current = intent.getParcelableExtra("account_logged_in");
        currentId = intent.getIntExtra("last_id", 5);
        System.out.println(currentId);

        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        conditionSpinner = (Spinner) findViewById(R.id.condition_spinner);
        editLat = (EditText) findViewById(R.id.lat_text);
        editLon = (EditText) findViewById(R.id.lon_text);

        ArrayAdapter<Report.WaterType> waterTypeArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, Report.legalTypes);
        waterTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(waterTypeArrayAdapter);

        ArrayAdapter<Report.WaterType> conditionAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, Report.legalConditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);



        final Button cancelButton = (Button) findViewById(R.id.add_source_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AddSourceActivity.this, MainMenuActivity.class);
                AddSourceActivity.this.startActivity(myIntent);
                myIntent.putExtra("account_logged_in", current);
            }
        });

        final Button addButton = (Button) findViewById(R.id.add_source_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLat.setError(null);
                editLon.setError(null);
                if (!editLat.getText().toString().matches("^[-+]?\\d+(\\.\\d+)?$")) {
                    editLat.setError("Please enter a valid number");
                    return;
                }
                if (!editLon.getText().toString().matches("^[-+]?\\d+(\\.\\d+)?$")) {
                    editLon.setError("Please enter a valid number");
                    return;
                }
                if (attemptSave()) {
                    System.out.println("Saved");
                } else {
                    System.out.println("Did not save");
                }
                Intent myIntent = new Intent(AddSourceActivity.this, MainMenuActivity.class);
                myIntent.putExtra("account_logged_in", current);
                AddSourceActivity.this.startActivity(myIntent);
            }
        });
    }

    public boolean attemptSave() {

        List<Report> reports = new ArrayList<Report>();
        try {
            FileInputStream fis = openFileInput("mySources");
            ObjectInputStream objectInputStream = new ObjectInputStream(fis);
            reports = (List<Report>) objectInputStream.readObject();
            Report report = new Report(new Date(), current.getUsername(),
                    Double.parseDouble(editLat.getText().toString()),
                    Double.parseDouble(editLon.getText().toString()),
                    (Report.WaterType) typeSpinner.getSelectedItem(),
                    (Report.Condition) conditionSpinner.getSelectedItem(), currentId + 1);
            reports.add(report);
            objectInputStream.close();

            FileOutputStream fileOutputStream = openFileOutput("mySources", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(reports);
            objectOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            reports = new ArrayList<Report>();
            Report report = new Report(new Date(), current.getUsername(), lat, lon,
                    (Report.WaterType) typeSpinner.getSelectedItem(),
                    (Report.Condition) conditionSpinner.getSelectedItem(), currentId + 1);
            reports.add(report);
            try {
                FileOutputStream fileOutputStream = openFileOutput("mySources",
                        Context.MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(reports);
                objectOutputStream.close();
                return true;
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return false;
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            try {
                FileOutputStream fileOutputStream = openFileOutput("mySources",
                        Context.MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(reports);
                objectOutputStream.close();
                return true;
            } catch (Exception ex) {
                e.printStackTrace();
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }


}



