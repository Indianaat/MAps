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

public class DuoFragment extends Fragment {
    private static final String TAG = "Log: ";

    boolean place;
    int scoreDuo = 25;

    View view;
    Button indice1;
    Button indice2;

    DatabaseReference databaseReference;

    public DuoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_duo, container, false);

        indiceDuo();
        // Inflate the layout for this fragment
        return view;
    }

    public void indiceDuo(){

        Quiz quizActivity = (Quiz) getActivity();

        indice1 = view.findViewById(R.id.boutonInd1);
        indice2 = view.findViewById(R.id.boutonInd2);

        int idQuestion = quizActivity.getIdQuestion(); //Récupère l'ID dans l'activité Quiz

        // Chemin pour récupérer les question : Question + numéro
        databaseReference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(idQuestion));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Question question = dataSnapshot.getValue(Question.class);
                // Récupération des options de réponses --> écrit l'option dans le texte du bouton de manière aléatoire
                if (genereRandom()){
                    indice1.setText(question.getAnswer());
                    indice2.setText(question.getOption2());
                } else {
                    indice1.setText(question.getOption1());
                    indice2.setText(question.getAnswer());
                }

                // L'utilisateur clique sur le 1er bouton
                indice1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( indice1.getText().toString().equals(question.getAnswer())) {
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
                indice2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( indice2.getText().toString().equals(question.answer)) {
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
    public boolean genereRandom(){
        Random random = new Random();
        place = random.nextBoolean();
        return place;
    }
}