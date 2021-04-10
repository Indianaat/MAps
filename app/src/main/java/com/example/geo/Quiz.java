/**
 L'activity Quiz
 1er mini jeu de cette application:
 Un quiz de 10 questions, il faut retrouver où se situe le monument
 **/

package com.example.geo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Quiz extends AppCompatActivity {

    ImageView imageQuiz;
    TextView questions;
    Button option1, option2, option3;

    int numQuestion;
    int compteur=0;
    int score=0;
    int maxquestions = 3;

    DatabaseReference databaseReference;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        imageQuiz = findViewById(R.id.ImageQuiz);
        questions = findViewById(R.id.Question);
        option1 = findViewById(R.id.Duo);
        option2 = findViewById(R.id.Carre);
        option3 = findViewById(R.id.Cash);

        firestore = FirebaseFirestore.getInstance();
        score = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        lancementQuiz();
    }

    // Lancement du quiz
    public void lancementQuiz() {

        compteur++;

        if (compteur > maxquestions  ) {       // le quiz s'arrête au bout de n questions (n = maxquestions)

            Toast.makeText(getApplicationContext(), "Fin du quiz", Toast.LENGTH_SHORT).show();
            // On lance l'activité Score
            Intent i = new Intent(Quiz.this, Score.class);
            startActivity(i);

        } else {        //Si le nombre de questions max n'est pas atteint:

            // Chemin pour récupérer les question : Question + numéro
            databaseReference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(compteur));
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Question question = dataSnapshot.getValue(Question.class);
                    // Récupération de la question --> écrit la question dans le textView
                    questions.setText(question.getQuestion());
                    // Récupération des options de réponses --> écrit l'option dans le texte du bouton
                    option1.setText(question.getOption1());
                    option2.setText(question.getOption2());
                    option3.setText(question.getOption3());

                    // L'utilisateur clique sur le 1er bouton
                    option1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Vérification si option1 corresponds à la réponse
                            if (option1.getText().toString().equals(question.answer)) {     // Si correct
                                Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                                score ++;   // Score + 1

                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        lancementQuiz();    // Lance la prochaine question si le nombre max n'est pas atteint
                                    }
                                }, 1500);

                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();

                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        lancementQuiz();
                                    }
                                }, 1500);
                            }
                        }
                    });

                    // L'utilisateur clique sur le 2eme bouton
                    option2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Vérification si option2 corresponds à la réponse
                            if (option2.getText().toString().equals(question.answer)) {
                                score ++;
                                Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();

                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        lancementQuiz();     // Lance la prochaine question si le nombre max n'est pas atteint
                                    }
                                }, 2000);

                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();

                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        lancementQuiz();
                                    }
                                }, 1500);

                            }
                        }
                    });

                    // L'utilisateur clique sur le 3eme bouton
                    option3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Vérification si option3 corresponds à la réponse
                            if (option3.getText().toString().equals(question.answer)) {
                                score++;
                                Toast.makeText(getApplicationContext(), "Correct answer", Toast.LENGTH_SHORT).show();

                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        lancementQuiz();
                                    }
                                }, 2000);
                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        lancementQuiz();
                                    }
                                }, 1500);
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}

