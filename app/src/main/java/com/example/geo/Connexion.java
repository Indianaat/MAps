/**
 L'activity Connexion
 Permet de se connecter pour enregistrer les scores par la suite

 **/

package com.example.geo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Connexion extends AppCompatActivity {

    EditText login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

    }

    // Bouton valider --> Vérifier les identifiants. Si ils sont corrects renvoie sur l'activité Accueil
    public void actValider(View v){
        if(login.getText().toString().equals(getResources().getString(R.string.login)) &&
                password.getText().toString().equals(getResources().getString(R.string.password))){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.logok),Toast.LENGTH_LONG).show();
            //Envoi de l'intent à l'activity accueil
            Intent i= new Intent(Connexion.this,Accueil.class);
            i.putExtra("msg",login.getText().toString());
            //lancement de l'activity Accueil
            startActivity(i);
        }
        else
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.logfailed),Toast.LENGTH_LONG).show();
    }
}