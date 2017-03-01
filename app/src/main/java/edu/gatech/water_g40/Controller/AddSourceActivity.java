package edu.gatech.water_g40.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.R;

public class AddSourceActivity extends AppCompatActivity {

    protected Account current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_source);
        Intent intent = getIntent();
        current = (Account) intent.getParcelableExtra("account_logged_in");


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
                Intent myIntent = new Intent(AddSourceActivity.this, MainMenuActivity.class);
                AddSourceActivity.this.startActivity(myIntent);
                myIntent.putExtra("account_logged_in", current);
            }
        });


    }


}
