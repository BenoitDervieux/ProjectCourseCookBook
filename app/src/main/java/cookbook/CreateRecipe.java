package cookbook;

import cookbook.controller.CreateRecipeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CreateRecipe extends Application {

    private AnchorPane cardAnchor;


    public CreateRecipe(AnchorPane cardAnchor) {
        this.cardAnchor = cardAnchor;
    
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CreateRecipe.fxml"));
            Parent root = loader.load();
            
            
            CreateRecipeController createRecipe = loader.getController();
            createRecipe.setUserData((User) cardAnchor.getScene().getUserData());
            createRecipe.setStage(primaryStage);
            createRecipe.setCardAnchor(cardAnchor);
            Scene scene = new Scene(root);
            
            primaryStage.setTitle("EDIT RECIPE");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    
    
}
