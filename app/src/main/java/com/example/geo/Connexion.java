/**
 L'activity Connexion
 Permet de se connecter pour enregistrer les scores par la suite

 **/

package com.example.geo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Connexion extends AppCompatActivity {

    EditText login, password;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();

    }

    // lien pour s'enregistrer sans les scores
    public void actNoScore(View v){
        Intent i= new Intent(Connexion.this,Accueil.class);
        i.putExtra("msg","Bienvenue sur l'application Maps");
        startActivity(i);
    }
    // text pour créer un compte
    public void actCreeCompte(View v){
        Intent i= new Intent(Connexion.this,Inscription.class);
        i.putExtra("msg","Veuillez créer un compte");
        startActivity(i);
    }

    // Bouton valider --> Vérifier les identifiants. Si ils sont corrects renvoie sur l'activité Accueil
    public void actValider(View v){

        String mlogin = login.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        // test champs vide
        if(TextUtils.isEmpty(mlogin)){
            login.setError("Email requis");
            return;
        }

        if(TextUtils.isEmpty(pwd)){
            password.setError("Password requis");
            return;
        }

        if(pwd.length() < 6){
            password.setError("Le mot de passe doit être de 6 charactère minimum");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        //Authentification utilisateur

        fAuth.signInWithEmailAndPassword(mlogin,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){ // Si l'authentification ce fait
                    Toast.makeText(Connexion.this, getResources().getString(R.string.logok), Toast.LENGTH_SHORT).show();
                    Intent i= new Intent(Connexion.this,Accueil.class);
                    i.putExtra("msg",login.getText().toString());
                    //lancement de l'activity Accueil
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.logfailed),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
        /*if(login.getText().toString().equals(getResources().getString(R.string.login)) &&
                password.getText().toString().equals(getResources().getString(R.string.password))){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.logok),Toast.LENGTH_LONG).show();
            //Envoi de l'intent à l'activity accueil
            Intent i= new Intent(Connexion.this,Accueil.class);
            i.putExtra("msg",login.getText().toString());
            //lancement de l'activity Accueil
            startActivity(i);
        }
        else
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.logfailed),Toast.LENGTH_LONG).show();*/

}