package cookbook.controller;

import java.io.IOException;
import java.util.ArrayList;
import cookbook.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


public class TagsRecipeController {

    @FXML
    private AnchorPane theMainPane;

    @FXML
    private Label tagName;

    @FXML
    private ScrollPane thePane;

    public void initialize() {
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        int gPaneColumn = 0;
        int gPanerow = 0;
        int gPaneGeneral = 0;

        GridPane theGrid = new GridPane();
        thePane.setContent(theGrid);
        for (Recipe e : recipes) {
            gPaneGeneral++;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
                Parent card = fxmlLoader.load();
                RecipeBoxController recipeToAdd = fxmlLoader.getController();
                recipeToAdd.setRecipe(e);
                theGrid.add(card, gPaneColumn, gPanerow, 1, 1);
                gPaneColumn++;
            if (gPaneColumn % 4 == 0 && gPaneGeneral != 0) {
                gPaneColumn = gPaneColumn % 4;
                gPanerow++;
            }
            } catch (IOException p) {
                p.printStackTrace();
            }

        }
        theGrid.setHgap(10);
        theGrid.setVgap(10);
    }

    public void setName(String name) {
        tagName.setText("#" + name);
    }
    
}
