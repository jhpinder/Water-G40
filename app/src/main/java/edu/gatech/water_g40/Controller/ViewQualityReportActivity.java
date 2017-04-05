package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.Model.QualityReport;
import edu.gatech.water_g40.Model.Report;
import edu.gatech.water_g40.R;

public class ViewQualityReportActivity extends AppCompatActivity implements OnMapReadyCallback {
    protected Account current;
    protected QualityReport report;

    private TextView report_date_time;
    private TextView report_number;
    private TextView report_username;
    private TextView report_lat_lon;
    private TextView report_condition;
    private TextView report_virusPPM;
    private TextView report_contPPM;

    private GoogleMap map;
    private String previous;
    private ArrayList reports;

    /*
     * Standard activity method to initialize GUI elements and create the screen
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quality_report);
        Intent intent = getIntent();
        report = (QualityReport) intent.getParcelableExtra("current_report");
        current = (Account) intent.getParcelableExtra("account_logged_in");
        previous = intent.getStringExtra("previous");
        reports = intent.getParcelableArrayListExtra("reports");

        report_date_time = (TextView) findViewById(R.id.qreport_date_time);
        report_number = (TextView) findViewById(R.id.qreport_number);
        report_username = (TextView) findViewById(R.id.qreport_username);
        report_lat_lon = (TextView) findViewById(R.id.qreport_lat_lon);
        report_condition = (TextView) findViewById(R.id.qreport_condition);
        report_virusPPM = (TextView) findViewById(R.id.qreport_virusPPM);
        report_contPPM = (TextView) findViewById(R.id.qreport_contPPM);

        String dateTime = "Date/Time: " + report.getDate().toString();
        String repNum = "Report # " + Integer.toString(report.getReportNumber());
        String user = "Reported by: " + report.getName();
        String latLon = "Lat: " + Double.toString(report.getLat()) + ", Lon: " + Double.toString(report.getLon());
        String condition = "Condition: " + report.getqCondition().toString();
        String vPPM = "Virus PPM: " + Double.toString(report.getVirusPPM());
        String cPPM = "Contaminant PPM: " + Double.toString(report.getContaminantPPM());

        System.out.println(report_date_time == null);
        report_date_time.setText(dateTime);
        report_number.setText(repNum);
        report_username.setText(user);
        report_lat_lon.setText(latLon);
        report_condition.setText(condition);
        report_virusPPM.setText(vPPM);
        report_contPPM.setText(cPPM);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.view_quality_report_map);
        mapFragment.getMapAsync(this);


        final Button viewSourceBack = (Button) findViewById(R.id.view_quality_report_back);
        viewSourceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (previous.equals("main_menu")) {
                    Intent myIntent = new Intent(ViewQualityReportActivity.this, MainMenuActivity.class);
                    myIntent.putExtra("account_logged_in", (Parcelable) current);
                    ViewQualityReportActivity.this.startActivity(myIntent);
                } else if (previous.equals("map_view")) {
                    Intent myIntent = new Intent(ViewQualityReportActivity.this, MapViewActivity.class);
                    myIntent.putExtra("account_logged_in", (Parcelable) current);
                    myIntent.putParcelableArrayListExtra("reports", reports);
                    ViewQualityReportActivity.this.startActivity(myIntent);
                }
            }
        });

    }

    /*
        Initializes the map and readies the Google Map functionality
     */
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLon = new LatLng(report.getLat(), report.getLon());
        map.addMarker(new MarkerOptions().position(latLon));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLon, 3));
    }
}
