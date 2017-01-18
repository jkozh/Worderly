package com.julia.android.worderly.utils;

import java.util.Random;

public class WordUtility {
    // Scramble the letters using the standard Fisher-Yates shuffle
    public static String scramble(String word) {
        char w[] = word.toCharArray();
        for(int i = 0; i < w.length - 1; i++) {
            int j = new Random().nextInt(w.length - 1);
            char temp = w[i];
            w[i] = w[j];
            w[j] = temp;
        }
        return new String(w);
    }
}
