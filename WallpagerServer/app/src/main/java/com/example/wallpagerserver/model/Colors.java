package com.example.wallpagerserver.model;

public class Colors {
    private String hex;
    private String name;

    public Colors() {
    }

    public Colors(String hex, String name) {
        this.hex = hex;
        this.name = name;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getHex() {
        return hex;
    }
}
