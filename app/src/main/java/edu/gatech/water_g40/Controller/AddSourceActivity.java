package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.Model.Report;
import edu.gatech.water_g40.R;

public class AddSourceActivity extends AppCompatActivity {

    private Spinner typeSpinner;
    private Spinner conditionSpinner;

    protected Account current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_source);
        Intent intent = getIntent();
        current = (Account) intent.getParcelableExtra("account_logged_in");

        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        conditionSpinner = (Spinner) findViewById(R.id.condition_spinner);

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
                attemptSave();
                Intent myIntent = new Intent(AddSourceActivity.this, MainMenuActivity.class);
                AddSourceActivity.this.startActivity(myIntent);
                myIntent.putExtra("account_logged_in", current);
            }
        });
    }

    public boolean attemptSave() {
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyyHHmmss");
        Date date = new Date();

        Report report = new Report();
        List<Report> reports;
        try {
            FileInputStream fis = new FileInputStream("sources");
            ObjectInputStream objectInputStream = new ObjectInputStream(fis);
            reports = (List<Report>) objectInputStream.readObject();
            reports.add(report);
            objectInputStream.close();

            FileOutputStream fileOutputStream = new FileOutputStream("sources");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(reports);
            objectOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            reports = new ArrayList<Report>();
            reports.add(report);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("data");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(reports);
                objectOutputStream.close();
            } catch (FileNotFoundException ex) {
                return false;
            } catch (IOException ex) {
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }

    }


}



