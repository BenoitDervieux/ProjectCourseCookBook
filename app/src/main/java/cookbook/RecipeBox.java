package cookbook;


import cookbook.controller.RecipeBoxController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RecipeBox extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {

    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);

    RecipeBoxController theRecipe = loader.getController();

    // Get the name from the database
    theRecipe.setName("Pot au feu");
    theRecipe.setLabel("French Stew with meat\nand vegetables");
    // null recipe for default
    theRecipe.setImage("src/main/resources/images/Pot au feu.jpg", null);

    scene.getStylesheets().add(getClass().getResource("/MenuBar.css").toExternalForm());
    primaryStage.setTitle("Recipe Box");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
}
