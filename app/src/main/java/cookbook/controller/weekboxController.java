package cookbook.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.impl.conn.SystemDefaultRoutePlanner;

import cookbook.DBUtils;
import cookbook.Recipe;
import cookbook.User;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class weekboxController {

    @FXML
    private VBox weeksVBox, weekEditVBox;
    @FXML
    private Pane weekAnchorPane;
    @FXML
    private DatePicker weekPicker;
    @FXML
    private Pane weeksPane;
    @FXML
    private Button saveButton;
    @FXML
    private Label errorLabel;

    private Recipe recipe;
    private Stage stage;
    DBUtils db = DBUtils.getInstance();
    private User user = db.getUser();
    public void initialize() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
        try {
            Parent root = loader.load();
            RecipeBoxController rbc = loader.getController();
            
            // Calender so the user can choose what dates to add.
            weekPicker.setOnAction(Event -> {
                LocalDate date = weekPicker.getValue();
                HBox editDate = generateBox(date);
                weekEditVBox.getChildren().add(editDate);
                weekEditVBox.setPadding(new Insets(0, 0, 10, 0));

                
            });
            // Takes all the dates in the HBoxes of the recipe
            // And adds to the weekly list.
            saveButton.setOnAction(Event -> {
                try {
                    for (Node n : weekEditVBox.getChildren()) {
                        HBox weekEdit = (HBox) n;
                        TextField label = (TextField) weekEdit.getChildren().get(1);
                        LocalDate date = LocalDate.parse(label.getText());
                        DateTimeFormatter wtf = DateTimeFormatter.ofPattern("w");
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE", Locale.US);
                        int week = Integer.parseInt(date.format(wtf));
                        String day = date.format(dtf);
                        day = day.substring(0, 1).toUpperCase() + day.substring(1, day.length());
                        // Gets the recipes of the date.
                        ArrayList<Recipe> recipes = db.getWeeklyRecipes(week, user.getUserId(), day);
                        if (recipes.size() == 0) {
                            db.addtoWeekly(getRecipe(), user.getUserId(), week, day);
                        } else {
                            // Check if the recipe is already added to the date.
                            // if not added then add it to the date.
                            for (Recipe r : recipes) {
                                if (!(r.getRecipeId() == getRecipe().getRecipeId())) {
                                    db.addtoWeekly(getRecipe(), user.getUserId(), week, day);
                                    break;
                                }
                            }
                        }
                        
                        
                    }        
                    getStage().hide();
                } catch (DateTimeParseException e) {
                    errorLabel.setText("You entered a wrong date!");
                }
                
            });
            
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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

    /**
     * This method will set the proper recipe that the user chooses to add to.
     * The recipe comes from the last controller and is used to display
     * the recipe when the user selects what dates to add it to.
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
            Button button = (Button) pane.getChildren().get(pane.getChildren().size() - 2);
            VBox vbox = (VBox) pane.getChildren().get(0);
            HBox hbox = (HBox) vbox.getChildren().get(1);
            StackPane stackpane = (StackPane) hbox.getChildren().get(1);
            ImageView emptyHeart = (ImageView) stackpane.getChildren().get(0);
            ImageView heart = (ImageView) stackpane.getChildren().get(1);

            // If recipe is in favourite, make the heart red.
            if (isFavourite) {
                heart.setVisible(true);
                emptyHeart.setVisible(false);
            } else {
                heart.setVisible(false);
                emptyHeart.setVisible(true);
            }
            button.setDisable(true);
            String[] days = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            ArrayList<HBox> dates = new ArrayList<>();

            // Gets the dates that the current recipe is added to,
            // then create HBoxes with the dates so that the user knows where the recipe
            // is already added.
            for (int i = 1; i < 53; i++) {
                for (String day : days) {
                    ArrayList<Recipe> weeklyRecipes = db.getWeeklyRecipes(i, user.getUserId(), day);
                    for (Recipe recip: weeklyRecipes) {
                        if (weeklyRecipes.size() == 0 || weeklyRecipes.size() != 0) {
                            if (recipe.getRecipeId() == recip.getRecipeId()) {
                                int year = LocalDate.now().getYear();
                                // Get the date of the Monday in the given week number
                                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                                LocalDate date = LocalDate.of(year, 1, 1)
                                    .with(weekFields.weekOfYear(), i)
                                    .with(DayOfWeek.valueOf(day.toUpperCase()));
                                dates.add(generateBox(date));
                            }
                        }
                        
                    }
                }
            }
            for (HBox h : dates) {
                weekEditVBox.getChildren().add(h);
            }
            weeksVBox.getChildren().add(0, card);
            VBox.setMargin(card, new Insets(0, 0, 0, 10));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;

    }

    /**
     * This will create an HBox with a date on a label that corresponds to
     * the day the recipe is added to.
     *
     * @param date is the date 
     * @return an HBox with a label and an imageview.
     */
    public HBox generateBox(LocalDate date) {
        // Get the week of the date
        DateTimeFormatter wtf = DateTimeFormatter.ofPattern("w");
        // Get the day of the date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE", Locale.US);
        int week = Integer.parseInt(date.format(wtf));
        String day = date.format(dtf);
        TextField textField = new TextField(date.toString());
        textField.setPrefSize(100, 15);
        Label label = new Label("Added to: ");
        label.setPadding(new Insets(0, 0, 0, 5));
        textField.setOnAction(ActionEvent -> {
            textField.setEditable(false);
        });
        textField.setStyle("-fx-padding:5; -fx-Background-Color: lightblue;");
        textField.setEditable(false);
        try {
            Image delete = new Image(new FileInputStream("src/main/resources/util/Bin.png")); 
            ImageView deleteImage = new ImageView(delete);
            HBox editDate = new HBox(5, label, textField, deleteImage);
            HBox.setMargin(textField, new Insets(0, 0, 0, -8));
            
            editDate.setStyle("-fx-Background-Color: lightblue; -fx-Background-Radius: 10;");
            editDate.setMaxSize(250, 15);
            editDate.setAlignment(Pos.CENTER_LEFT);
            deleteImage.setFitHeight(15);
            deleteImage.setFitWidth(15);
            deleteImage.setCursor(Cursor.HAND);
            HBox.setMargin(deleteImage, new Insets(0, 5, 0, 0));

            // Deletes the recipe from the date.
            deleteImage.setOnMouseClicked(MouseEvent -> {
                for (Recipe r : db.getWeeklyRecipes(week, user.getUserId(), day)) {
                    if (r.getRecipeId() == getRecipe().getRecipeId()) {
                        db.removeFromWeekly(getRecipe(), user.getUserId(), week, day);
                    }
                }
                weekEditVBox.getChildren().remove(editDate);
            });
            return editDate;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
        
}