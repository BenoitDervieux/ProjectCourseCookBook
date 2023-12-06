package cookbook.controller;

import java.io.IOException;
import java.util.ArrayList;

import cookbook.DBUtils;
import cookbook.Message;
import cookbook.Recipe;
import cookbook.RecipeMessage;
import cookbook.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserMessagesController {
    
    @FXML
    private AnchorPane messagePane;

    DBUtils db = DBUtils.getInstance();
    
    private Stage stage;
    private User user = db.getUser();
    ArrayList<RecipeMessage> messages =  db.getUserMessages(user.getUserId());
    public void initialize() {

    }


    private VBox generateMessage(User user, Recipe recipe, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
            Parent root = loader.load();
            RecipeBoxController rb = loader.getController();
            Pane card = rb.createCard(recipe);
            // Gets all the users favourites.
            ArrayList<Recipe> favRecipes = db.getFavouriteRecipes(this.user.getUserId());
            for (Recipe r : favRecipes) {
                if (favRecipes.stream().anyMatch(fav -> fav.getRecipeId() == r.getRecipeId())) {
                    Pane pane = (Pane) card.getChildren().get(1);
                    VBox vbox = (VBox) pane.getChildren().get(0);
                    HBox hbox = (HBox) vbox.getChildren().get(1);
                    StackPane stackpane = (StackPane) hbox.getChildren().get(1);
                    ImageView heart = (ImageView) stackpane.getChildren().get(1);
                    ImageView emptyHeart = (ImageView) stackpane.getChildren().get(0);
                    heart.setVisible(true);
                    emptyHeart.setVisible(false);
                }
            }

            Label userLabel = new Label(user.getUsername() + " sent you a recipe!");
            userLabel.setStyle("-fx-padding: 3, 5, 3, 5; -fx-background-radius: 15; -fx-background-color: lightblue;");
            Label recipeLabel = new Label("Recipe");
            recipeLabel.setStyle("-fx-font-weight: bolder;");
            Label messageLabel = new Label("Message");
            messageLabel.setStyle("-fx-font-weight: bolder;");
            Label theMessage = new Label(message);
            theMessage.setPrefSize(200, 150);
            theMessage.setAlignment(Pos.TOP_LEFT);
            theMessage.setPadding(new Insets(5, 10, 5, 10));
            theMessage.setStyle("-fx-border-color: #CCC; -fx-border-radius: 20; -fx-wrap-text: true;");
            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-radius: 40; -fx-background-color: red;" 
                                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 0), inner-shadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 0);");
            
            VBox container = new VBox(10, userLabel, recipeLabel, card, messageLabel, theMessage, deleteButton);
            container.setStyle("-fx-border-color: #CCC; -fx-radius:10;");
            container.setMaxWidth(200);
            container.setPadding(new Insets(5, 5, 5, 5));
            VBox.setMargin(deleteButton, new Insets(0, 0, 0, 65));
            deleteButton.setOnAction(Event -> {
                db.removeUserMessage(this.user, user, recipe, message);
                messagePane.getChildren().remove(container);
                messagePane.getChildren().clear();
                ArrayList<RecipeMessage> messages = db.getUserMessages(db.getUser().getUserId());
                printMessages(messages);
            });
            return container;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
    }

    public void printMessages(ArrayList<RecipeMessage> messages) {
        if (messages.size() == 0) {
            Label label = new Label("You have no messages!");
            label.setStyle("-fx-font-size: 16; -fx-font-weight: bolder;");
            messagePane.getChildren().add(label);
            label.setLayoutX(345);
            label.setLayoutY(300);
        } else {
            double xLayout = 0;
            double yLayout = 0;
            for (RecipeMessage r : messages) {
                VBox messageBox = generateMessage(db.getOneUser(r.getSenderId()), r.getRecipe(), r.getMessage());
                if (messagePane.getChildren().size() == 0) {
                    messagePane.getChildren().add(messageBox);
                } else if ((messages.indexOf(r) % 4) == 0) {
                    xLayout = 0;
                    Node node = messagePane.getChildren().get(messagePane.getChildren().size() - 1);
                    double size = node.getBoundsInLocal().getHeight();
                    yLayout = size + 330;
                }
                else {
                    xLayout = messagePane.getChildren().get(messagePane.getChildren().size() - 1).getLayoutX() + 210;
                }
                if (!messagePane.getChildren().contains(messageBox)) {
                    messagePane.getChildren().add(messageBox);
                }
                messageBox.setLayoutX(xLayout);
                messageBox.setLayoutY(yLayout);

            }
        }
    }
    

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
