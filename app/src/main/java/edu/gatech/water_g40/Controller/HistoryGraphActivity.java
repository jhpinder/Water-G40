package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.Model.QualityReport;
import edu.gatech.water_g40.R;

public class HistoryGraphActivity extends AppCompatActivity {

    public ArrayList<QualityReport> reports = new ArrayList<QualityReport>();

    protected Account current;

    private Spinner spinner;
    private GraphView graph;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_graph);

        try {
            FileInputStream sourcesFIS = openFileInput("myQReports");
            ObjectInputStream sourcesOIS = new ObjectInputStream(sourcesFIS);
            reports = (ArrayList<QualityReport>) sourcesOIS.readObject();
        } catch (Exception e) {
            reports = new ArrayList<QualityReport>();
            e.printStackTrace();
        }


        Intent intent = getIntent();
        current = (Account) intent.getParcelableExtra("account_logged_in");

        spinner = (Spinner) findViewById(R.id.graph_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Arrays.asList("Virus", "Contaminant"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Button backButton = (Button) findViewById(R.id.graph_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(HistoryGraphActivity.this, MainMenuActivity.class);
                HistoryGraphActivity.this.startActivity(myIntent);
            }
        });

        final Button updateButton = (Button) findViewById(R.id.graph_update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        graph = (GraphView) findViewById(R.id.history_graph);
        graph.addSeries(series);

    }

    private void populate(double lat, double lon, int year, boolean virus, QualityReport report) {
        series = new LineGraphSeries<>();
        if (reports != null) {
            for (int m = 1; m <= 12; m++) {
                int totalPPM = 0;
                int numReps = 0;
                for (QualityReport r : reports) {
                    if (r != null && r.getYear() == year && m == r.getMonth()
                            && checkIfInRad(lat, lon, report)) {
                        if (virus) {
                            totalPPM += r.getVirusPPM();
                            numReps++;
                        } else {
                            totalPPM += r.getContaminantPPM();
                            numReps++;
                        }
                    }
                }
                series.appendData(new DataPoint(m, totalPPM / numReps), false, 12);
            }
        }
    }

    private boolean checkIfInRad(double lat1, double lon1, QualityReport qualityReport) {
        double lat2 = qualityReport.getLat();
        double lon2 = qualityReport.getLon();
        double distanceLat = (lat2 - lat1) * Math.PI / 180;
        double distanceLon = (lon2 - lon1) * Math.PI / 180;
        lat1 = lat1 * Math.PI / 180;
        lat2 = lat2 * Math.PI / 180;

        double a = Math.sin(distanceLat / 2) * Math.sin(distanceLat / 2);
        return false;
    }

}
