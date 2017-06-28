package com.example.roshan.ncab;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by roshan on 6/14/17.
 */

public class Post {

    public Post() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String email;
    private double latitude;
    private double longitude;

    public Long getContact() {
        return contact;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }

    private Long contact;
    private String username;
    private String licence;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public Post(String id,String email, double latitude, double longitude,long contact,String licence,String username) {
        this.email = email;
        this.id=id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.licence=licence;
        this.username=username;
        this.contact=contact;
    }
}
