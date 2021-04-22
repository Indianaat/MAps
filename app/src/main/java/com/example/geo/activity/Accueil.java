/**
    L'activity Accueil
    Après s'être connecté, cette activité permet de choisir le mode de jeu/quiz/score

 **/

package com.example.geo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.geo.R;

public class Accueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
    }

    // Bouton statue de la liberté --> lance le quiz image
    public void actQuiz(View v){
            //Envoi de l'intent à l'activity quiz
            Intent i= new Intent(Accueil.this, Quiz.class);
            //lancement de l'activity Quiz
            startActivity(i);
    }

    // Bouton univers --> lance le jeu map
    public void actGeo(View v){
        //Envoi de l'intent à l'activity Geo
        Intent i= new Intent(Accueil.this, Geo.class);
        //lancement de l'activity Geo
        startActivity(i);
    }

    // Bouton médaille --> lance la page des scores
    public void actScore(View v){
        //Envoi de l'intent à l'activity Score
        Intent i= new Intent(Accueil.this, Score.class);
        //lancement de l'activity Score
        startActivity(i);
    }
}