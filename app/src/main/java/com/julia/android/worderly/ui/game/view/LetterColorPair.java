package com.julia.android.worderly.ui.game.view;


public class LetterColorPair {

    private String letter;
    private int color;

    LetterColorPair(String letter, int color) {
        this.letter = letter;
        this.color = color;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
