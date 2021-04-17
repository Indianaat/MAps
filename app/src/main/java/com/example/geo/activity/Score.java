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
    ArrayList<String> objScore = new ArrayList<>();
    ArrayList<Integer> intScore = new ArrayList<>();
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
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData().get("score"));
                        Log.d("TAG2", document.getId() + " => " + document.getData().get("fPseudo"));
                        if(document.getData().get("score") != null){
                            objtScoreDB.add(new ScoreDB(  "" + document.getData().get("fPseudo"), (Long) document.getData().get("score")));
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