/**
 L'activity Score
 Podium des scores des différents joueurs
 **/

package com.example.geo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Score extends AppCompatActivity {
    FirebaseFirestore fStore;
    ArrayList<Map> objScore = new ArrayList<>();
    ListView listViewScore;
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
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        objScore.add(document.getData());
                    }
                    ArrayAdapter<Map> adapter = new ArrayAdapter<>(Score.this, android.R.layout.simple_list_item_1,objScore);
                    listViewScore.setAdapter(adapter);
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }

}