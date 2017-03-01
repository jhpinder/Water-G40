package edu.gatech.water_g40.Model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by jhpinder on 2/28/17.
 */

public class Report implements Parcelable {
    private Date date;
    private String username;
    private double lat;
    private double lon;
    private WaterType waterType;
    private Condition condition;
    private static int reportNum = 0;

    public enum WaterType {
        BOTTLED("Bottled"),
        WELL("Well"),
        STREAM("Stream"),
        LAKE("Lake"),
        SPRING("Spring"),
        OTHER("Other");

        private String name;
        private WaterType(String name) { this.name = name; }
        public String toString() {
            return name;
        }
        }
    public static List<WaterType> legalTypes = Arrays.asList(WaterType.values());

    private enum Condition {
        WASTE("Waste"),
        TREATCLEAR("Treatable-Clear"),
        TREATMUD("Treatable-Muddy"),
        POTABLE("Potable");

        private String name;

        private Condition(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public static List<Condition> legalConditions = Arrays.asList(Condition.values());

    public Report() {
        this(new Date(), "User", 0, 0, WaterType.BOTTLED, Condition.POTABLE);
    }
    public Report(Date date, String username, double lat, double lon, WaterType waterType,
                  Condition condition) {
        this.date = date;
        this.username = username;
        this.lat = lat;
        this.lon = lon;
        this.waterType = waterType;
        this.condition = condition;
        reportNum += reportNum;
    }

    public String toString() {
        return waterType.toString() + " water reported by " + username;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Date getDate() {
        return date;
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





    private Report(Parcel in) {
        date = (Date) in.readSerializable();
        username = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        waterType = WaterType.valueOf(in.readString());
        condition = Condition.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(date);
        dest.writeString(username);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(waterType.toString());
        dest.writeString(condition.toString());
    }

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

