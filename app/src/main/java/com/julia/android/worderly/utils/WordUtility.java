package com.julia.android.worderly.utils;

import java.util.Random;

public class WordUtility {

    /**
     * Scramble the letters using the standard Fisher-Yates shuffle.
     * @param word to be scrambled
     * @return scrambled word
     */
    public static String scrambleWord(String word) {
        char w[] = word.toCharArray();
        for(int i = 0; i < w.length; i++) {
            int j = new Random().nextInt(w.length - 1);
            char temp = w[i];
            w[i] = w[j];
            w[j] = temp;
        }
        return new String(w);
    }

    // Implementing Fisher–Yates shuffle
    public static void shuffleArray(int[] ar) {
        for (int i = ar.length - 1; i > 0; i--) {
            int index = new Random().nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static int getTileValue(char c) {
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O'
                || c == 'U' || c == 'L' || c == 'N'
                || c == 'S' || c == 'T' || c == 'R') {
            return 1;
        } else if (c == 'D' || c == 'G') {
            return 2;
        } else if (c == 'B' || c == 'C' || c == 'M' || c == 'P') {
            return 3;
        } else if (c == 'F' || c == 'H' || c == 'V' || c == 'W' || c == 'Y') {
            return 4;
        } else if (c == 'K') {
            return 5;
        } else if (c == 'J' || c == 'X') {
            return 8;
        } else if (c == 'Q' || c == 'Z') {
            return 10;
        } else {
            return 0;
        }
    }
}
