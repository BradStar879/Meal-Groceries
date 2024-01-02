import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class Window {

    private final JFrame frame;
    //private final JPanel outerPanel;

    public Window() {
        frame = new JFrame("Grocery List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setLocationRelativeTo(null);
    }

    public void loadRecipeChoices(MealWorkbook mealWorkbook) {
        Map<String, Recipe> recipeMap = mealWorkbook.getRecipeMap();

        JPanel panel = new JPanel();
        frame.add(panel);

        JLabel label = new JLabel("Select your recipes");
        panel.add(label);

        String[] recipeNames = new String[recipeMap.size()];
        System.arraycopy(recipeMap.keySet().toArray(), 0, recipeNames, 0, recipeMap.size());
        JComboBox<String> cb = new JComboBox<>(recipeNames);
        panel.add(cb);

        JButton button = new JButton("Add");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Recipe recipe = recipeMap.get((String) cb.getSelectedItem());
                mealWorkbook.chooseRecipe(recipe);
            }
        });
        panel.add(button);

        JButton button2 = new JButton("Calculate");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayFoodNeeded(mealWorkbook);
            }
        });
        panel.add(button2);
    }

    public void displayWindow() {
        frame.setVisible(true);
        frame.pack();
    }

    private void displayFoodNeeded(MealWorkbook mealWorkbook) {
        JPanel panel = new JPanel();
        String neededFoodString = mealWorkbook.getNeededFoodString();
        JLabel foodDisplay = new JLabel(neededFoodString);
        panel.add(foodDisplay);
        frame.add(panel, 1);
        frame.repaint();
        frame.setVisible(true);
    }

}
