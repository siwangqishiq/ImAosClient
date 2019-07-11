package com.xinlan.imclient.model;

public class AblumImageItem {
    public String name;
    public String path;
    public long size;
    public int id;
    public int images;
    public boolean isFolder = true;

    public AblumImageItem(String path, String name, int id) {
        this.path = path;
        this.name = name;
        this.id = id;
    }

    public AblumImageItem(String path, String name) {
        this.path = path;
        this.name = name;
    }

}
