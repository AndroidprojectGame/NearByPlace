package model;

/**
 * Created by mappsupport on 09-05-2018.
 */

public class PlaceModel
{
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOpeningStatus() {
        return openingStatus;
    }

    public void setOpeningStatus(String openingStatus) {
        this.openingStatus = openingStatus;
    }

    public String getNearPlace() {
        return nearPlace;
    }

    public void setNearPlace(String nearPlace) {
        this.nearPlace = nearPlace;
    }

    String icon;
    String place;
    String openingStatus;
    String nearPlace;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating)
    {
        this.rating = rating;
    }

    float rating;

    public double getCurlat() {
        return curlat;
    }

    public void setCurlat(double curlat) {
        this.curlat = curlat;
    }

    public double getCurlng() {
        return curlng;
    }

    public void setCurlng(double curlng) {
        this.curlng = curlng;
    }

    double curlat,curlng;

    public String getPlaceUrlRefe() {
        return placeUrlRefe;
    }

    public void setPlaceUrlRefe(String placeUrlRefe) {
        this.placeUrlRefe = placeUrlRefe;
    }

    String placeUrlRefe;

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    String placeID;
}
