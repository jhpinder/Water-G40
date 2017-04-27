package edu.gatech.water_g40.Controller;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.water_g40.Model.Account;
import edu.gatech.water_g40.R;

/**
 * Created by Vikram Kumar (Archlefirth) on 2/22/2017.
 *
 * This class represents the profile editing screen.
 * Here a user can enter/change their name, home address, email address and user type
 *
 */

public class EditProfileActivity extends AppCompatActivity {

    /* ************************
        Widgets we will need for binding and getting information
     */
    private EditText nameField;
    private EditText emailField;
    private EditText homeField;
    private Spinner titleSpinner;
    private EditText banField;
    private ArrayList<Account> users;
    private ImageView ProfileView1;
    private ImageView ProfileView2;

    boolean cancelClicked = false;

    // The current account to make changes to
    protected Account editAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent intent = getIntent();
        editAccount = (Account) intent.getParcelableExtra("account_logged_in");

        try {
            FileInputStream usersFIS = openFileInput("myUsers");
            ObjectInputStream usersOIS = new ObjectInputStream(usersFIS);
            users = (ArrayList<Account>) usersOIS.readObject();
        } catch (Exception e) {
            users = new ArrayList<Account>();
            e.printStackTrace();
        }



        /**
         * Grab the dialog widgets so we can get info for later
         */
        nameField = (EditText) findViewById(R.id.name_text);
        emailField = (EditText) findViewById(R.id.email_text);
        homeField = (EditText) findViewById(R.id.home_text);
        titleSpinner = (Spinner) findViewById(R.id.title_spinner);
        banField = (EditText) findViewById(R.id.accountToBan);
        ProfileView1 = (ImageView) findViewById(R.id.profile_deer);
        ProfileView1.setImageResource(R.drawable.logo);
        ProfileView1.setClickable(true);
        ProfileView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAccount.setProfilePic("deer");
            }
        });
        ProfileView2 = (ImageView) findViewById(R.id.profile_uni);
        ProfileView2.setImageResource(R.drawable.logo);
        ProfileView2.setClickable(true);
        ProfileView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAccount.setProfilePic("unicorn");
            }
        });

        /*
         * Create a dummy profile to grab the 'legalTitles' enum array
         */

        /*
          Set up the adapter to display the allowable majors in the spinner
         */
        ArrayAdapter<Account.Title> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Account.legalTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleSpinner.setAdapter(adapter);


        final Button cancelButton = (Button) findViewById(R.id.cancel_button_profile);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelClicked = true;
                Intent myIntent = new Intent(EditProfileActivity.this, MainMenuActivity.class);
                myIntent.putExtra("account_logged_in", (Parcelable) editAccount);
                EditProfileActivity.this.startActivity(myIntent);
            }
        });

        final Button saveChangesButton = (Button) findViewById(R.id.save_profile_changes_button);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSaveChanges();
            }
        });

        final Button banButton = (Button) findViewById(R.id.banAccount);
        banButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Account a : users) {
                    if (a.getUsername().equals(banField.getText().toString())) {
                        a.setBanned(true);
                    }
                }
            }
        });

        final Button unBanButton = (Button) findViewById(R.id.unBanAccount);
        unBanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Account a : users) {
                    if (a.getUsername().equals(banField.getText().toString())) {
                        a.setBanned(false);
                    }
                }
            }
        });

        if (editAccount.getTitle().equals(Account.Title.ADMINISTRATOR)) {
            banButton.setVisibility(View.VISIBLE);
            unBanButton.setVisibility(View.VISIBLE);
            banField.setVisibility(View.VISIBLE);
        } else {
            banButton.setVisibility(View.GONE);
            unBanButton.setVisibility(View.GONE);
            banField.setVisibility(View.GONE);
        }

    }

    /**
     * Attempts to save the changes to the profile by the user
     */
    private void attemptSaveChanges() {
        nameField.setError(null);
        emailField.setError(null);
        homeField.setError(null);

        boolean error = false;
        View focusView = null;

        // Store the data the user entered
        String nameString = nameField.getText().toString();
        String emailString = emailField.getText().toString();
        String homeString = homeField.getText().toString();
        Account.Title userType = (Account.Title) titleSpinner.getSelectedItem();


        if (!cancelClicked) {
            if (nameString.length() == 0) {
                nameField.setError("Please enter a name");
                focusView = nameField;
                error = true;
            } else if (!validName(nameString)) {
                nameField.setError("Please enter a valid name");
                focusView = nameField;
                error = true;
            }

            if (homeString.length() == 0) {
                homeField.setError("Please enter a home address");
                focusView = homeField;
                error = true;
            } else if (!validAddress(homeString)) {
                homeField.setError("Please enter a valid address");
                focusView = nameField;
                error = true;
            }

            if (emailString.length() == 0) {
                emailField.setError("Please enter an email");
                focusView = emailField;
                error = true;
            }
            if (!validEmail(emailString)) {
                emailField.setError("Please enter a valid email");
                focusView = emailField;
                error = true;
            }
        }

        if (!cancelClicked) {
            if (error) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                editAccount.setName(nameString);
                editAccount.setEmailAddress(emailString);
                editAccount.setHomeAddress(homeString);
                editAccount.setTitle(userType);
                List<Account> users = new ArrayList<Account>();
                try {
                    FileInputStream fis = openFileInput("myUsers");
                    ObjectInputStream objectInputStream = new ObjectInputStream(fis);
                    users = (List<Account>) objectInputStream.readObject();
                    users.remove(editAccount);
                    users.add(editAccount);
                    objectInputStream.close();

                    FileOutputStream fileOutputStream = openFileOutput("myUsers", Context.MODE_PRIVATE);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(users);
                    objectOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    users = new ArrayList<Account>();
                    users.remove(editAccount);
                    users.add(editAccount);
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("myUsers",
                                Context.MODE_PRIVATE);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(users);
                        objectOutputStream.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (IOException e) {
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("myUsers",
                                Context.MODE_PRIVATE);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(users);
                        objectOutputStream.close();
                    } catch (Exception ex) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Intent myIntent = new Intent(EditProfileActivity.this, MainMenuActivity.class);
                myIntent.putExtra("account_logged_in", (Parcelable) editAccount);
                EditProfileActivity.this.startActivity(myIntent);
            }
        } else {
            /*
            * The user clicked the cancel button during editing
            * so return to the main menu
            */
            Intent myIntent = new Intent(EditProfileActivity.this, MainMenuActivity.class);
            myIntent.putExtra("account_logged_in", (Parcelable) editAccount);
            EditProfileActivity.this.startActivity(myIntent);
        }

    }

    /*
     * checks if a name is in the valid format
     *
     * @param email  the email address to verify
     */
    //TODO: Implement actual validation criteria
    private boolean validName(String name) {
        return (name.length() > 0);
    }

    /*
     * checks if a home address is in the valid format
     *
     * @param email  the email address to verify
     */
    //TODO: Implement actual validation criteria
    private boolean validAddress(String home) { return home.length() > 0; }

    /*
     * checks if an email is in the valid format
     *
     * @param email  the email address to verify
     */
    private boolean validEmail(String email) {
        return !(email == null || TextUtils.isEmpty(email)) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
