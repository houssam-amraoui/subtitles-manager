package com.manager.subtitles.model;

import java.util.ArrayList;

public class SubFileOnly {
    public String iddb;
    public String name;
    public String path;

    public SubFileOnly() {
    }

    public SubFileOnly(String name, String path,String iddb) {
        this.name = name;
        this.path = path;
        this.iddb=iddb;
    }
}
