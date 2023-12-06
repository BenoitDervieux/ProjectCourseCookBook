package cookbook;

import cookbook.controller.LeftMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TheMenuBar extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MenuBarWithStage.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);

    LeftMenuController leftMenu = loader.getController();
    scene.getStylesheets().add(getClass().getResource("/MenuBar.css").toExternalForm());
    primaryStage.setTitle("Left Menu");
    primaryStage.setScene(scene);
    primaryStage.setResizable(true);
    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.show();
  }
  
  
}
