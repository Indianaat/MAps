/**
 L'activity Quiz
 1er mini jeu de cette application:
 Un quiz de 10 questions, il faut retrouver où se situe le monument
 **/

package com.example.geo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geo.Fragment.QuizFragment;
import com.example.geo.Fragment.CarreFragment;
import com.example.geo.Fragment.CashFragment;
import com.example.geo.Fragment.DuoFragment;
import com.example.geo.R;
import com.example.geo.model.InfoUtilisateur;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Quiz extends AppCompatActivity{

    private static final String TAG = "Log: ";

    ImageView img_quiz;
    TextView txt_quest, txt_num_quest, txt_show_score, txt_popup_aff_score;
    Button but_duo, but_carre, but_cash, but_popup_rejouer, but_popup_accueil;

    int counter = 0;
    public int score = 0;
    int maxquestions = 10;
    private int idQuestion;
    int totalQuestions = 40; // Nombre de question dans la BDD

    InfoUtilisateur infoUtilisateur;

    private DocumentReference noteRef ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Dialog dialog;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txt_num_quest = findViewById(R.id.txt_num_quest);
        img_quiz = findViewById(R.id.img_quiz);
        txt_quest = findViewById(R.id.txt_quest);
        but_duo = findViewById(R.id.but_duo);
        but_carre = findViewById(R.id.but_carre);
        but_cash = findViewById(R.id.but_cash);
        txt_show_score = findViewById(R.id.txt_show_score);
        dialog = new Dialog(this);
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

        txt_show_score.setText(score + "");

        choixSolution(); // Choix entre Duo, Carré, Cash et apparition du fragment correspondant
        fragmentvide(); // Affichage du fragment présentation du jeu par défault

        counter++; // Incrémentation du compteur de tours joué

        idQuestion = genereRandom(1, totalQuestions+1);  // Génération d'une question aléatoire correspondant à son ID dans la BDD
        Log.d(TAG, idQuestion+ "");

        if (counter > maxquestions) {       // le quiz s'arrête au bout de n questions (n = maxquestions)
            loadScore();
            Toast.makeText(getApplicationContext(), "Fin du quiz", Toast.LENGTH_SHORT).show();
            but_duo.setVisibility(View.GONE);
            but_carre.setVisibility(View.GONE);
            but_cash.setVisibility(View.GONE);

            //popup de fin de partie
            dialog.setContentView(R.layout.popup);
            txt_popup_aff_score = (TextView) dialog.findViewById(R.id.txt_popup_aff_score);

            txt_popup_aff_score.setText(score+"");
            popupButton();

            dialog.show();

        } else {        //Si le nombre de questions max n'est pas atteint:

            but_duo.setVisibility(View.VISIBLE);
            but_carre.setVisibility(View.VISIBLE);
            but_cash.setVisibility(View.VISIBLE);

            txt_num_quest.setText(counter + "/" + maxquestions); // Affichage du nombre de tours joué

            // Chemin pour récupérer les questions : Question + numéro
            databaseReference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(idQuestion));
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Question question = dataSnapshot.getValue(Question.class);
                    // Récupération de la question --> écrit la question dans le textView
                    txt_quest.setText(question.getQuestion());

                    // Ajout du changement d'image avec Picasso
                    Picasso.get().load(question.getImage()).into(img_quiz);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    // Popup de fin qui affiche le score du joueur
    public void popupButton(){
        but_popup_rejouer = (Button) dialog.findViewById(R.id.but_popup_rejouer);
        but_popup_accueil = (Button) dialog.findViewById(R.id.but_popup_accueil);

        but_popup_rejouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On lance l'activité Score
                Intent i = new Intent(Quiz.this, Quiz.class);
                startActivity(i);
            }
        });
        but_popup_accueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On lance l'activité Score
                Intent i = new Intent(Quiz.this, Accueil.class);
                startActivity(i);
            }
        });
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

        but_duo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_carre.setVisibility(View.GONE); // Désactive le bouton Carré
                but_cash.setVisibility(View.GONE); // Désactive le bouton Cash

                // Affichage du fragment Duo dans le linear layout
                DuoFragment fragmDuo = new DuoFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayoutfra, fragmDuo);
                transaction.commit();
            }
        });
        but_carre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_duo.setVisibility(View.GONE); // Désactive le bouton Duo
                but_cash.setVisibility(View.GONE); // Désactive le bouton Cash

                // Affichage du fragment Carré dans le linear layout
                CarreFragment fragmCarre = new CarreFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linearLayoutfra, fragmCarre);
                transaction.commit();
            }
        });
        but_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_carre.setVisibility(View.GONE); // Désactive le bouton Carré
                but_duo.setVisibility(View.GONE); // Désactive le bouton Duo

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
        QuizFragment quizFragment = new QuizFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.linearLayoutfra, quizFragment);
        transaction.commit();
    }
    public void loadScore(){
        infoUtilisateur = (InfoUtilisateur) getApplicationContext();
        Map<String,Object> scoreObject = new HashMap<>();
        FirebaseFirestore fStore;
        String userDB = String.format("users/"+infoUtilisateur.getEmailUser());
        noteRef = db.document(userDB);
        fStore = FirebaseFirestore.getInstance();

        infoUtilisateur.setScoreQuizz(score);
        scoreObject.put("scoreQuiz",score);
        scoreObject.put("email",infoUtilisateur.getEmailUser());
        scoreObject.put("mdp",infoUtilisateur.getMdpUser());
        scoreObject.put("fPseudo",infoUtilisateur.getPseudoUser());
        scoreObject.put("scoreGeo",infoUtilisateur.getScoreGeo());
        noteRef.set(scoreObject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Quiz.this," Score sauvegardé",Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Quiz.this," Echec ajout dans la bdd",Toast.LENGTH_SHORT);
            }
        });
    }
}


