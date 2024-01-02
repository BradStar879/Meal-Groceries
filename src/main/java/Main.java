import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InvalidFormatException {
        MealWorkbook mealWorkbook = new MealWorkbook();
        mealWorkbook.readWorkbook();
        Window window = new Window();
        window.loadRecipeChoices(mealWorkbook);
        window.displayWindow();
    }
}
