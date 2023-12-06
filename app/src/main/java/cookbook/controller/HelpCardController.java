package cookbook.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import cookbook.HelpInstruction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class HelpCardController {
  @FXML
  private ImageView heroImage;

  @FXML
  private Label title, duration, desc;

  @FXML
  private Button watchBtn;

  @FXML
  private Pane basePane;
  
  private ArrayList<HelpInstruction> instructions = new ArrayList<>();

  public void initialize(){

  }

  /**
   * This is load a help card.
   */
  public void showHelp(){
      FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ViewHelp.fxml"));
      try {
          Parent root = loader.load();
          ViewHelpController controller = loader.getController();
          controller.setTitle(title.getText());
          controller.setInstructions(instructions);
          controller.loadInstructions();
          Scene mainScene = title.getScene();
          Parent mainRoot = mainScene.getRoot();
          ScrollPane mainView = (ScrollPane) mainRoot.lookup("#cardScroll");
          mainView.setContent(root);
         
         
          // add an event handler to go to main screen
          mainScene.setOnKeyPressed(KeyEvent -> {
            if (KeyEvent.getCode() == KeyCode.ESCAPE) {
              FXMLLoader loaderMain = new FXMLLoader(getClass().getClassLoader().getResource("HelpCenter.fxml"));
              try {
                Parent currentRoot = loaderMain.load();
                mainView.setContent(currentRoot);
               
              } catch (Exception e) {
                System.out.println("Error in going back to the help center");
              }
             
            }
        });
         

         
      } catch (Exception error) {
        error.printStackTrace();
      }
  }


  public void setTitle(String text) {
    title.setText(text);
  }


  public void setImage(String path, String helpID) {
    InputStream stream;
    
    try {
        stream = new FileInputStream(path);
        Image image = new Image(stream);
        heroImage.setImage(image);

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}


  public Label getDuration() {
    return duration;
  }


  public void setDuration(String durationText) {
    duration.setText(durationText);
  }


  public Label getDesc() {
    return desc;
  }


  public void setDesc(String text) {
    desc.setText(text);
  }


  public ArrayList<HelpInstruction> getInstructions() {
    return instructions;
  }


  public void setInstructions(ArrayList<HelpInstruction> instructions) {
    this.instructions = instructions;
  }


}
