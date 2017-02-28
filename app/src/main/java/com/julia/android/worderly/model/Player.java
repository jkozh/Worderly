package com.julia.android.worderly.model;


import android.os.Parcel;
import android.os.Parcelable;


public class Player implements Parcelable {

    private String username;
    private String word;
    private int wordScore;
    private int gameScore;


    public Player() {
    }


    private Player(Parcel in) {
        username = in.readString();
        word = in.readString();
        wordScore = in.readInt();
        gameScore = in.readInt();
    }


    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };


    public Player(String username, String word, int wordScore, int gameScore) {
        this.username = username;
        this.word = word;
        this.wordScore = wordScore;
        this.gameScore = gameScore;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getWord() {
        return word;
    }


    public void setWord(String word) {
        this.word = word;
    }


    public int getWordScore() {
        return wordScore;
    }


    public void setWordScore(int wordScore) {
        this.wordScore = wordScore;
    }


    public int getGameScore() {
        return gameScore;
    }


    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(word);
        dest.writeInt(wordScore);
        dest.writeInt(gameScore);
    }

}
