package com.example.forlempopoli.Entity.request;

public class Home_Page {

    private String name;
    private int bg_color;


    public Home_Page() {
    }

    public Home_Page(String name, int bg_color) {
        this.name = name;
        this.bg_color = bg_color;
    }

    public int getBg_color() {
        return bg_color;
    }

    public void setBg_color(int bg_color) {
        this.bg_color = bg_color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
