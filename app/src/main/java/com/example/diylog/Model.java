package com.example.diylog;

public class Model {

    String date, header, main, image, author,key;

    // custructor
    public Model(){

    }

    public Model(String date, String header, String main, String image, String author,String key) {
        this.date = date;
        this.header = header;
        this.main = main;
        this.image = image;
        this.author = author;
    }


    // getter and setter


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
