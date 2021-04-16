/**
 L'activity Quiz
 1er mini jeu de cette application:
 Un quiz de 10 questions, il faut retrouver où se situe le monument
 **/

package com.example.geo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Quiz extends AppCompatActivity{

    private static final String TAG = "Log: ";
    ImageView imageQuiz;
    TextView questions, numQuestion;
    Button option1, option2, option3, option4;

    Fragment fragment;

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
        option1 = findViewById(R.id.Duo);
        option2 = findViewById(R.id.Carre);
        option3 = findViewById(R.id.Cash);
        option4 = findViewById(R.id.Autres);

        firestore = FirebaseFirestore.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        score = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DuoFragment fragmDuo = new DuoFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayoutfra, fragmDuo);
                transaction.commit();
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarreFragment fragmCarre = new CarreFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayoutfra, fragmCarre);
                transaction.commit();
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CashFragment fragmCash = new CashFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayoutfra, fragmCash);
                transaction.commit();
            }
        });
        //lancementQuiz();
    }


/*
    // Lancement du quiz
    public void lancementQuiz() {
        compteur++;
        idQuestion = genereRandom(1, totalQuestions+1);
        Log.d(TAG, idQuestion+ "");

        if (compteur > maxquestions  ) {       // le quiz s'arrête au bout de n questions (n = maxquestions)

            Toast.makeText(getApplicationContext(), "Fin du quiz", Toast.LENGTH_SHORT).show();
            popupFinQuiz();

        } else {        //Si le nombre de questions max n'est pas atteint:

            numQuestion.setText(compteur + "/" + maxquestions);
            // Chemin pour récupérer les question : Question + numéro
            databaseReference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(idQuestion));
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
                    option4.setText(question.getOption4());
                    // Ajout du changement d'image avec Picasso
                    Picasso.get().load(question.getImage()).into(imageQuiz);

                    // L'utilisateur clique sur le 1er bouton
                    option1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Envoi de l'intent à l'activity quiz
                            Intent i= new Intent(Quiz.this,CarreFragment.class);
                            //lancement de l'activity Quiz
                            startActivity(i);
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
                    option4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Vérification si option3 corresponds à la réponse
                            if (option4.getText().toString().equals(question.answer)) {
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

    public void popupFinQuiz(){

        //popup fin de quiz
        AlertDialog.Builder scorePopup = new AlertDialog.Builder(this);
        scorePopup.setTitle("Votre Score:");
        scorePopup.setMessage(score + " points");
        // Bouton sur le popup suivant --> renvoi vers l'Accueil (choix mini jeux)
        scorePopup.setPositiveButton("Suivant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Fin du jeu", Toast.LENGTH_SHORT).show();

                // On lance l'activité Score
                Intent i = new Intent(Quiz.this, Accueil.class);
                startActivity(i);
            }
        });
        // Bouton sur le popup Rejouer --> relance le quiz
        scorePopup.setNegativeButton("Rejouer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Une nouvelle partie commence", Toast.LENGTH_SHORT).show();

                // On lance l'activité Score
                Intent i = new Intent(Quiz.this, Quiz.class);
                startActivity(i);
            }
        });
        scorePopup.show();
    }
    public int genereRandom(int borneMin , int borneMax){ // Génération d'un idQuestions aléatoire
        Random random = new Random();
        idQuestion = borneMin+random.nextInt(borneMax-borneMin);
        return idQuestion;
    }

 */

}


