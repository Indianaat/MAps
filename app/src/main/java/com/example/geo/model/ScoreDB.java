package com.example.geo.model;

public class ScoreDB implements Comparable <ScoreDB>{
    private String pseudo;
    private Long scoreGeo;
    private Long scoreQuiz;
    private Long sommeScore;

    public ScoreDB(String pseudo, Long scoreGeo, Long scoreQuiz, Long sommeScore) {
        this.pseudo = pseudo;
        this.scoreGeo = scoreGeo;
        this.scoreQuiz = scoreQuiz;
        this.sommeScore = sommeScore;
    }

    @Override
    public int compareTo(ScoreDB o) {
        Long compare = o.sommeScore - this.sommeScore;
        return   compare.intValue();
    }
    public String toString(){
        return String.format(" Pseudo : %s\n Score total : %d\n Score Geo : %d\n Score Quiz : %d",pseudo,sommeScore,scoreGeo,scoreQuiz);
    }
}
