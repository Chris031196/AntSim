package main;

import java.util.ArrayList;
import java.util.List;

public class Food {
    public static final int INIT_AMOUNT = 100;

    public static final List<Food> foods = new ArrayList<>();

    private Vect pos;
    private double amount;

    public Food(Vect pos) {
        this.pos = pos;
        this.amount = Food.INIT_AMOUNT;

        Food.foods.add(this);
    }

    public Vect getPos() {
        return this.pos;
    }

    public void bite() {
        this.amount--;
//        if (this.amount <= 0)
//            Food.foods.remove(this);
    }
}
