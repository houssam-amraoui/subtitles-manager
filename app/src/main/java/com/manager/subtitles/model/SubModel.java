package com.manager.subtitles.model;

import java.util.ArrayList;

public class SubModel {
    public int id;
    public SubTime timeStart;
    public SubTime timeEnd;
    public ArrayList<String> lines = new ArrayList<>();

    public SubModel(int id, SubTime timeStart, SubTime timeEnd, ArrayList<String> lines) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.lines = lines;
    }

    public SubModel() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeStart(SubTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(SubTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }


    public String getText() {
        String ll="";
        for (int i=0;i<lines.size();i++)
            if (i<lines.size()-1)
                ll+=lines.get(i)+"\n";
            else
                ll+=lines.get(i);
        return ll;
    }
}
