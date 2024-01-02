import java.util.ArrayList;
import java.util.List;

public class Recipe implements Comparable<Recipe> {
    private final String name;
    private List<FoodAmount> foodAmounts = new ArrayList<>();

    public Recipe(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addFoodAmount(FoodAmount foodAmount) {
        foodAmounts.add(foodAmount);
    }

    public List<FoodAmount> getFoodAmounts() {
        return foodAmounts;
    }

    public int compareTo(Recipe other) {
        return name.compareTo(other.name);
    }
}
