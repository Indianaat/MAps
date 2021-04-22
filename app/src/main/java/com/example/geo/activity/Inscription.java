package com.example.geo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.geo.R;
import com.example.geo.model.InfoUtilisateur;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Inscription extends AppCompatActivity {

    public static final String TAG = "TAG";

    EditText txt_pseudo,txt_email,txt_password;
    Button but_signup;
    TextView txt_connexion;
    ProgressBar progress_bar;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    InfoUtilisateur infoUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        txt_pseudo = findViewById(R.id.txt_pseudo);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        but_signup = findViewById(R.id.but_signup);
        txt_connexion = findViewById(R.id.txt_connexion);

        infoUtilisateur = (InfoUtilisateur) getApplicationContext();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        progress_bar = findViewById(R.id.progress_bar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Accueil.class));
            finish();
        }

        but_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txt_email.getText().toString().trim();
                String password = txt_password.getText().toString().trim();
                final String pseudo = txt_pseudo.getText().toString();
                String pseudo2 = txt_pseudo.getText().toString();


                if(TextUtils.isEmpty(email)){
                    txt_email.setError("Email requis");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    txt_password.setError("Mot de passe requis.");
                    return;
                }

                if(password.length() < 6){
                    txt_password.setError("Le mdp doit être >= 6");
                    return;
                }

                progress_bar.setVisibility(View.VISIBLE);

                //Enregistrement dans FireBase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Envoi mail de vérification

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Inscription.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });
                            Toast.makeText(Inscription.this, "Utilisateur Créé.", Toast.LENGTH_SHORT).show();
                            DocumentReference documentReference = fStore.collection("users").document(email);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fPseudo",pseudo2);
                            user.put("email",email);
                            user.put("mdp",password);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ pseudo2);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            infoUtilisateur.setEmailUser(email);
                            infoUtilisateur.setMdpUser(password);
                            infoUtilisateur.setPseudoUser(pseudo2);
                            Log.v("test",infoUtilisateur.getPseudoUser());
                            infoUtilisateur.setIdUSer(email);
                            startActivity(new Intent(getApplicationContext(),Accueil.class));
                        }else {
                            Toast.makeText(Inscription.this, "Erreur ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progress_bar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        txt_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Connexion.class));
            }
        });
    }
}