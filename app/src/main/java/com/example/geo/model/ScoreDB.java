package com.example.geo.model;

public class ScoreDB implements Comparable <ScoreDB>{
    private String pseudo;
    private Long score;

    public ScoreDB(String pseudo, Long score) {
        this.pseudo = pseudo;
        this.score = score;
    }

    @Override
    public int compareTo(ScoreDB o) {
        Long compare = o.score - this.score;
        return   compare.intValue();
    }
    public String toString(){
        return String.format("Score : %d Pseudo : %s",score,pseudo);
    }
}
