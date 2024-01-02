public class FoodAmount {
    private final String foodName;
    private float amountNeeded = 0f;

    public FoodAmount(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public float getAmountNeeded() {
        return amountNeeded;
    }

    public void updateAmountNeeded(float amount) {
        this.amountNeeded += amount;
    }

}
