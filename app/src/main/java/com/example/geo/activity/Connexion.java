/**
 L'activity Connexion
 Permet de se connecter pour enregistrer les scores par la suite

 **/

package com.example.geo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.geo.R;
import com.example.geo.model.InfoUtilisateur;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Connexion extends AppCompatActivity {

    String login, password;

    EditText txt_login, txt_password;

    ProgressBar progressBar;
    FirebaseAuth fAuth;
    InfoUtilisateur infoUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        txt_login = findViewById(R.id.txt_login);
        txt_password = findViewById(R.id.txt_password);
        progressBar = findViewById(R.id.progress_bar);

        fAuth = FirebaseAuth.getInstance();
        infoUtilisateur = (InfoUtilisateur) getApplicationContext();
    }

    // lien pour s'enregistrer sans les scores
    public void actNoScore(View v){
        Intent i= new Intent(Connexion.this, Accueil.class);
        i.putExtra("msg","Bienvenue sur l'application Maps");
        startActivity(i);
    }

    // text pour créer un compte
    public void actCreateAccount(View v){
        Intent i= new Intent(Connexion.this, Inscription.class);
        i.putExtra("msg","Veuillez créer un compte");
        startActivity(i);
        finish();
    }

    // Bouton valider --> Vérifier les identifiants. Si ils sont corrects renvoie sur l'activité Accueil
    public void actValidate(View v){

        login = txt_login.getText().toString().trim();
        password = txt_password.getText().toString().trim();

        // test champs vide
        if(TextUtils.isEmpty(login)){
            txt_login.setError("Email requis");
            return;
        }

        if(TextUtils.isEmpty(password)){
            txt_password.setError("Password requis");
            return;
        }

        if(password.length() < 6){
            txt_password.setError("Le mot de passe doit être de 6 charactère minimum");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Authentification utilisateur

        fAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){ // Si l'authentification ce fait
                    Toast.makeText(Connexion.this, getResources().getString(R.string.logok), Toast.LENGTH_SHORT).show();
                    Intent i= new Intent(Connexion.this,Accueil.class);
                    i.putExtra("msg",txt_login.getText().toString());
                    //lancement de l'activity Accueil
                    infoUtilisateur.setMdpUser(password);
                    infoUtilisateur.setEmailUser(login);
                    loadPseudo();
                    Log.v("test",infoUtilisateur.getEmailUser());
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.logfailed),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
    public void loadPseudo(){
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
                        Log.v("value Email check",login);

                        if(  mailCheck.trim().equals(login.trim()) ){
                           infoUtilisateur.setPseudoUser(String.valueOf(document.getData().get("fPseudo")));
                           Long scoreQuizz = Long.valueOf((Long) document.getData().get("scoreQuiz"));
                           infoUtilisateur.setScoreQuizz(scoreQuizz.intValue());
                           Long scoreGeo = Long.valueOf((Long)document.getData().get("scoreGeo"));
                            infoUtilisateur.setScoreGeo(scoreGeo.intValue());
                           Log.v("PseudoUtilisateur",infoUtilisateur.getPseudoUser());
                           Log.v("Score Quizz",infoUtilisateur.getScoreQuizz().toString());
                           Log.v("Score Geo",infoUtilisateur.getScoreGeo().toString());
                        }

                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }
}