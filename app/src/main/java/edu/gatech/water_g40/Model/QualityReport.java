package edu.gatech.water_g40.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by jhpinder on 3/9/17.
 */

public class QualityReport implements Parcelable, Serializable{

    private Date date;
    private int reportNumber;
    private String name;
    private double lat;
    private double lon;
    private QCondition qCondition;
    private double virusPPM;
    private double contaminantPPM;



    /**
     * Enum for water condition
     */
    public enum QCondition {
        SAFE("Safe"),
        TREATABLE("Treatable"),
        UNSAFE("Unsafe");
        private String name;
        QCondition(String name) {
            this.name = name;
        }
        public String toString() {
            return name;
        }
    }

    public static List<QCondition> legalQConditions = Arrays.asList(QCondition.values());

    /**
     * No arg constructor, do not use this
     */
    public QualityReport() {
        this(new Date(), 0, "default", 0, 0, QCondition.SAFE, 0, 0);
    }

    /**
     * 8 arg constructor, please only use this.
     */
    public QualityReport(Date date, int reportNumber, String name, double lat, double lon,
                         QCondition qCondition, double virusPPM, double contaminantPPM) {
        this.date = date;
        this.reportNumber = reportNumber;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.qCondition = qCondition;
        this.virusPPM = virusPPM;
        this.contaminantPPM = contaminantPPM;
    }

    /**
     * toString method for QualityReport
     * @return toString with username of who submitted it at the end
     */
    @Override
    public String toString() {
        return "Quality report submitted by " + name;
    }


    protected QualityReport(Parcel in) {
        date = (Date) in.readSerializable();
        reportNumber = in.readInt();
        name = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        qCondition = (QCondition) in.readSerializable();
        virusPPM = in.readDouble();
        contaminantPPM = in.readDouble();
    }

    public static final Creator<QualityReport> CREATOR = new Creator<QualityReport>() {
        @Override
        public QualityReport createFromParcel(Parcel in) {
            return new QualityReport(in);
        }

        @Override
        public QualityReport[] newArray(int size) {
            return new QualityReport[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(date);
        dest.writeInt(reportNumber);
        dest.writeString(name);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeSerializable(qCondition);
        dest.writeDouble(virusPPM);
        dest.writeDouble(contaminantPPM);
    }

}
