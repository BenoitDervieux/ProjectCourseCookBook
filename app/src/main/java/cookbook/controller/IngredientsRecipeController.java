package cookbook.controller;

import java.io.IOException;
import java.util.ArrayList;

import cookbook.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class IngredientsRecipeController {

    @FXML
    private AnchorPane theMainPane;

    @FXML
    private Label tagName;

    @FXML
    private Pane thePane;

    public void initialize() {
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        int gPaneColumn = 0;
        int gPanerow = 0;
        int gPaneGeneral = 0;

        GridPane theGrid = new GridPane();
        thePane.getChildren().add(theGrid);
        for (Recipe e : recipes) {
            //thePane.setPrefHeight( (gPanerow + 1) * 300);
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
        tagName.setText("Ingredient: " + name);
    }
    
}
