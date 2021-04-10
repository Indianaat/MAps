/**
 L'activity Score
 Podium des scores des différents joueurs
 **/

package com.example.geo;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.widget.TextView;

public class Score extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        textView = findViewById(R.id.textView);
        textView.setText("score");
    }
}