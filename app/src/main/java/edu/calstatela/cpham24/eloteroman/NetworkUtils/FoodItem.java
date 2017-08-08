package edu.calstatela.cpham24.eloteroman.NetworkUtils;

/**
 * Created by bill on 8/5/17.
 */

public class FoodItem {
    public String name;
    public String description;
    public double price;

    public FoodItem() {
        // empty constructor
    }

    public FoodItem(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
