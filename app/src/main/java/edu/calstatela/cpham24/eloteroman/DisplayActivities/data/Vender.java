package edu.calstatela.cpham24.eloteroman.DisplayActivities.data;

import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Johnson on 7/30/2017.
 */

public class Vender implements Comparable {

    private String name;
    private String desc;
    private String workHour;
    private boolean work;
    private double lat;
    private double lon;
    private double userlat;
    private double userlon;


    public Vender(String na, String de, String wo, String ye, double lat, double lon, double userlat, double userlon) {
        this.name = na;
        this.desc = de;
        this.workHour = wo;
        this.work = Boolean.parseBoolean(ye);
        this.lat = lat;
        this.lon = lon;
        this.userlat = userlat;
        this.userlon = userlon;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWorkHour() {
        return workHour;
    }

    public void setWorkHour(String workHour) {
        this.workHour = workHour;
    }

    public boolean getWork() {return work; }

    public void setWork(boolean ye) {this.work = ye; }

    public double getLat() { return lat; }

    public void setLat(int lat) {this.lat = lat;}

    public double getLon() { return lon; }

    public void setLon(int lon) {this.lon = lon;}

    public double getUserlat() {
        return userlat;
    }

    public void setUserlat(double userlat) {
        this.userlat = userlat;
    }

    public double getUserlon() {
        return userlon;
    }

    public void setUserlon(double userlon) {
        this.userlon = userlon;
    }

    //Code to determine distance with latitude and longitude points provided
    //by this site, for convenience
    //http://www.geodatasource.com/developers/java
    @Override
    public int compareTo(Object o) {

        double theta = this.getUserlon() - this.getLon();
        double dist = Math.sin(deg2rad(this.getUserlat())) * Math.sin(deg2rad(this.getLat())) +
                Math.cos(deg2rad(this.getUserlat())) * Math.cos(deg2rad(this.getLat())) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        Log.d(TAG, " woah " + this.getName() + " iii " + dist);

        double theta2 = ((Vender)o).getUserlon() - ((Vender)o).getLon();
        double dist2 = Math.sin(deg2rad(((Vender)o).getUserlat())) * Math.sin(deg2rad(((Vender)o).getLat())) +
                Math.cos(deg2rad(((Vender)o).getUserlat())) * Math.cos(deg2rad(((Vender)o).getLat())) * Math.cos(deg2rad(theta2));
        dist2 = Math.acos(dist2);
        dist2 = rad2deg(dist2);
        dist2 = dist2 * 60 * 1.1515;

        Log.d(TAG, " woah " + ((Vender)o).getName() + " iii " + dist2);

        if (dist > dist2) {
            return 1;
        }

        else if (dist < dist2) {
            return -1;
        }
        else {
            return 0;
        }



    }


    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
