package com.manager.subtitles.model;

import android.annotation.SuppressLint;

public class SubTime {
    int heurs; //2
    int min;//2
    int secend;//2
    int mlsecend;//3

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
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d.%03d", this.heurs, this.min, this.secend, this.mlsecend);
    }
}
