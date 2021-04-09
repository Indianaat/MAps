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
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

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
    TextView cityChosenView, txtNumQuest;
    Double distance;
    Button btnNext;
    Integer numQuestion =0;
    Marker markerCityChosen, markerCityFound;
    Polyline distancePoly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);
        cityChosenView = findViewById(R.id.cityChosen);
        txtNumQuest = findViewById(R.id.txtNumQuest);
        btnNext = (Button) findViewById(R.id.btnNext);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); // Fragment de la map google

        mapFragment.getMapAsync(this);

        InputStream in = getResources().openRawResource(R.raw.ville); // Inputstream des villes pouvant être choisi

        JSONParser jsonParser = new JSONParser(); // création d'un Parser


        txtNumQuest.setText(numQuestion.toString() + "/ 5");

        //Manipulation du JSON
        try {
            JSONArray jsonArr = (JSONArray)jsonParser.parse(
                    new InputStreamReader(in, "UTF-8"));

            Iterator it = jsonArr.iterator();
            while (it.hasNext()) { // Boucle sur tout les éléments du JSON
                JSONObject o = (JSONObject) it.next();
                String city = ((String) o.get("Ville")).trim();
                Double lat = ((Double) o.get("Latitude"));
                Double lg = ((Double) o.get("Longitude"));
                LatLng latLng = new LatLng(lat,lg);
                arrayVille.add(city);
                Mapcity.put(city,latLng); // MAP avec les Villes et leurs corrdonnées GPS
            }

        } catch (IOException e) { // Toutes les exceptions
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Choix random de la ville
        Random rand = new Random();
        CityChosen = rand.nextInt(Mapcity.size()+1);
        cityChosenView.setText(arrayVille.get(CityChosen));
    }


    //Bordures de cadre de la france pour la map

    LatLngBounds franceBounds = new LatLngBounds(
            new LatLng(41.245846, -5.414552), // SW bounds
            new LatLng(51.728401, 9.246259)  // NE bounds
    );

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Disposition de la map
        LatLng latLng2 = new LatLng(0,0);
        mMap = googleMap;
        mMap.setLatLngBoundsForCameraTarget(franceBounds);
        mMap.setMinZoomPreference(5.0f);
        mMap.setMaxZoomPreference(10.0f);

        // On place le points au coordonées choisi
        LatLng cityFounded = Mapcity.get(arrayVille.get(CityChosen));
        try {
            // On customise notre MAP avec le JSON mapstyle
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
            // Si erreur on l'affiche normal
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }
        // Méthode pour mettre le marker sur la map
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng2) {
                if (markerCityChosen != null){
                    markerCityChosen.remove();
                    distancePoly.remove();
                }
                // Ajour du markeur sur la map apres un long clic
                markerCityChosen = googleMap.addMarker(new MarkerOptions()
                        .position(latLng2)
                        .title("Your marker title")
                        .snippet("Your marker snippet"));
                //calcul de la distance entre les 2 points
                distance = SphericalUtil.computeDistanceBetween(latLng2, Mapcity.get(arrayVille.get(CityChosen)));
                //Affichage du trait reliant les 2 positions
                PolylineOptions rectOptions = new PolylineOptions()
                        .add(latLng2)
                        .add(Mapcity.get(arrayVille.get(CityChosen)));
                        // Ajout du marker de la ville à trouver
                markerCityFound = googleMap.addMarker(new MarkerOptions()
                        .position(cityFounded)
                        .title("Marker ville à trouver "));
                distancePoly =mMap.addPolyline(rectOptions);
                        // Toast de la distance
                Toast.makeText(Geo.this,"Distance \n " + String.format("%.2f", distance / 1000) + "km",Toast.LENGTH_LONG).show();
            }

        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numQuestion < 6){
                    if (markerCityChosen != null) {
                        markerCityChosen.remove();
                    }
                    if (distancePoly != null) {
                        distancePoly.remove();
                    }
                    if (markerCityFound != null){
                        markerCityFound.remove();
                    }
                    // Choix random de la ville
                    Random rand = new Random();
                    CityChosen = rand.nextInt(Mapcity.size()+1);
                    cityChosenView.setText(arrayVille.get(CityChosen));
                    LatLng cityFounded = Mapcity.get(arrayVille.get(CityChosen));
                    numQuestion = numQuestion+1;
                    txtNumQuest.setText(numQuestion.toString() + "/ 5");
                }
            }
        });

    }
}