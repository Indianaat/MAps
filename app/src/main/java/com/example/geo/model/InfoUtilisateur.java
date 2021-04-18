package com.example.geo.model;

import android.app.Application;

import java.util.ArrayList;

public class InfoUtilisateur extends Application {
    private String emailUser;
    private String mdpUser;
    private String PseudoUser;

    public Integer getScoreGeo() {
        return ScoreGeo;
    }

    public void setScoreGeo(Integer scoreGeo) {
        ScoreGeo = scoreGeo;
    }

    public Integer getScoreQuizz() {
        return ScoreQuizz;
    }

    public void setScoreQuizz(Integer scoreQuizz) {
        ScoreQuizz = scoreQuizz;
    }

    private Integer ScoreGeo=0;
    private Integer ScoreQuizz=0;

    public String getIdUSer() {
        return idUSer;
    }

    public void setIdUSer(String idUSer) {
        this.idUSer = idUSer;
    }

    private String idUSer;

    public ArrayList<Double> getListScore() {
        return listScore;
    }

    public void setListScore(ArrayList<Double> listScore) {
        this.listScore = listScore;
    }

    public ArrayList<Double> getListDistance() {
        return listDistance;
    }

    public void setListDistance(ArrayList<Double> listDistance) {
        this.listDistance = listDistance;
    }

    private ArrayList<Double> listScore;
    private ArrayList<Double> listDistance;

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getMdpUser() {
        return mdpUser;
    }

    public void setMdpUser(String mdpUser) {
        this.mdpUser = mdpUser;
    }

    public String getPseudoUser() {
        return PseudoUser;
    }

    public void setPseudoUser(String pseudoUser) {
        PseudoUser = pseudoUser;
    }

}
