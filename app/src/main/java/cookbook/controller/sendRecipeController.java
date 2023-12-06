package cookbook.controller;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;

import cookbook.DBUtils;
import cookbook.Recipe;
import cookbook.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class sendRecipeController {
    private Stage stage;
    private Recipe recipe;
    private DBUtils db = DBUtils.getInstance();
    private User user = db.getUser();
    private ArrayList<User> recievers = new ArrayList<>();
    private String message;

    @FXML
    private VBox messageVBox, userVBox;

    @FXML
    private HBox userHBox;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea textMessage;
    @FXML
    private Label sentLabel;

    public void initialize() {

        sendButton.setOnAction(Event -> {
            db.sendRecipe(recipe, user, recievers, textMessage.getText());
            for (Node n : userVBox.getChildren()) {
                if (n instanceof CheckBox) {
                    CheckBox checkbox = (CheckBox) n;
                    checkbox.setSelected(false);
                }
                
            }
            recievers.clear();
            textMessage.clear();
            sentLabel.setText("The recipe has been sent!");

        });
    }
    /**
     * This method will set the proper recipe that the user chooses to send.
     * The recipe comes from the last controller and is used to display
     * the recipe when the user selects what users to send to.
     *
     * @param recipe is the recipe that is going to be added to the weekly list.
     */
    public void setCard(Recipe recipe) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
        try {
            ArrayList<Recipe> recipes = db.getFavouriteRecipes(user.getUserId());
            boolean isFavourite = false;
            // Checks if the recipe is in favourites, so the favourite heart will be empty or full.
            for (Recipe r : recipes) {
                if (recipe.getRecipeId() == r.getRecipeId()) {
                    isFavourite = true;
                }
            }
            Parent root = loader.load();
            RecipeBoxController rbc = loader.getController();
            // Creates the recipe card.
            Pane card = rbc.createCard(recipe);
            Pane pane = (Pane) card.getChildren().get(card.getChildren().size() - 1);
            Button button = (Button) pane.getChildren().get(pane.getChildren().size() - 1);
            Button button2 = (Button) pane.getChildren().get(pane.getChildren().size() - 2);
            button2.setDisable(true);
            button.setDisable(true);
            VBox vbox = (VBox) pane.getChildren().get(0);
            HBox hbox = (HBox) vbox.getChildren().get(1);
            StackPane stackpane = (StackPane) hbox.getChildren().get(1);
            ImageView emptyHeart = (ImageView) stackpane.getChildren().get(0);
            ImageView heart = (ImageView) stackpane.getChildren().get(1);

            ArrayList<User> users = db.getUsers();
            for (User u : users) {
                System.out.println(u.getUsername());
                CheckBox checkBox = new CheckBox(u.getUsername());
                checkBox.setStyle(":selected .box {-fx-Background-Color: black;}");
                checkBox.setOnAction(Event -> {
                    if (checkBox.isSelected()) {
                        recievers.add(u);
                    } else if (!checkBox.isSelected()) {
                        recievers.remove(u);
                    }
                    
                });
                userVBox.getChildren().add(checkBox);
                
            }

            // If recipe is in favourite, make the heart red.
            if (isFavourite) {
                heart.setVisible(true);
                emptyHeart.setVisible(false);
            } else {
                heart.setVisible(false);
                emptyHeart.setVisible(true);
            }         
            userHBox.getChildren().add(0, card);
            VBox.setMargin(card, new Insets(5, 0, 0, 10));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    private String getMessage() {
        return message;
    }
}
