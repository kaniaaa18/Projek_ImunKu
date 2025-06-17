package com.example.projek_imunku.ui.lokasi;

import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Puskesmas {
    String name;
    String address;
    GeoPoint point;
    double distance;
    String link;

    public Puskesmas() {}

    public Puskesmas(String name, String address, double lat, double lng, double distance) {
        this.name = name;
        this.address = address;
        this.point = point;
        this.distance = distance;
        this.link = link;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public GeoPoint getPoint() { return point; }
    public void setPoint(GeoPoint point) { this.point = point; }

    public double getLat() {
        return point != null ? point.getLatitude() : 0.0;
    }

    public double getLng() {
        return point != null ? point.getLongitude() : 0.0;
    }


    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

}