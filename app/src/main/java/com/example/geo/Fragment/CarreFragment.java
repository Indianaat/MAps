package com.example.geo.Fragment;

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

import com.example.geo.activity.Question;
import com.example.geo.activity.Quiz;
import com.example.geo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class CarreFragment extends Fragment {
    private static final String TAG = "Log: ";

    int place;
    int idplace;
    int scoreCarre =1250;

    View view;

    DatabaseReference databaseReference;

    public CarreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_carre, container, false);

        tipsCarre();

        // Inflate the layout for this fragment
        return view;
    }

    public void tipsCarre(){

        Quiz quizActivity = (Quiz) getActivity();

        Button but_tips1 = view.findViewById(R.id.but_tips1);
        Button but_tips2 = view.findViewById(R.id.but_tips2);
        Button but_tips3 = view.findViewById(R.id.but_tips3);
        Button but_tips4 = view.findViewById(R.id.but_tips4);

        int idQuestion = quizActivity.getIdQuestion(); //Récupère l'ID dans l'activité Quiz

        // Chemin pour récupérer les question : Question + numéro
        databaseReference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(idQuestion));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Question question = dataSnapshot.getValue(Question.class);

                idplace = generateRandom(); // ID aléatoir pour placer la réponse parmis les indices

                // Récupération des options de réponses --> écrit l'option dans le texte du bouton
                // Placement de la réponses dans les boutons
                if (idplace == 1){
                    but_tips1.setText(question.getAnswer());
                    but_tips2.setText(question.getOption1());
                    but_tips3.setText(question.getOption2());
                    but_tips4.setText(question.getOption3());
                } else if (idplace == 2 ){
                    but_tips1.setText(question.getOption1());
                    but_tips2.setText(question.getAnswer());
                    but_tips3.setText(question.getOption2());
                    but_tips4.setText(question.getOption3());
                } else if (idplace == 3){
                    but_tips1.setText(question.getOption1());
                    but_tips2.setText(question.getOption2());
                    but_tips3.setText(question.getAnswer());
                    but_tips4.setText(question.getOption3());
                } else {
                    but_tips1.setText(question.getOption1());
                    but_tips2.setText(question.getOption2());
                    but_tips3.setText(question.getOption3());
                    but_tips4.setText(question.getAnswer());
                }

                // L'utilisateur clique sur le 1er bouton
                but_tips1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( but_tips1.getText().toString().equals(question.getAnswer())) {
                            Log.v(TAG, but_tips1.getText().toString() + "");
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + scoreCarre;
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
                but_tips2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( but_tips2.getText().toString().equals(question.answer)) {
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + scoreCarre;
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
                                    quizActivity.lancementQuiz();
                                }
                            }, 1500);
                        }
                    }
                });
                but_tips3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( but_tips3.getText().toString().equals(question.answer)) {
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + scoreCarre;
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
                but_tips4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( but_tips4.getText().toString().equals(question.answer)) {
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + scoreCarre;
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
    // Méthode pour générer un nombres aléatoire pour placer les indices
    public int generateRandom() {
        Random random = new Random();
        place = 1 +random.nextInt(4 - 1);
        return place;
    }
}