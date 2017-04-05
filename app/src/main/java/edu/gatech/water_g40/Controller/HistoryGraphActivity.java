package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.Model.QualityReport;
import edu.gatech.water_g40.R;

public class HistoryGraphActivity extends AppCompatActivity {

    public ArrayList<QualityReport> reports = new ArrayList<QualityReport>();

    final int RADIUSINKM = 500;
    protected Account current;

    private Spinner spinner;
    private EditText editLat;
    private EditText editLon;
    private EditText editYear;

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

        editLat = (EditText) findViewById(R.id.graph_lat);
        editLon = (EditText) findViewById(R.id.graph_lon);
        editYear = (EditText) findViewById(R.id.graph_year);
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
                myIntent.putExtra("account_logged_in", (Parcelable) current);
                HistoryGraphActivity.this.startActivity(myIntent);
            }
        });

        final Button updateButton = (Button) findViewById(R.id.graph_update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editYear.setError(null);
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
                if (!editYear.getText().toString().matches("^[-+]?\\d+(\\.\\d+)?$")) {
                    editYear.setError("Please enter a valid number");
                    return;
                }
                double lat = Double.parseDouble(editLat.getText().toString());
                double lon = Double.parseDouble(editLon.getText().toString());
                int year = Integer.parseInt(editYear.getText().toString());
                boolean virus = spinner.getSelectedItem().equals("Virus");
                Map<Integer, Double> dataPoints = populate(lat, lon, year, virus);
                System.out.println(dataPoints.get(1));
                series = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, dataPoints.get(1)),
                        new DataPoint(1, dataPoints.get(2)),
                        new DataPoint(2, dataPoints.get(3)),
                        new DataPoint(3, dataPoints.get(4)),
                        new DataPoint(4, dataPoints.get(5)),
                        new DataPoint(5, dataPoints.get(6)),
                        new DataPoint(6, dataPoints.get(7)),
                        new DataPoint(7, dataPoints.get(8)),
                        new DataPoint(8, dataPoints.get(9)),
                        new DataPoint(9, dataPoints.get(10)),
                        new DataPoint(10, dataPoints.get(11)),
                        new DataPoint(11, dataPoints.get(12))
                });

                graph.removeAllSeries();
                graph.addSeries(series);
                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                staticLabelsFormatter.setHorizontalLabels(new String[] {"J", "F", "M", "A", "M",
                    "J", "J", "A", "S", "O", "N", "D"});
                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            }

        });


        graph = (GraphView) findViewById(R.id.history_graph);

    }

    private Map<Integer, Double> populate(double lat, double lon, int year, boolean virus) {
        series = new LineGraphSeries<>();
        Map<Integer, Double> dataPoints = new HashMap<>();
        if (reports != null) {
            for (int m = 1; m <= 12; m++) {
                double totalPPM = 0;
                double numReps = 0;
                for (QualityReport r : reports) {
                    if (r != null && r.getYear() == year && m == r.getMonth()
                            && checkIfInRad(lat, lon, r)) {
                        if (virus) {
                            totalPPM += r.getVirusPPM();
                            numReps++;
                        } else {
                            totalPPM += r.getContaminantPPM();
                            numReps++;
                        }
                    }
                }
                if (numReps != 0) {
                    dataPoints.put(m, totalPPM / numReps);
                } else {
                    dataPoints.put(m, 0.0);
                }
            }
        }
        return dataPoints;
    }

    private boolean checkIfInRad(double lat1, double lon1, QualityReport qualityReport) {
        double lat2 = qualityReport.getLat();
        double lon2 = qualityReport.getLon();
        double distanceLat = (lat2 - lat1) * Math.PI / 180;
        double distanceLon = (lon2 - lon1) * Math.PI / 180;
        lat1 = lat1 * Math.PI / 180;
        lat2 = lat2 * Math.PI / 180;
        double a = Math.sin(distanceLat / 2) * Math.sin(distanceLat / 2) + Math.sin(distanceLon / 2)
                * Math.sin(distanceLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.abs(6371 * c) < RADIUSINKM;
    }

}
