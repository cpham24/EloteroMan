package edu.calstatela.cpham24.eloteroman.DisplayActivities.data;

/**
 * Created by Ambrosio on 7/24/2017.
 */

public class location {
    String locationId;
    String cartId;
    String street;
    String city;
    String zip;
    Double currentLatitude;
    Double currentLongitude;
    Double previousLatitude;
    Double previousLongitude;

    public location(String locationId, String cartId, String street, String city, String zip, Double currentLatitude, Double currentLongitude, Double previousLatitude, Double previousLongitude) {
        this.locationId = locationId;
        this.cartId = cartId;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.previousLatitude = previousLatitude;
        this.previousLongitude = previousLongitude;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getCartId() {

        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getStreet() {

        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {

        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {

        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public Double getPreviousLatitude() {

        return previousLatitude;
    }

    public void setPreviousLatitude(Double previousLatitude) {
        this.previousLatitude = previousLatitude;
    }

    public Double getPreviousLongitude() {

        return previousLongitude;
    }

    public void setPreviousLongitude(Double previousLongitude) {
        this.previousLongitude = previousLongitude;
    }
}
