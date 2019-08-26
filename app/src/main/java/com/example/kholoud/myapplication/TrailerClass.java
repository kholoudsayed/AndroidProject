package com.example.kholoud.myapplication;

public class TrailerClass {
    //DEF**********************************************************
    private String name;
    private String site;
    private String key;
    private String url;

    ///Constractor*******************************************************************

    public TrailerClass(String name, String site, String key) {
        this.name = name;
        this.site = site;
        this.key = key;
        this.url = "https://www.youtube.com/watch?v=" + key;
    }
//Getter***************************
    public String getName() {
        return name;
    }
    public String getKey() {
        return key;
    }
}
