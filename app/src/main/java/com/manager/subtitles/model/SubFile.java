package com.manager.subtitles.model;

import java.util.ArrayList;

public class SubFile extends SubFileOnly {
    public ArrayList<SubModel> subModels = new ArrayList<>();

    public SubFile() {
    }

    public SubFile(String name, String path, ArrayList<SubModel> subModels,String iddb) {
        super(name, path,iddb);
        this.subModels = subModels;
    }
}
