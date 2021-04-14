package com.example.geo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class Geo_Score extends AppCompatActivity {
    ArrayList<Double> listScore;
    ArrayList<Double> listDistance;
    ArrayList<String> listDisplay = new ArrayList<>();
    ListView lstViewScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo__score);

        lstViewScore = findViewById(R.id.listScore);
        Intent intent = getIntent();

        if (intent != null){
            Log.d("Intent", "Intent is not null :(");
            listScore = (ArrayList) intent.getSerializableExtra("arrayScore");
            listDistance = (ArrayList) intent.getSerializableExtra("arrayDistance");
            Log.v("test",listDistance.toString());
            Log.v("test33",listScore.toString());
            Log.v("MDR","OMG");
        }else {
            Log.d("Intent", "Intent is null :(");
        }
        for (int i=0; i<listScore.size();i++){
            String objectDisplay = String.format("Manche %d\n %.2f Points   \n%.2f Kilometres",i,listScore.get(i),listDistance.get(i)/1000);
            Log.v("OMG",objectDisplay);
            listDisplay.add(objectDisplay);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listDisplay);
        lstViewScore.setAdapter(adapter);

    }
    public void actAccueil(View v){
        Intent i = new Intent(this,Accueil.class);
        startActivity(i);
    }
}