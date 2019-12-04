package com.manager.subtitles.model;

import java.util.ArrayList;

public class SubFile  {
    public String name;
    public String path;
    public ArrayList<SubModel> subModels = new ArrayList<>();

    public SubFile() {
    }

    public SubFile(String name, String path, ArrayList<SubModel> subModels) {
        this.name = name;
        this.path = path;
        this.subModels = subModels;
    }
}
