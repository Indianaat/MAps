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
import androidx.recyclerview.widget.RecyclerView;
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
    Button boutonDuo, boutonCarre, boutonCash;

    int compteur = 0;
    int score = 0;
    int maxquestions = 10;
    private int idQuestion;
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
        boutonDuo = findViewById(R.id.Duo);
        boutonCarre = findViewById(R.id.Carre);
        boutonCash = findViewById(R.id.Cash);

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

        // Rendre les boutons Duo, Carré, Cash visible
        boutonDuo.setVisibility(View.VISIBLE);
        boutonCarre.setVisibility(View.VISIBLE);
        boutonCash.setVisibility(View.VISIBLE);

        choixSolution(); // Choix entre Duo, Carré, Cash et apparition du fragment correspondant
        fragmentvide(); // Affichage du fragment présentation du jeu par défault

        compteur++; // Incrémentation du compteur de tours joué

        idQuestion = genereRandom(1, totalQuestions+1);  // Génération d'une question aléatoire correspondant à son ID dans la BDD
        Log.d(TAG, idQuestion+ "");

        if (compteur > maxquestions) {       // le quiz s'arrête au bout de n questions (n = maxquestions)

            Toast.makeText(getApplicationContext(), "Fin du quiz", Toast.LENGTH_SHORT).show();
            popupFinQuiz();

        } else {        //Si le nombre de questions max n'est pas atteint:

            numQuestion.setText(compteur + "/" + maxquestions); // Affichage du nombre de tours joué

            // Chemin pour récupérer les questions : Question + numéro
            databaseReference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(idQuestion));
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Question question = dataSnapshot.getValue(Question.class);
                    // Récupération de la question --> écrit la question dans le textView
                    questions.setText(question.getQuestion());

                    // Ajout du changement d'image avec Picasso
                    Picasso.get().load(question.getImage()).into(imageQuiz);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    // Popup de fin qui affiche le score du joueur
    public void popupFinQuiz(){

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

    // Méthode pour générer un nombre aléatoire
    public int genereRandom(int borneMin , int borneMax){ // Génération d'un idQuestions aléatoire
        Random random = new Random();
        idQuestion = borneMin+random.nextInt(borneMax-borneMin);
        return idQuestion;
    }

    // Getter pour récupérer l'id de la question de les fragments
    public int getIdQuestion() {
        return idQuestion;
    }

    // Méthode pour le choix entre Duo, Carré, Cash et affichage du fragment
    public void choixSolution(){

        boutonDuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boutonCarre.setVisibility(View.GONE); // Désactive le bouton Carré
                boutonCash.setVisibility(View.GONE); // Désactive le bouton Cash

                // Affichage du fragment Duo dans le linear layout
                DuoFragment fragmDuo = new DuoFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayoutfra, fragmDuo);
                transaction.commit();
            }
        });
        boutonCarre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boutonDuo.setVisibility(View.GONE); // Désactive le bouton Duo
                boutonCash.setVisibility(View.GONE); // Désactive le bouton Cash

                // Affichage du fragment Carré dans le linear layout
                CarreFragment fragmCarre = new CarreFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayoutfra, fragmCarre);
                transaction.commit();
            }
        });
        boutonCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boutonCarre.setVisibility(View.GONE); // Désactive le bouton Carré
                boutonDuo.setVisibility(View.GONE); // Désactive le bouton Duo

                // Affichage du fragment Cash dans le linear layout
                CashFragment fragmCash = new CashFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayoutfra, fragmCash);
                transaction.commit();
            }
        });
    }

    // Fragment de départ
    public void fragmentvide(){
        BlankFragment blankFragment = new BlankFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayoutfra, blankFragment);
        transaction.commit();
    }
}


