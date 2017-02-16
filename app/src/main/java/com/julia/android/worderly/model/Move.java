package com.julia.android.worderly.model;


public class Move {

    private String word;
    private String score;

    public Move() {
    }

    public Move(String word, String score) {
        this.word = word;
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}
