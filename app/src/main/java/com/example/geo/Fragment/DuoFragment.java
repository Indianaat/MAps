package com.example.geo.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.geo.activity.Question;
import com.example.geo.activity.Quiz;
import com.example.geo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class DuoFragment extends Fragment {
    private static final String TAG = "Log: ";

    boolean place;
    int scoreDuo = 250;
    View view;
    Button but_tips1;
    Button but_tips2;

    DatabaseReference databaseReference;

    public DuoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_duo, container, false);

        tipsDuo();
        // Inflate the layout for this fragment
        return view;
    }

    public void tipsDuo(){

        Quiz quizActivity = (Quiz) getActivity();

        but_tips1 = view.findViewById(R.id.but_tips1);
        but_tips2 = view.findViewById(R.id.but_tips2);

        int idQuestion = quizActivity.getIdQuestion(); //Récupère l'ID dans l'activité Quiz

        // Chemin pour récupérer les question : Question + numéro
        databaseReference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(idQuestion));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Question question = dataSnapshot.getValue(Question.class);
                // Récupération des options de réponses --> écrit l'option dans le texte du bouton de manière aléatoire
                if (generateRandom()){
                    but_tips1.setText(question.getAnswer());
                    but_tips2.setText(question.getOption2());
                } else {
                    but_tips1.setText(question.getOption1());
                    but_tips2.setText(question.getAnswer());
                }

                // L'utilisateur clique sur le 1er bouton
                but_tips1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( but_tips1.getText().toString().equals(question.getAnswer())) {
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + scoreDuo;
                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();     // Lance la prochaine question si le nombre max n'est pas atteint
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(quizActivity.getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();   // Lance la prochaine question si le nombre max n'est pas atteint
                                }
                            }, 1500);
                        }
                    }
                });

                // L'utilisateur clique sur le 2eme bouton
                but_tips2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( but_tips2.getText().toString().equals(question.answer)) {
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + scoreDuo;
                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();     // Lance la prochaine question si le nombre max n'est pas atteint
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(quizActivity.getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();   // Lance la prochaine question si le nombre max n'est pas atteint
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

    // Méthode pour générer un boolean aléatoirement pour mélanger les indices
    public boolean generateRandom(){
        Random random = new Random();
        place = random.nextBoolean();
        return place;
    }
}