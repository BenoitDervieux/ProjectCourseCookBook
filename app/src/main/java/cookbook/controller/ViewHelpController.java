package cookbook.controller;



import java.util.ArrayList;
import cookbook.HelpInstruction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class ViewHelpController {
  @FXML
  private Label title;

  @FXML
  private VBox container;

  private ArrayList<HelpInstruction>  instructions =  new ArrayList<>();

  public void setTitle(String t){
    title.setText(t);
  }

  public String getTitle(){
    return title.getText();
  }

  public VBox getContainer(){
    return container;
  }

  public ArrayList<HelpInstruction> getInstructions() {
    return instructions;
  }

  public void setInstructions(ArrayList<HelpInstruction> instructions) {
    this.instructions = instructions;
  }

  public void loadInstructions(){
    int stepCount = 1;
    for (var i : instructions){
      Label step = new Label(stepCount+". "+i.getStep());
      step.setWrapText(true);
      step.setTextAlignment(TextAlignment.JUSTIFY);
      
      stepCount++;
      step.setStyle("-fx-font-size: 26px; -fx-padding: 5px;");
      ImageView imageView = new ImageView();
      try {
        Image image = new Image("/help/" + i.getImageID() + ".jpg");
        imageView.setImage(image);
        imageView.setFitWidth(500); // Set the desired width
        imageView.setPreserveRatio(true);
      } catch (Exception e) {
        System.out.println("Error at help image");
      }
      container.getChildren().add(step);
        container.getChildren().add(imageView);
    }
  }

  
}
