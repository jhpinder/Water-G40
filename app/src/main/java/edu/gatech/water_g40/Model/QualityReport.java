package edu.gatech.water_g40.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by jhpinder on 3/9/17.
 * Quality report class for anyone but users to use
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

    /**
     * List for legal values of qconditions
     */
    public static List<QCondition> legalQConditions = Arrays.asList(QCondition.values());


    /**
     * 8 arg constructor, please only use this.
     *
     * @param date the date of the report
     * @param reportNumber the index of the report
     * @param name the report's name
     * @param lat the latitude of the report's GPS location
     * @param lon the longitude of the report's GPS location
     * @param qCondition the quality condition of the water source
     * @param virusPPM PPM value for the virus
     * @param contaminantPPM PPM value for the contaminant
     *
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


    /* **********************
     * Getters and setters
     */
    public String getName() { return name; }
    public Date getDate() { return date; }
    public int getReportNumber() { return reportNumber; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public QCondition getqCondition() { return qCondition; }
    public double getVirusPPM() { return virusPPM; }
    public double getContaminantPPM() { return contaminantPPM; }
    public int getYear() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY");
        return Integer.parseInt(dateFormat.format(date));
    }
    public int getMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return Integer.parseInt(dateFormat.format(date));
    }

    /**
     * toString method for QualityReport
     * @return toString with username of who submitted it at the end
     */
    @Override
    public String toString() {
        return "Quality report submitted by " + name;
    }

    /**
     * Parcelable requirement
     * @param in Parcel to unpack
     */
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

    /**
     * Parcels the report and initializes the array of reports
     * Method required for interface
     */
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

    /**
     * Method required for interface
     * @return 0 default
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Required for parcelable
     * @param dest Parcel to write to
     * @param flags flags, none used
     */
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
