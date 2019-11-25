package com.manager.subtitles.model;


public class SubTime {
    public int heurs; //2
    public int min;//2
    public int secend;//2
    public int mlsecend;//3

    public SubTime(int heurs, int min, int secend, int mlsecend) {
        this.heurs = heurs;
        this.min = min;
        this.secend = secend;
        this.mlsecend = mlsecend;
    }

    public int getHeurs() {
        return heurs;
    }

    public int getMin() {
        return min;
    }

    public int getSecend() {
        return secend;
    }

    public int getMlsecend() {
        return mlsecend;
    }

}
