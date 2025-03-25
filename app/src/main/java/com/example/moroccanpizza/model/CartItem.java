package com.example.moroccanpizza.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int pizzaId;
    private String pizzaName;
    private String pizzaImageUrl;
    private double pizzaPrice;
    private int quantity;
    private String specialInstructions;

    public CartItem(int pizzaId, String pizzaName, String pizzaImageUrl, double pizzaPrice, int quantity, String specialInstructions) {
        this.pizzaId = pizzaId;
        this.pizzaName = pizzaName;
        this.pizzaImageUrl = pizzaImageUrl;
        this.pizzaPrice = pizzaPrice;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getPizzaImageUrl() {
        return pizzaImageUrl;
    }

    public void setPizzaImageUrl(String pizzaImageUrl) {
        this.pizzaImageUrl = pizzaImageUrl;
    }

    public double getPizzaPrice() {
        return pizzaPrice;
    }

    public void setPizzaPrice(double pizzaPrice) {
        this.pizzaPrice = pizzaPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public double getTotalPrice() {
        return pizzaPrice * quantity;
    }
}
