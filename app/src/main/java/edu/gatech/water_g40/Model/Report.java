package edu.gatech.water_g40.Model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jhpinder on 2/28/17.
 */

public class Report implements Parcelable {
    private int date;
    private int time;
    private String username;
    private double lat;
    private double lon;
    private WaterType waterType;
    private Condition condition;

    private enum WaterType {
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


    public Report() {
        this(0, 0, "User", 0, 0, WaterType.BOTTLED, Condition.POTABLE);
    }
    public Report(int date, int time, String username, double lat, double lon, WaterType waterType,
                  Condition condition) {
        this.date = date;
        this.time = time;
        this.username = username;
        this.lat = lat;
        this.lon = lon;
        this.waterType = waterType;
        this.condition = condition;
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
    public int getTime() {
        return time;
    }
    public int getDate() {
        return date;
    }
    public void setDate(int date) {
        this.date = date;
    }
    public void setTime(int time) {
        this.time = time;
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





    private Report(Parcel in) {
        date = in.readInt();
        time = in.readInt();
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
        dest.writeInt(date);
        dest.writeInt(time);
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

