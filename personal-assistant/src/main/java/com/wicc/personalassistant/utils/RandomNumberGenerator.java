package com.wicc.personalassistant.utils;
import java.util.Random;

public class RandomNumberGenerator {
    public static String generate(){
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        System.out.println(number);
        return String.format("%05d", number);
    }

}
