package com.morsecode.model;

import java.util.HashMap;
import java.util.Map;

public class MorseCodeCalculator {

    private static volatile MorseCodeCalculator mInstance;
    private final Map<String, String> morseCodes = new HashMap<String, String>();

    private MorseCodeCalculator() {
        morseCodes.put("01", "a");
        morseCodes.put("1000", "b");
        morseCodes.put("1010", "c");
        morseCodes.put("100", "d");
        morseCodes.put("0", "e");
        morseCodes.put("0010", "f");
        morseCodes.put("110", "g");
        morseCodes.put("0000", "h");
        morseCodes.put("00", "i");
        morseCodes.put("0111", "j");
        morseCodes.put("101", "k");
        morseCodes.put("0100", "l");
        morseCodes.put("11", "m");
        morseCodes.put("10", "n");
        morseCodes.put("111", "o");
        morseCodes.put("0110", "p");
        morseCodes.put("1101", "q");
        morseCodes.put("010", "r");
        morseCodes.put("000", "s");
        morseCodes.put("1", "t");
        morseCodes.put("001", "u");
        morseCodes.put("0001", "v");
        morseCodes.put("011", "w");
        morseCodes.put("1001", "x");
        morseCodes.put("1011", "y");
        morseCodes.put("1100", "z");
        
        morseCodes.put("010101", ".");
        morseCodes.put("110011", ",");
        morseCodes.put("001100", "?");
        morseCodes.put("10010", "/");
        morseCodes.put("011010", "@");
        
        morseCodes.put("01111", "1");
        morseCodes.put("00111", "2");
        morseCodes.put("00011", "3");
        morseCodes.put("00001", "4");
        morseCodes.put("00000", "5");
        morseCodes.put("10000", "6");
        morseCodes.put("11000", "7");
        morseCodes.put("11100", "8");
        morseCodes.put("11110", "9");
        morseCodes.put("11111", "0");
    }

    public static MorseCodeCalculator getInstance() {
        if (mInstance == null) {
            synchronized (MorseCodeCalculator.class) {
                if (mInstance == null) {
                    mInstance = new MorseCodeCalculator();
                }
            }
        }
        return mInstance;
    }
    
    public String produce(String input, boolean isUpperCase) {
        String result = morseCodes.get(input);
        android.util.Log.e("TEST", result+"");
        if (result == null || result.isEmpty()) {
            return "";
        }
        return (isUpperCase) ? result.toUpperCase() : result;
    }
}
