package edu.gatech.water_g40.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.Model.Report;
import edu.gatech.water_g40.R;

public class ViewSourceActivity extends AppCompatActivity implements OnMapReadyCallback {
    protected Account current;
    protected Report report;

    private TextView report_date_time;
    private TextView report_number;
    private TextView report_username;
    private TextView report_lat_lon;
    private TextView report_type;
    private TextView report_condition;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_source);
        Intent intent = getIntent();
        report = (Report) intent.getParcelableExtra("current_report");
        current = (Account) intent.getParcelableExtra("account_logged_in");

        report_date_time = (TextView) findViewById(R.id.report_date_time);
        report_number = (TextView) findViewById(R.id.report_number);
        report_username = (TextView) findViewById(R.id.report_username);
        report_lat_lon = (TextView) findViewById(R.id.report_lat_lon);
        report_type = (TextView) findViewById(R.id.report_type);
        report_condition = (TextView) findViewById(R.id.report_condition);

        String dateTime = "Date/Time: " + report.getDateString();
        String repNum = "Report # " + Integer.toString(report.getReportNum());
        String user = "Reported by: " + report.getUsername();
        String latLon = "Lat: " + Double.toString(report.getLat()) + ", Lon: " + Double.toString(report.getLon());
        String type = "Water Type: " + report.getWaterType().toString();
        String condition = "Water Condition: " + report.getCondition().toString();

        report_date_time.setText(dateTime);
        report_number.setText(repNum);
        report_username.setText(user);
        report_lat_lon.setText(latLon);
        report_type.setText(type);
        report_condition.setText(condition);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.view_source_map);
        mapFragment.getMapAsync(this);


        final Button viewSourceBack = (Button) findViewById(R.id.view_source_back);
        viewSourceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ViewSourceActivity.this, MainMenuActivity.class);
                myIntent.putExtra("account_logged_in", current);
                ViewSourceActivity.this.startActivity(myIntent);
            }
        });

    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLon = new LatLng(report.getLat(), report.getLon());
        map.addMarker(new MarkerOptions().position(latLon));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLon, 3));
    }
}
