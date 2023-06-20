package com.example.eshopproject;


public class Item {

    private String id;
    private String description;
    private double price;

    private String imageUrl;
    public Item(String description, double price) {
        this.description = description;
        this.price = price;
    }
    public Item() {}

    public void setId(String id){
        this.id = id;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
    public String getDescription(){
        return description;
    }
    public double getPrice(){
        return price;
    }
    public String getImageUrl() {return imageUrl;}

}
