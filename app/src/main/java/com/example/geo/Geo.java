package com.example.geo;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Geo extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng latLng2;
    String in;
    ArrayList<String> arrayVille = new ArrayList<>();
    HashMap<String, LatLng> Mapcity = new HashMap();
    Integer CityChosen;
    TextView cityChosenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);
        cityChosenView = findViewById(R.id.cityChosen);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        InputStream in = getResources().openRawResource(R.raw.ville);
        JSONParser jsonParser = new JSONParser();

        try {
            JSONArray jsonArr = (JSONArray)jsonParser.parse(
                    new InputStreamReader(in, "UTF-8"));

            Iterator it = jsonArr.iterator();
            while (it.hasNext()) {
                JSONObject o = (JSONObject) it.next();
                String city = ((String) o.get("Ville")).trim();
                Double lat = ((Double) o.get("Latitude"));
                Double lg = ((Double) o.get("Longitude"));
                LatLng latLng = new LatLng(lat,lg);
                arrayVille.add(city);
                Mapcity.put(city,latLng);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Random rand = new Random();
        CityChosen = rand.nextInt(Mapcity.size()+1);
        cityChosenView.setText(arrayVille.get(CityChosen));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng2 = new LatLng(0,0);
        mMap = googleMap;
        LatLng cityFounded = Mapcity.get(arrayVille.get(CityChosen));
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }
        googleMap.addMarker(new MarkerOptions()
                .position(cityFounded)
                .title("Marker in Sydney"));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng2) {
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng2)
                        .title("Your marker title")
                        .snippet("Your marker snippet"));
                Toast.makeText(Geo.this,latLng2.toString(),Toast.LENGTH_SHORT).show();
            }

        });
        /*PolylineOptions rectOptions = new PolylineOptions()
                // coord sydney
                .add(latLng2)
                // coord new
                .add(sydney);
        mMap.addPolyline(rectOptions);
        // ajout d'un cercle
        CircleOptions circleOptions = new CircleOptions().center(latLng2).radius(100); // en metre
        mMap.addCircle(circleOptions);*/

    }
}