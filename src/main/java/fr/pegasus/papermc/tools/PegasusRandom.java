package fr.pegasus.papermc.tools;

import java.util.Random;

public class PegasusRandom {
    private final Random random;

    public PegasusRandom(){
        this.random = new Random();
    }

    public PegasusRandom(long seed){
        this.random = new Random(seed);
    }

    public int nextInt(int max){
        return this.nextInt(0, max);
    }

    public int nextInt(int min, int max){
        return random.nextInt(max - min) + min;
    }

    public String randomString(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }
}
