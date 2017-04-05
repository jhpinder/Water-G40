package edu.gatech.water_g40.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by jhpinder on 2/28/17.
 */

public class Report implements Parcelable, Serializable {
    private Date date;
    private String username;
    private double lat;
    private double lon;
    private WaterType waterType;
    private Condition condition;
    private int reportNum;

    /**
     * Enum for water type
     */
    public enum WaterType {
        BOTTLED("Bottled"),
        WELL("Well"),
        STREAM("Stream"),
        LAKE("Lake"),
        SPRING("Spring"),
        OTHER("Other");

        private String name;
        WaterType(String name) { this.name = name; }
        public String toString() {
            return name;
        }
        }

    /**
     * List for legal values of WaterTypes
     */
    public static List<WaterType> legalTypes = Arrays.asList(WaterType.values());

    /**
     * Enum for water condition
     */
    public enum Condition {
        WASTE("Waste"),
        TREATCLEAR("Treatable-Clear"),
        TREATMUD("Treatable-Muddy"),
        POTABLE("Potable");

        private String name;

        Condition(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    /**
     * List for legal values of Conditions
     */
    public static List<Condition> legalConditions = Arrays.asList(Condition.values());

    /**
     * Default no-param constructor
     */
    public Report() {
        this(new Date(), "User", 0, 0, WaterType.BOTTLED, Condition.POTABLE, 0);
    }

    /**
     * 4 param constructor
     *
     * @param user the name of the submitting user
     * @param waterType the water source's type
     * @param condition the water source's condition
     * @param reportNum the number of the report
     *
     */
    public Report(String user, WaterType waterType, Condition condition, int reportNum) {
        this(new Date(), user, 0, 0, waterType, condition, reportNum);
    }

    /**
     * 8 arg constructor, please only use this.
     *
     * @param date the date of the report
     * @param username the name of the submitting user
     * @param lat the latitude of the report's GPS location
     * @param lon the longitude of the report's GPS location
     * @param waterType the water source's type
     * @param condition the water source's condition
     * @param reportNum he number of the report
     *
     */
    public Report(Date date, String username, double lat, double lon, WaterType waterType,
                  Condition condition, int reportNum) {
        this.date = date;
        this.username = username;
        this.lat = lat;
        this.lon = lon;
        this.waterType = waterType;
        this.condition = condition;
        this.reportNum = reportNum;
    }

    /**
     * toString method for Report
     * @return toString with username of who submitted it at the end
     */
    @Override
    public String toString() {
        return waterType.toString() + " water reported by " + username;
    }

    /* **********************
     * Getters and setters
     */
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Date getDate() {
        return date;
    }
    public String getDateString() {
        return date.toString();
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public WaterType getWaterType() {
        return waterType;
    }
    public void setWaterType(WaterType waterType) {
        this.waterType = waterType;
    }
    public Condition getCondition() {
        return condition;
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public int getReportNum() {
        return reportNum;
    }
    public int getYear() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY");
        return Integer.parseInt(dateFormat.format(date));
    }
    public int getMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return Integer.parseInt(dateFormat.format(date));
    }

    /**
     * Parcelable requirement
     * @param in Parcel to unpack
     */
    private Report(Parcel in) {
        date = (Date) in.readSerializable();
        username = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        waterType = (WaterType) in.readSerializable();
        condition = (Condition) in.readSerializable();
        reportNum = in.readInt();
    }

    /**
     * Method required for interface
     * @return 0 default
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcelable requirement
     * @param dest the Parcel in which the object should be written.
     * @param flags additional flag about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(date);
        dest.writeString(username);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeSerializable(waterType);
        dest.writeSerializable(condition);
        dest.writeInt(reportNum);
    }

    /*
     * Initializes and packages the reports array into a parcel
     */
    public static final Parcelable.Creator<Report> CREATOR
            = new Parcelable.Creator<Report>() {
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}

