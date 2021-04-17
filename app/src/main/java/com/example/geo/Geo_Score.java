package com.example.geo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.Date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Geo_Score extends AppCompatActivity {
    ArrayList<Double> listScore;
    ArrayList<Double> listDistance;
    ArrayList<String> listDisplay = new ArrayList<>();
    ListView lstViewScore;
    InfoUtilisateur infoUtilisateur;
    Double TotalScore = 0.0;
    private FirebaseFirestore db =FirebaseFirestore.getInstance() ;
    private DocumentReference noteRef ;

    public String getIDUser() {
        return IDUser;
    }

    public void setIDUser(String IDUser) {
        this.IDUser = IDUser;
    }

    private String IDUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo__score);
        lstViewScore = findViewById(R.id.listScore);
        Intent intent = getIntent();
        infoUtilisateur = (InfoUtilisateur) getApplicationContext();

        String userDB = String.format("users/"+infoUtilisateur.getEmailUser());
        Log.v("OMGANSWSERE",userDB);
        noteRef = db.document(userDB);
        if (intent != null){
            Log.d("Intent", "Intent is not null :(");
            listScore = (ArrayList) intent.getSerializableExtra("arrayScore");
            listDistance = (ArrayList) intent.getSerializableExtra("arrayDistance");
            infoUtilisateur.setListScore(listScore);
            infoUtilisateur.setListDistance(listDistance);
            SaveScore();
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
    public void SaveScore(){
            String date= Long.toString(System.currentTimeMillis());
        sommeScore();
        Map<String,Object> scoreObject = new HashMap<>();
        scoreObject.put(date,TotalScore);
        scoreObject.put("email",infoUtilisateur.getEmailUser());
        scoreObject.put("mdp",infoUtilisateur.getMdpUser());
        scoreObject.put("fPseudo",infoUtilisateur.getPseudoUser());
            noteRef.set(scoreObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(Geo_Score.this," Score sauvegardé",Toast.LENGTH_SHORT);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Geo_Score.this," Echec ajout dans la bdd",Toast.LENGTH_SHORT);
                }
            });
    }
    public void sommeScore(){
        for (int i=0; i< listScore.size();i++)
        {
            TotalScore = TotalScore+ listScore.get(i);
        }

    }
}