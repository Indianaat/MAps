package com.example.geo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class CarreFragment extends Fragment {
    private static final String TAG = "Log: ";

    String reponse;
    View view;
    String indiceRandom;
    DatabaseReference databaseReference;

    public CarreFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_carre, container, false);


        indiceCarre();
        // Inflate the layout for this fragment
        return view;
    }

    public void indiceCarre(){

        Quiz quizActivity = (Quiz) getActivity();
        Button indice1 = view.findViewById(R.id.boutonInd1);
        Button indice2 = view.findViewById(R.id.boutonInd2);
        Button indice3 = view.findViewById(R.id.boutonInd3);
        Button indice4 = view.findViewById(R.id.boutonInd4);
        int idQuestion = quizActivity.getIdQuestion();

    // Chemin pour récupérer les question : Question + numéro
        databaseReference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(idQuestion));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Question question = dataSnapshot.getValue(Question.class);
                // Récupération des options de réponses --> écrit l'option dans le texte du bouton

                indice1.setText(question.getOption1());
                indice2.setText(question.getOption2());
                indice3.setText(question.getOption3());
                indice4.setText(question.getOption4());

                // L'utilisateur clique sur le 1er bouton
                indice1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( indice1.getText().toString().equals(question.getAnswer())) {
                            Log.v(TAG, indice1.getText().toString() + "");
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + 25;
                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();     // Lance la prochaine question si le nombre max n'est pas atteint
                                    indiceCarre();
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(quizActivity.getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();
                                    indiceCarre();
                                }
                            }, 1500);
                        }
                    }
                });
                indice2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( indice2.getText().toString().equals(question.answer)) {
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + 25;
                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();     // Lance la prochaine question si le nombre max n'est pas atteint
                                    indiceCarre();
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(quizActivity.getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();
                                    indiceCarre();
                                }
                            }, 1500);
                        }
                    }
                });
                indice3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( indice3.getText().toString().equals(question.answer)) {
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + 25;
                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();     // Lance la prochaine question si le nombre max n'est pas atteint
                                    indiceCarre();
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(quizActivity.getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();
                                    indiceCarre();
                                }
                            }, 1500);
                        }
                    }
                });
                indice4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( indice4.getText().toString().equals(question.answer)) {
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + 25;
                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();     // Lance la prochaine question si le nombre max n'est pas atteint
                                    indiceCarre();
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(quizActivity.getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    quizActivity.lancementQuiz();
                                    indiceCarre();
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