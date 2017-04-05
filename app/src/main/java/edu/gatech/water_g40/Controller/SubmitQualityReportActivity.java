package edu.gatech.water_g40.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import edu.gatech.water_g40.Model.QualityReport;
import edu.gatech.water_g40.Model.Report;
import edu.gatech.water_g40.R;

public class SubmitQualityReportActivity extends AppCompatActivity implements OnMapReadyCallback {

    protected Account current;
    protected int currentId;

    private Spinner conditionSpinner;
    private GoogleMap map;
    private EditText editVirusPPM;
    private EditText editConPPM;
    private double lat = 0;
    private double lon = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_quality_report);
        Intent intent = getIntent();
        current = intent.getParcelableExtra("account_logged_in");
        currentId = intent.getIntExtra("currentId", 0);

        conditionSpinner = (Spinner) findViewById(R.id.quality_report_spinner);
        editVirusPPM = (EditText) findViewById(R.id.editVirusPPM);
        editConPPM = (EditText) findViewById(R.id.editConPPM);



        ArrayAdapter<Report.WaterType> conditionAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, QualityReport.legalQConditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.add_q_fragment);
        mapFragment.getMapAsync(this);

        final Button addButton = (Button) findViewById(R.id.add_qreport_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editConPPM.setError(null);
                editVirusPPM.setError(null);
                if (!editConPPM.getText().toString().matches("^[-+]?\\d+(\\.\\d+)?$")) {
                    editConPPM.setError("Please enter a valid number");
                    return;
                }
                if (!editVirusPPM.getText().toString().matches("^[-+]?\\d+(\\.\\d+)?$")) {
                    editVirusPPM.setError("Please enter a valid number");
                    return;
                }

                if (attemptSave()) {
                    System.out.println("Saved");
                } else {
                    System.out.println("Did not save");
                }
                Intent myIntent = new Intent(SubmitQualityReportActivity.this,
                        MainMenuActivity.class);
                myIntent.putExtra("account_logged_in",(Parcelable) current);
                SubmitQualityReportActivity.this.startActivity(myIntent);
            }
        });

        final Button cancelButton = (Button) findViewById(R.id.add_qreport_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SubmitQualityReportActivity.this, MainMenuActivity.class);
                SubmitQualityReportActivity.this.startActivity(myIntent);
                myIntent.putExtra("account_logged_in", (Parcelable) current);
            }
        });
    }



    public boolean attemptSave() {

        List<QualityReport> qReports = new ArrayList<QualityReport>();
        try {
            FileInputStream fis = openFileInput("myQReports");
            ObjectInputStream objectInputStream = new ObjectInputStream(fis);
            qReports = (List<QualityReport>) objectInputStream.readObject();
            QualityReport report = new QualityReport(new Date(), currentId + 1, current.getUsername(),
                    lat, lon, (QualityReport.QCondition) conditionSpinner.getSelectedItem(),
                    Double.parseDouble(editVirusPPM.getText().toString()),
                    Double.parseDouble(editConPPM.getText().toString()));
            qReports.add(report);
            objectInputStream.close();

            FileOutputStream fileOutputStream = openFileOutput("myQReports", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(qReports);
            objectOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            qReports = new ArrayList<QualityReport>();
            QualityReport report = new QualityReport(new Date(), currentId + 1, current.getUsername(),
                    lat, lon, (QualityReport.QCondition) conditionSpinner.getSelectedItem(),
                    Double.parseDouble(editVirusPPM.getText().toString()),
                    Double.parseDouble(editConPPM.getText().toString()));
            qReports.add(report);
            try {
                FileOutputStream fileOutputStream = openFileOutput("myQReports",
                        Context.MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(qReports);
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
                FileOutputStream fileOutputStream = openFileOutput("myQReports",
                        Context.MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(qReports);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                lat = point.latitude;
                lon = point.longitude;
                map.clear();
                map.addMarker(new MarkerOptions().position(point));
            }
        });
    }
}
