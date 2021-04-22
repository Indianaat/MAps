/**
 L'activity Geo
 Jeu calcul de distance

 **/

package com.example.geo.activity;

import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.geo.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.SphericalUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Geo extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geo geoActivity;

    Integer CityChosen, numQuestion =1;
    Double distance, score;

    TextView txt_city_chosen, txt_num_quest;
    Button but_next, but_popup_marker, but_popup_next;

    Marker markerPointChosen, markerCityToFound;
    Polyline distancePoly;
    LatLng cooPointChosen, cooCityToFound;
    Dialog dialog;

    ArrayList <Double> listScore = new ArrayList<>();
    ArrayList <Double> listDistance = new ArrayList<>();
    ArrayList<String> arrayCity = new ArrayList<>();
    HashMap<String, LatLng> Mapcity = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);

        txt_city_chosen = findViewById(R.id.txt_city_chosen);
        txt_num_quest = findViewById(R.id.txt_num_quest);
        but_next = (Button) findViewById(R.id.but_next);

        dialog = new Dialog(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fra_map); // Fragment de la map google

        this.geoActivity = this;

        mapFragment.getMapAsync(this);

        InputStream in = getResources().openRawResource(R.raw.ville); // Inputstream des villes pouvant être choisi

        JSONParser jsonParser = new JSONParser(); // création d'un Parser

        txt_num_quest.setText(numQuestion.toString() + "/ 5");

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
                arrayCity.add(city);
                Mapcity.put(city,latLng); // MAP avec les Villes et leurs corrdonnées GPS
            }

        } catch (IOException e) { // Toutes les exceptions
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Bordures de cadre de la france pour la map

    LatLngBounds franceBounds = new LatLngBounds(
            new LatLng(41.245846, -5.414552), // SW bounds
            new LatLng(51.728401, 9.246259)  // NE bounds
    );

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Gestionnaire pour la map
        GestionMap gestionMap = new GestionMap(googleMap);

        // Disposition de la map
        mMap = googleMap;
        mMap.setLatLngBoundsForCameraTarget(franceBounds);
        mMap.setMinZoomPreference(5.0f);
        mMap.setMaxZoomPreference(10.0f);

        //supression des anciens  markeur et lignes
        if (markerPointChosen != null){
            markerPointChosen.remove();
        }
        if (distancePoly != null){
            distancePoly.remove();
        }
        if (markerCityToFound != null){
            markerCityToFound.remove();
        }

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

        // Choix random de la ville
        Random rand = new Random();
        CityChosen = rand.nextInt(Mapcity.size());
        txt_city_chosen.setText(arrayCity.get(CityChosen)); // affichage de la ville a choisir
        cooCityToFound = Mapcity.get(arrayCity.get(CityChosen));

        // On Détermine les coordonnées de la ville a trouver
        LatLng cityFounded = Mapcity.get(arrayCity.get(CityChosen));

        // Méthode pour mettre le marker sur la map
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng LatLgnCityChosen) {
                if( cooPointChosen == null){
                    if (markerPointChosen != null) {
                        markerPointChosen.remove();
                    }
                    if (distancePoly != null) {
                        distancePoly.remove();
                    }
                    if (markerCityToFound != null){
                        markerCityToFound.remove();
                    }

                    cooPointChosen = LatLgnCityChosen;
                    // Ajour du markeur sur la map apres un long clic
                    markerPointChosen=gestionMap.PlaceMarker(cooPointChosen);

                    //calcul de la distance entre les 2 points
                    distance = SphericalUtil.computeDistanceBetween(cooPointChosen, cooCityToFound);
                    listDistance.add(distance);
                    //Affichage du trait reliant les 2 positions
                    gestionMap.TracerDistance(cooPointChosen,cooPointChosen);
                    // Ajout du marker de la ville à trouver
                    markerCityToFound=gestionMap.PlaceMarker(cooCityToFound);

                    distancePoly =mMap.addPolyline(gestionMap.TracerDistance(cooCityToFound,cooPointChosen));
                    // Toast de la distance
                    //Toast.makeText(Geo.this,"Distance \n " + String.format("%.2f", distance / 1000) + "km",Toast.LENGTH_LONG).show();
                    score = 5000/(1+((distance/1000)/100) );
                    listScore.add(score);

                    //PopUP
                    TextView txt_popup_aff_score;

                    dialog.setContentView(R.layout.popup_geo);
                    txt_popup_aff_score = (TextView) dialog.findViewById(R.id.txt_popup_aff_score);
                    txt_popup_aff_score.setText(String.format("%.2f",score) + " Points");
                    but_popup_marker = (Button) dialog.findViewById(R.id.but_popup_marker);
                    but_popup_next = (Button) dialog.findViewById(R.id.but_popup_next);

                    dialog.show();

                    but_popup_marker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    but_popup_next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            actNext(v);
                            dialog.dismiss();
                        }
                    });

                } else {
                    //PopUP
                    dialog.setContentView(R.layout.popup_geo_fin);
                    dialog.show();
                }

            }

        });


    }
    public void actNext(View v){
        Log.v("VIEW",v.toString());
        if (numQuestion < 5){
            if (markerPointChosen != null) {
                markerPointChosen.remove();
            }
            if (distancePoly != null) {
                distancePoly.remove();
            }
            if (markerCityToFound != null){
                markerCityToFound.remove();
            }
            cooPointChosen =null;
            // Choix random de la ville
            Random rand = new Random();
            CityChosen = rand.nextInt(Mapcity.size());
            txt_city_chosen.setText(arrayCity.get(CityChosen));
            cooCityToFound = Mapcity.get(arrayCity.get(CityChosen));
            numQuestion = numQuestion+1;
            txt_num_quest.setText(numQuestion.toString() + "/ 5");
        }else {
            but_next.setText("Voir score");
            Intent i= new Intent(Geo.this, GeoScore.class);
            Bundle b = new Bundle();
            b.putSerializable("arrayScore",listScore);
            b.putSerializable("arrayDistance",listDistance);
            i.putExtras(b);
            startActivity(i);
        }
    }
}