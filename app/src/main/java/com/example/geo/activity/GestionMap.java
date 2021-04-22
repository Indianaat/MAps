package com.example.geo.activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class GestionMap {

    GoogleMap googleMap;

    public GestionMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
    public Marker PlaceMarker(LatLng cooPoint){
        Marker markerPoint = googleMap.addMarker(new MarkerOptions()
                .position(cooPoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Your marker title")
                .snippet("Your marker snippet"));
        return  markerPoint;
    }
    public PolylineOptions TracerDistance(LatLng Point1, LatLng Point2){
        PolylineOptions rectOptions = new PolylineOptions()
                .add(Point1)
                .add(Point2);
        return rectOptions;
    }
}
