package fr.pegasus.papermc.tools;

public class PegasusRandom {
    public int nextInt(int max){
        return this.nextInt(0, max);
    }

    public int nextInt(int min, int max){
        return new PegasusRandom().nextInt(max - min) + min;
    }
}
