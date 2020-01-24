package com.manager.subtitles.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SubModel implements Serializable {
    public int id;
    public int num;
    public String lang;
    public SubTime timeStart;
    public SubTime timeEnd;
    public String lines ;

    public SubModel(int id, SubTime timeStart, SubTime timeEnd, String lines) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.lines = lines;
    }

    public SubModel() {
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setTimeStart(SubTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(SubTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setText(String lines) {
        this.lines = lines;
    }


    public String getText() {
        return lines;
    }



}
