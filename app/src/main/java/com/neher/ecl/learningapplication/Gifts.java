package com.neher.ecl.learningapplication;

public class Gifts {
    private String name;
    private String image;
    private int en_marks;
    private int math_marks;
    private String description;

    public Gifts(String name, String image, int en_marks, int math_marks, String description) {
        this.name = name;
        this.image = image;
        this.en_marks = en_marks;
        this.math_marks = math_marks;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getEn_marks() {
        return en_marks;
    }

    public int getMath_marks() {
        return math_marks;
    }

    public String getDescription() {
        return description;
    }
}
