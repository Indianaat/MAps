package com.example.geo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Accueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
    }
    public void actQuiz(View v){
            //Envoi de l'intent à l'activity quiz
            Intent i= new Intent(Accueil.this,Quiz.class);
            //lancement de l'activity Quiz
            startActivity(i);
    }
    public void actGeo(View v){
        //Envoi de l'intent à l'activity Geo
        Intent i= new Intent(Accueil.this,Geo.class);
        //lancement de l'activity Geo
        startActivity(i);
    }
    public void actScore(View v){
        //Envoi de l'intent à l'activity Score
        Intent i= new Intent(Accueil.this,Score.class);
        //lancement de l'activity Score
        startActivity(i);
    }
}