package cookbook.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;


public class LeftMenuController {
    MainController tb = new MainController();

    
    @FXML
    private HBox beachBody;

    @FXML
    private HBox browseButton;

    @FXML
    private HBox byIngredientButton;

    @FXML
    private HBox byTagButton;

    @FXML
    private Label getFit;

    @FXML
    private HBox homeButton;

    @FXML
    private HBox newRecipe;

    @FXML
    private Label secretPleasure;

    @FXML
    private HBox shoppingList;

    @FXML
    private HBox weekSelectionButton;

    @FXML
    private HBox weeklyDinnerList;

    public void initialize() {
        /*browseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                tb.printAll();
            }
        });*/
    }

    @FXML
    void ShoppingListClicked(MouseEvent event) {

    }

    @FXML
    void beachBodyClicked(MouseEvent event) {

    }

    @FXML
    void browseClicked(MouseEvent event) {
        //tb.printAll();
    }

    @FXML
    void byIngredientClicked(MouseEvent event) {

    }

    @FXML
    void byTagClicked(MouseEvent event) {

    }

    @FXML
    void calendarClicked(MouseEvent event) {

    }

    @FXML
    void createRecipeClicked(MouseEvent event) {

    }

    @FXML
    void getFitClicked(MouseEvent event) {

    }

    @FXML
    void homeButtonClicked(MouseEvent event) {

    }

    @FXML
    void secretPleasureClicked(MouseEvent event) {

    }
    

    

    @FXML
    void weeklyDinnerClicked(MouseEvent event) {

    }

}
