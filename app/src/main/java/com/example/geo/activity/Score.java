/**
 L'activity Score
 Podium des scores des différents joueurs
 **/

package com.example.geo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.geo.R;
import com.example.geo.model.ScoreDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class Score extends AppCompatActivity {

    FirebaseFirestore fStore;

    ListView listViewScore;

    ArrayList<ScoreDB> objtScoreDB = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        listViewScore = findViewById(R.id.list_score);
        fStore = FirebaseFirestore.getInstance();
        fStore.collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Integer i=0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG2", document.getId() + " => " + document.getData().get("fPseudo"));
                        if (i<10){
                            if(document.getData().get("scoreGeo") != null || document.getData().get("scoreQuiz") != null && i<10){
                                Long scoreGeo =  document.getData().get("scoreGeo") != null ? (Long) document.getData().get("scoreGeo") : 0;
                                Long scoreQuiz = document.getData().get("scoreQuiz") != null ? (Long) document.getData().get("scoreQuiz") : 0;
                                Long sommeScore = scoreQuiz + scoreGeo;
                                objtScoreDB.add(new ScoreDB(  "" + document.getData().get("fPseudo"),scoreGeo, scoreQuiz,sommeScore));
                                i=i+1;
                                Log.v("TAG3",i.toString());
                            }
                        }

                    }
                    Collections.sort(objtScoreDB);

                    ArrayAdapter<ScoreDB> adapter = new ArrayAdapter<>(Score.this, android.R.layout.simple_list_item_1,objtScoreDB);
                    listViewScore.setAdapter(adapter);
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }

}