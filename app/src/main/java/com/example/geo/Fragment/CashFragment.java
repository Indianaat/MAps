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
import android.widget.TextView;
import android.widget.Toast;

import com.example.geo.activity.Question;
import com.example.geo.activity.Quiz;
import com.example.geo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CashFragment extends Fragment {
    private static final String TAG = "Log: ";

    View view;
    TextView txt_reponse;
    Button but_check;

    String reponse;
    int scoreCash = 2500;

    DatabaseReference databaseReference;

    public CashFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cash, container, false);

        answerCash();
        // Inflate the layout for this fragment
        return view;
    }

    public void answerCash(){

        Quiz quizActivity = (Quiz) getActivity();

        int idQuestion = quizActivity.getIdQuestion(); //Récupère l'ID dans l'activité Quiz

        databaseReference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(idQuestion));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Question question = dataSnapshot.getValue(Question.class);

                txt_reponse = view.findViewById(R.id.txt_reponse);
                but_check = view.findViewById(R.id.but_check);

                // Le joueur valide sa réponse
                but_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reponse = txt_reponse.getText().toString().trim(); // .trim() pour supprimer les espaces avant et après
                        Log.v(TAG, reponse + "");

                        // Si la réponse est correct
                        if (reponse.equals(question.getAnswer())) {
                            Toast.makeText(quizActivity.getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                            quizActivity.score = quizActivity.score + scoreCash;
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
                                    quizActivity.lancementQuiz(); // Lance la prochaine question si le nombre max n'est pas atteint
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