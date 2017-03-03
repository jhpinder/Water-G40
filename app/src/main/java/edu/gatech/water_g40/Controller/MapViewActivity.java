package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.os.ParcelableCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.Model.Report;
import edu.gatech.water_g40.R;

public class MapViewActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap mMap;

    private Map<Marker, Report> h = new HashMap<>();

    protected Account current;
    protected ArrayList<? extends Parcelable> reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        Intent intent = getIntent();
        current = intent.getParcelableExtra("account_logged_in");
        reports = intent.getParcelableArrayListExtra("reports");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.view_map);
        mapFragment.getMapAsync(this);

        final Button mapViewBack = (Button) findViewById(R.id.map_view_back);
        mapViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MapViewActivity.this, MainMenuActivity.class);
                myIntent.putExtra("account_logged_in", (Parcelable) current);
                myIntent.putParcelableArrayListExtra("reports", reports);
                MapViewActivity.this.startActivity(myIntent);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng l = new LatLng(0,0);
        for (int i = 0; i < reports.size(); i++) {
            Report r = (Report) reports.get(i);
            l = new LatLng(r.getLat(), r.getLon());
            Marker mark = mMap.addMarker(new MarkerOptions().position(l).title(r.getWaterType().toString()));
            h.put(mark, r);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (h != null && h.get(marker) != null) {
            Intent myIntent = new Intent(MapViewActivity.this, ViewSourceActivity.class);
            myIntent.putExtra("previous", "map_view");
            myIntent.putExtra("account_logged_in", (Parcelable) current);
            myIntent.putExtra("current_report", (Parcelable) h.get(marker));
            myIntent.putExtra("reports", reports);
            MapViewActivity.this.startActivity(myIntent);
        }
        return true;
    }
}
