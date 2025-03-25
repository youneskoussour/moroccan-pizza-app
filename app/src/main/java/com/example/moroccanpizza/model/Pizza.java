package com.example.moroccanpizza.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pizzas")
public class Pizza {
    @PrimaryKey
    private int id;
    private String name;
    private String nameArabic;
    private String description;
    private String imageUrl;
    private double price;
    private boolean isSpicy;
    private boolean isVegetarian;
    private String ingredients;

    public Pizza(int id, String name, String nameArabic, String description, String imageUrl, 
                 double price, boolean isSpicy, boolean isVegetarian, String ingredients) {
        this.id = id;
        this.name = name;
        this.nameArabic = nameArabic;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.isSpicy = isSpicy;
        this.isVegetarian = isVegetarian;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameArabic() {
        return nameArabic;
    }

    public void setNameArabic(String nameArabic) {
        this.nameArabic = nameArabic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSpicy() {
        return isSpicy;
    }

    public void setSpicy(boolean spicy) {
        isSpicy = spicy;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
