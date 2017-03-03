package edu.gatech.water_g40.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vikram Kumar (Archlefirth) on 2/22/2017.
 *
 * Service Provider - allows logins via acting account data
 *
 * We are passing this object in a bundle between intents, so we implement
 * the Parcelable interface.
 *
 */

public class Account implements Parcelable, Serializable {
    
    private String username;
    private String password;
    private String name;
    private String email_address;
    private String home_address;
    private Title title;



    // An enum to hold class standings, each with their own two character attribute
    public enum Title {
        USER("User"),
        WORKER("Worker"),
        MANAGER("Manager"),
        ADMINISTRATOR("Administrator");

        private String name;

        private Title(String name) { this.name = name; }

        public String toString() {
            return name;
        }

    }

    public static List<Title> legalTitles = Arrays.asList(Title.values());


    /* **********************
     * Getters and setters
     */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmailAddress() {return email_address; }
    public void setEmailAddress(String email) { email_address = email; }

    public String getHomeAddress() {return home_address; }
    public void setHomeAddress(String home) { home_address = home; }

    public Title getTitle() {return title; }
    public void setTitle(Title standing) { title = standing; }


    /* **********************
     * Getters and setters
     */
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() {return password; }
    public void setPassword(String password) { this.password = password; }


    /**
     * Make a new Account (2-param default)
     * @param username     the Account's username
     * @param password     the Account's password
     */
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * No param constructor -- DO NOT CALL NORMALLY
     * This constructor only for GUI use in edit/new Account dialog
     */
    public Account() {
        this("enter new username" , "create a password");
    }

    /**
     *
     * @return the display string representation
     */
    @Override
    public String toString() {
        return username + " " + password + " " + name + " " + email_address + " " + home_address + " " + title;
    }


    /* *********************************
     * These methods are required by the parcelable interface
     *
     */

    private Account(Parcel in) {
        username = in.readString();
        password = in.readString();
        name = in.readString();
        email_address = in.readString();
        home_address = in.readString();
        title = (Title) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /* *************************
       If you add new instance vars to Account, you will need to add them to the write
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(email_address);
        dest.writeString(home_address);
        dest.writeSerializable(title);
    }

    public static final Parcelable.Creator<Account> CREATOR
            = new Parcelable.Creator<Account>() {
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
