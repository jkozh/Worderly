package com.julia.android.worderly.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Round implements Parcelable {

    public static final Creator<Round> CREATOR = new Creator<Round>() {
        @Override
        public Round createFromParcel(Parcel in) {
            return new Round(in);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };
    private int roundNumber;
    private String word;
    private String definition;
    private int wordScore;

    private Round(Parcel in) {
        roundNumber = in.readInt();
        word = in.readString();
        definition = in.readString();
        wordScore = in.readInt();
    }

    public Round() {
    }

    public Round(int roundNumber, String word, String definition, int wordScore) {
        this.roundNumber = roundNumber;
        this.word = word;
        this.definition = definition;
        this.wordScore = wordScore;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getWordScore() {
        return wordScore;
    }

    public void setWordScore(int wordScore) {
        this.wordScore = wordScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(roundNumber);
        dest.writeString(word);
        dest.writeString(definition);
        dest.writeInt(wordScore);
    }
}
