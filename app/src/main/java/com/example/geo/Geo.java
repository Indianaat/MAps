/**
 L'activity Geo
 Jeu calcul de distance

 **/

package com.example.geo;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private LatLng latLng2;
    String in;
    ArrayList<String> arrayVille = new ArrayList<>();
    HashMap<String, LatLng> Mapcity = new HashMap();
    Integer CityChosen;
    TextView cityChosenView, txtNumQuest;
    Double distance, score;
    Button btnNext;
    Integer numQuestion =1;
    Marker markerPointChoisi, markerVilleATrouver;
    Polyline distancePoly;
    LatLng cooPointChoisi, cooVilleATrouver;
    private Geo geoActivity;
    ArrayList <Double> listeScore = new ArrayList<>();
    ArrayList <Double> listeDistance = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);
        cityChosenView = findViewById(R.id.cityChosen);
        txtNumQuest = findViewById(R.id.txtNumQuest);
        btnNext = (Button) findViewById(R.id.btnNext);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); // Fragment de la map google
        this.geoActivity = this;
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
        if (markerPointChoisi != null){
            Log.v("TZZSYGREUYHEZGVY","ALED");
            markerPointChoisi.remove();
        }
        if (distancePoly != null){
            distancePoly.remove();
        }
        if (markerVilleATrouver != null){
            markerVilleATrouver.remove();
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
        CityChosen = rand.nextInt(Mapcity.size()+1);
        cityChosenView.setText(arrayVille.get(CityChosen)); // affichage de la ville a choisir
        cooVilleATrouver = Mapcity.get(arrayVille.get(CityChosen));

        // On Détermine les coordonnées de la ville a trouver
        LatLng cityFounded = Mapcity.get(arrayVille.get(CityChosen));

        // Méthode pour mettre le marker sur la map
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng LatLgnCityChosen) {
                if( cooPointChoisi == null){
                    if (markerPointChoisi != null) {
                        markerPointChoisi.remove();
                    }
                    if (distancePoly != null) {
                        distancePoly.remove();
                    }
                    if (markerVilleATrouver != null){
                        markerVilleATrouver.remove();
                    }

                    cooPointChoisi = LatLgnCityChosen;
                    // Ajour du markeur sur la map apres un long clic
                    markerPointChoisi=gestionMap.PlaceMarker(cooPointChoisi);

                    //calcul de la distance entre les 2 points
                    distance = SphericalUtil.computeDistanceBetween(cooPointChoisi, cooVilleATrouver);
                    listeDistance.add(distance);
                    //Affichage du trait reliant les 2 positions
                    gestionMap.TracerDistance(cooPointChoisi,cooPointChoisi);
                    // Ajout du marker de la ville à trouver
                    markerVilleATrouver=gestionMap.PlaceMarker(cooVilleATrouver);

                    distancePoly =mMap.addPolyline(gestionMap.TracerDistance(cooVilleATrouver,cooPointChoisi));
                    // Toast de la distance
                    //Toast.makeText(Geo.this,"Distance \n " + String.format("%.2f", distance / 1000) + "km",Toast.LENGTH_LONG).show();
                    score = 5000/(1+((distance/1000)/100) );
                    listeScore.add(score);


                    //PopUP
                    AlertDialog.Builder scorePopup = new AlertDialog.Builder(geoActivity);
                    scorePopup.setTitle("Votre Score");
                    scorePopup.setMessage(String.format("%.2f",score) + " Points");
                    scorePopup.show();
                }else {
                    //PopUP
                    AlertDialog.Builder scorePopup = new AlertDialog.Builder(geoActivity);
                    scorePopup.setTitle("Partie déja joué");
                    scorePopup.setMessage("Veuillez cliquer sur suivant vous avez déja posé votre point");
                    scorePopup.show();
                }

            }

        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numQuestion < 5){
                    if (markerPointChoisi != null) {
                        markerPointChoisi.remove();
                    }
                    if (distancePoly != null) {
                        distancePoly.remove();
                    }
                    if (markerVilleATrouver != null){
                        markerVilleATrouver.remove();
                    }
                    cooPointChoisi =null;
                    // Choix random de la ville
                    Random rand = new Random();
                    CityChosen = rand.nextInt(Mapcity.size());
                    cityChosenView.setText(arrayVille.get(CityChosen));
                    cooVilleATrouver = Mapcity.get(arrayVille.get(CityChosen));
                    numQuestion = numQuestion+1;
                    txtNumQuest.setText(numQuestion.toString() + "/ 5");
                }else {
                    btnNext.setText("Voir score");
                    Intent i= new Intent(Geo.this,Geo_Score.class);
                    Bundle b = new Bundle();
                    b.putSerializable("arrayScore",listeScore);
                    b.putSerializable("arrayDistance",listeDistance);
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });

    }
}