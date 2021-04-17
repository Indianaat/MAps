/**
 L'activity Quiz
 1er mini jeu de cette application:
 Un quiz de 10 questions, il faut retrouver où se situe le monument
 **/

package com.example.geo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class Quiz extends AppCompatActivity {
    private static final String TAG = "Log: ";
    ImageView imageQuiz;
    TextView questions, numQuestion;
    Button btnDuo, btnCarre, option3, option4;
    List listQuestion = Arrays.asList();

    int compteur = 0;
    int score = 0;
    int maxquestions = 10;
    int idQuestion;
    int totalQuestions = 11;

    DatabaseReference databaseReference;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        numQuestion = findViewById(R.id.numQuest);
        imageQuiz = findViewById(R.id.ImageQuiz);
        questions = findViewById(R.id.Question);
        btnDuo = (Button) findViewById(R.id.duo);
        //btnCarre = (Button) findViewById(R.id.Carre);
        option3 = (Button) findViewById(R.id.Cash);
        //option4 = findViewById(R.id.Autres);
        firestore = FirebaseFirestore.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        score = 0;
        Duo duoFragment = new Duo();
        getSupportFragmentManager().beginTransaction().add(R.id.quizLayout,duoFragment).commit();

    }




}

