import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MealWorkbook {

    private final XSSFWorkbook mealWorkbook;
    private Map<String, Recipe> recipeMap;
    private Map<Recipe, Integer> chosenRecipes = new TreeMap<>();
    private Map<String, FoodAmount> chosenRecipeFoodAmounts;

    public MealWorkbook() throws IOException, InvalidFormatException {
        File mealWorbookFile = new File("src/main/resources/MealWorkbook.xlsx");
        mealWorkbook = new XSSFWorkbook(mealWorbookFile);
    }

    public void readWorkbook() {
        recipeMap = gatherRecipes(mealWorkbook.getSheet("Meals"));
    }

    public String getNeededFoodString() {
        chosenRecipeFoodAmounts = getNeededFoodAmounts(chosenRecipes);
        String neededFoodString = printNeededFoodAmounts();
        chosenRecipes = new TreeMap<>();
        return neededFoodString;
    }

    public void chooseRecipe(Recipe recipe) {
        if (!chosenRecipes.containsKey(recipe)) {
            chosenRecipes.put(recipe, 1);
        } else {
            int currentRecipeAmount = chosenRecipes.get(recipe) + 1;
            chosenRecipes.replace(recipe, currentRecipeAmount);
        }
    }

    public Map<String, Recipe> getRecipeMap() {
        return recipeMap;
    }

    public void printRecipes() {
        for (Recipe recipe : recipeMap.values()) {
            System.out.print(recipe.getName() + " - ");
            for (FoodAmount foodAmount : recipe.getFoodAmounts()) {
                System.out.print(foodAmount.getFoodName() + ": " + foodAmount.getAmountNeeded() + ", ");
            }
            System.out.println();
        }
    }

    public String printNeededFoodAmounts() {
        String neededFoodString = "Food needed:\n";
        for (FoodAmount foodAmount : chosenRecipeFoodAmounts.values()) {
            DecimalFormat df = new DecimalFormat("0.#");
            neededFoodString += foodAmount.getFoodName() + ": " + df.format(foodAmount.getAmountNeeded()) + "\n";
        }
        return neededFoodString;
    }

    private Map<String, Recipe> gatherRecipes(Sheet sheet) {
        Map<String, Recipe> recipeMap = new TreeMap<>();
        for (Row row : sheet) {
            try {
                Recipe recipe = readRow(row);
                if (recipe != null) {
                    recipeMap.put(recipe.getName(), recipe);
                }
            } catch (Exception ignored) {}  //Ignore rows that aren't valid
        }

        return recipeMap;
    }

    private Recipe readRow(Row row) {
        Iterator<Cell> rowIter = row.iterator();
        Cell cell = rowIter.next();
        Recipe recipe = new Recipe(cell.getStringCellValue());
        while (rowIter.hasNext()) {
            cell = rowIter.next();
            String cellString = cell.getStringCellValue();
            FoodAmount foodAmount = parseFoodAmount(cellString);
            if (foodAmount == null) {
                return null;
            }
            recipe.addFoodAmount(foodAmount);
        }

        return recipe;
    }

    private FoodAmount parseFoodAmount(String foodAmountString) {
        String[] splitString = foodAmountString.split(":");
        if (splitString.length != 2) {
            System.out.println("Could not parse string: " + foodAmountString);
            return null;
        }
        try {
            FoodAmount foodAmount = new FoodAmount(splitString[0].trim());
            float amount = Float.parseFloat(splitString[1].trim());
            foodAmount.updateAmountNeeded(amount);

            return foodAmount;
        } catch (Exception e) {
            System.out.println("Could not parse string: " + foodAmountString);
            return null;
        }
    }

    private Map<String, FoodAmount> getNeededFoodAmounts(Map<Recipe, Integer> chosenRecipes) {
        Map<String, FoodAmount> neededFoodAmounts = new TreeMap<>();
        for (Map.Entry<Recipe, Integer> entry : chosenRecipes.entrySet()) {
            Recipe recipe = entry.getKey();
            float amount = (float) entry.getValue();
            for (FoodAmount foodAmount : recipe.getFoodAmounts()) {
                float multipliedAmount = foodAmount.getAmountNeeded() * amount;
                if (!neededFoodAmounts.containsKey(foodAmount.getFoodName())) {
                    FoodAmount newFoodAmount = new FoodAmount(foodAmount.getFoodName());
                    newFoodAmount.updateAmountNeeded(multipliedAmount);
                    neededFoodAmounts.put(newFoodAmount.getFoodName(), newFoodAmount);
                } else {
                    neededFoodAmounts.get(foodAmount.getFoodName()).updateAmountNeeded(multipliedAmount);
                }
            }
        }

        return neededFoodAmounts;
    }
}
