package com.example.geo;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class GestionMap {
    LatLng cooVilleATrouver;
    LatLng cooPointPose;
    GoogleMap googleMap;
    Marker markerPoint;
    GestionMap (GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
    Marker PlaceMarker (LatLng cooPoint){

        Marker markerPoint = googleMap.addMarker(new MarkerOptions()
                .position(cooPoint)
                .title("Your marker title")
                .snippet("Your marker snippet"));
        return  markerPoint;
    }
    PolylineOptions TracerDistance (LatLng Point1, LatLng Point2){
        PolylineOptions rectOptions = new PolylineOptions()
                .add(Point1)
                .add(Point2);
        return rectOptions;
    }
}
