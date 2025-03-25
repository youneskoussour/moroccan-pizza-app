package com.example.moroccanpizza.util;

import com.example.moroccanpizza.model.Pizza;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to provide sample pizza data.
 * In a production app, this data would come from a server API.
 */
public class PizzaData {

    /**
     * Get all available pizzas
     * 
     * @return List of all pizzas
     */
    public static List<Pizza> getAllPizzas() {
        List<Pizza> pizzas = new ArrayList<>();
        
        // Add some Moroccan-themed pizzas
        pizzas.add(new Pizza(
                1,
                "Marrakech Special",
                "مراكش سبيشيال",
                "Traditional Moroccan spices with lamb, olives, and bell peppers",
                "https://example.com/marrakech_special.jpg",
                89.99,
                true,
                false,
                "Pizza base, tomato sauce, mozzarella, lamb, olives, bell peppers, ras el hanout spice blend, mint"
        ));
        
        pizzas.add(new Pizza(
                2,
                "Casablanca Seafood",
                "كازابلانكا سي فود",
                "Fresh seafood pizza with Moroccan herbs and lemon",
                "https://example.com/casablanca_seafood.jpg",
                99.99,
                false,
                false,
                "Pizza base, tomato sauce, mozzarella, shrimp, calamari, mussels, parsley, lemon zest, garlic"
        ));
        
        pizzas.add(new Pizza(
                3,
                "Vegetarian Tagine",
                "طاجين نباتي",
                "Inspired by traditional tagine with roasted vegetables",
                "https://example.com/vegetarian_tagine.jpg",
                79.99,
                false,
                true,
                "Pizza base, harissa sauce, mozzarella, roasted eggplant, zucchini, bell peppers, chickpeas, preserved lemon, cilantro"
        ));
        
        pizzas.add(new Pizza(
                4,
                "Spicy Merguez",
                "ميرغيز حار",
                "Spicy Moroccan sausage with caramelized onions",
                "https://example.com/spicy_merguez.jpg",
                94.99,
                true,
                false,
                "Pizza base, tomato sauce, mozzarella, merguez sausage, caramelized onions, harissa, cumin, coriander"
        ));
        
        pizzas.add(new Pizza(
                5,
                "Fes Chicken",
                "دجاج فاس",
                "Moroccan spiced chicken with preserved lemon and olives",
                "https://example.com/fes_chicken.jpg",
                84.99,
                false,
                false,
                "Pizza base, tomato sauce, mozzarella, spiced chicken, preserved lemon, green olives, ginger, saffron"
        ));
        
        pizzas.add(new Pizza(
                6,
                "Chefchaouen Blue",
                "شفشاون بلو",
                "Blue cheese pizza inspired by the blue city",
                "https://example.com/chefchaouen_blue.jpg",
                89.99,
                false,
                true,
                "Pizza base, cream sauce, mozzarella, blue cheese, walnuts, honey, thyme, figs"
        ));
        
        pizzas.add(new Pizza(
                7,
                "Sahara BBQ",
                "باربيكيو الصحراء",
                "Smoky barbecue chicken with a touch of desert spices",
                "https://example.com/sahara_bbq.jpg",
                94.99,
                true,
                false,
                "Pizza base, BBQ sauce, mozzarella, chicken, red onions, corn, cilantro, smoked paprika"
        ));
        
        pizzas.add(new Pizza(
                8,
                "Atlas Mountain Veggie",
                "خضار جبال الأطلس",
                "Fresh mountain vegetables with goat cheese",
                "https://example.com/atlas_mountain.jpg",
                79.99,
                false,
                true,
                "Pizza base, tomato sauce, mozzarella, goat cheese, arugula, cherry tomatoes, wild mushrooms, pine nuts"
        ));
        
        return pizzas;
    }

    /**
     * Get featured pizzas for the home screen
     * 
     * @return List of featured pizzas
     */
    public static List<Pizza> getFeaturedPizzas() {
        List<Pizza> allPizzas = getAllPizzas();
        List<Pizza> featuredPizzas = new ArrayList<>();
        
        // Add some pizzas as featured
        featuredPizzas.add(allPizzas.get(0)); // Marrakech Special
        featuredPizzas.add(allPizzas.get(2)); // Vegetarian Tagine
        featuredPizzas.add(allPizzas.get(3)); // Spicy Merguez
        
        return featuredPizzas;
    }
    
    /**
     * Get a pizza by its ID
     * 
     * @param id The pizza ID
     * @return The pizza object, or null if not found
     */
    public static Pizza getPizzaById(int id) {
        List<Pizza> allPizzas = getAllPizzas();
        
        for (Pizza pizza : allPizzas) {
            if (pizza.getId() == id) {
                return pizza;
            }
        }
        
        return null;
    }
}
