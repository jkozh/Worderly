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
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
