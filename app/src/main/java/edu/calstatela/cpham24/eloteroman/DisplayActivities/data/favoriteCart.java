package edu.calstatela.cpham24.eloteroman.DisplayActivities.data;

/**
 * Created by Ambrosio on 8/6/2017.
 */

public class favoriteCart {
    String id;
    String name;
    String address;
    String time;
    String url;
    double longitude;
    double latitude;

    public favoriteCart(String id, String name, String address, String time, String url) {
        this.id=id;
        this.name = name;
        this.address = address;
        this.time = time;
        this.url=url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


}
