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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.geo.activity.Inscription;
import com.example.geo.activity.Score;
import com.example.geo.model.InfoUtilisateur;
import com.example.geo.model.ScoreDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class Connexion extends AppCompatActivity {

    EditText login, password;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    InfoUtilisateur infoUtilisateur;
    String mlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        infoUtilisateur = (InfoUtilisateur) getApplicationContext();
    }

    // lien pour s'enregistrer sans les scores
    public void actNoScore(View v){
        Intent i= new Intent(Connexion.this,Accueil.class);
        i.putExtra("msg","Bienvenue sur l'application Maps");
        startActivity(i);
    }
    // text pour créer un compte
    public void actCreeCompte(View v){
        Intent i= new Intent(Connexion.this, Inscription.class);
        i.putExtra("msg","Veuillez créer un compte");
        startActivity(i);
    }

    // Bouton valider --> Vérifier les identifiants. Si ils sont corrects renvoie sur l'activité Accueil
    public void actValider(View v){

        mlogin = login.getText().toString().trim();
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
                    infoUtilisateur.setMdpUser(pwd);
                    infoUtilisateur.setEmailUser(mlogin);
                    chargerPseudo();
                    Log.v("test",infoUtilisateur.getEmailUser());
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.logfailed),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
    public void chargerPseudo(){
        FirebaseFirestore fStore;
        fStore = FirebaseFirestore.getInstance();
        fStore.collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAGEMAIL", document.getId() + " => " + document.getData().get("score"));
                        String mailCheck = String.valueOf(document.getData().get("email")).trim();
                        Log.v("value Email",String.valueOf(document.getData().get("email")).trim());
                        Log.v("value Email check",mlogin);
                        if(  mailCheck.trim().equals(mlogin.trim()) ){
                           infoUtilisateur.setPseudoUser(String.valueOf(document.getData().get("fPseudo")));
                           Log.v("PseudoUtilisateur",infoUtilisateur.getPseudoUser());
                        }

                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }
}