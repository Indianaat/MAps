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

import com.example.geo.Accueil;
import com.example.geo.Connexion;
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
    EditText mPseudo,mEmail,mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    InfoUtilisateur infoUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        mPseudo     = findViewById(R.id.pseudo);
        mEmail      = findViewById(R.id.Email);
        mPassword   = findViewById(R.id.password);
        mRegisterBtn= findViewById(R.id.registerBtn);
        mLoginBtn   = findViewById(R.id.createText);
        infoUtilisateur = (InfoUtilisateur) getApplicationContext();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Accueil.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String pseudo = mPseudo.getText().toString();
                String pseudo2 = mPseudo.getText().toString();


                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email requis");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Mot de passe requis.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Le mdp doit être >= 6");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

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
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Connexion.class));
            }
        });
    }
}