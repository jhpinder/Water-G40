package edu.gatech.water_g40.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Vikram Kumar (Archlefirth) on 2/22/2017.
 *
 * Information Holder - represents a single Profile in model
 *
 * We are passing this object in a bundle between intents, so we implement
 * the Parcelable interface.
 *
 */

public class Profile implements Parcelable {


    // An enum to hold class standings, each with their own two character attribute
    public enum Title {
        USER("User"),
        WORKER("Worker"),
        MANAGER("Manager"),
        ADMINISTRATOR("Administrator");

        private String type;

        private Title(String name) { type = name; }

        public String toString() {
            return type;
        }

    }

    public static List<Title> legalTitles = Arrays.asList(Title.values());
    
    private String name;
    private String email_address;
    private String home_address;
    private Title title;

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

//    public static int findPosition(String code) {
//        int i = 0;
//        while (i < legalMajors.size()) {
//            if (code.equals(legalMajors.get(i))) return i;
//            ++i;
//        }
//        return 0;
//    }


    /**
     * Make a new Profile (3-parameter default)
     * @param name      the Profile's name
     * @param email     the Profile's email address
     * @param home     the Profile's home address
     */
    public Profile(String name, String email, String home) {
        this(name, email, home, Title.USER);

    }

    /**
     * Make a new Profile (4-parameter)
     * @param name      the Profile's name
     * @param email     the Profile's email address
     * @param home      the Profile's home address
     * @param title     the Profile's title
     */
    public Profile(String name, String email, String home, Title title) {
        this.name = name;
        email_address= email;
        home_address = home;
        this.title = title;
    }

    /**
     * No param constructor -- DO NOT CALL NORMALLY
     * This constructor only for GUI use in edit/new Profile dialog
     */
    public Profile() {
        this("enter new name" , "NA", "NA", null);
    }

    /**
     *
     * @return the display string representation
     */
    @Override
    public String toString() {
        return name + " " + email_address + " " + home_address + " " + title;
    }


    /* *********************************
     * These methods are required by the parcelable interface
     *
     */

    private Profile(Parcel in) {
        name = in.readString();
        email_address = in.readString();
        home_address = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /* *************************
       If you add new instance vars to Profile, you will need to add them to the write
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email_address);
        dest.writeString(home_address);

    }

    public static final Parcelable.Creator<Profile> CREATOR
            = new Parcelable.Creator<Profile>() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}
