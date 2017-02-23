package edu.gatech.water_g40.Model;

import android.os.Parcel;
import android.os.Parcelable;

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

public class Account implements Parcelable{
    
    private String username;
    private String password;


    /* **********************
     * Getters and setters
     */
    public String getusername() { return username; }
    public void setusername(String username) { this.username = username; }

    public String getPassword() {return password; }
    public void setPassword(String password) { this.password = password; }

    /**
     * Make a new Account (2-parameter default)
     * @param username      the Account's username
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
        return username + " " + password;
    }


    /* *********************************
     * These methods are required by the parcelable interface
     *
     */

    private Account(Parcel in) {
        username = in.readString();
        password = in.readString();
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
