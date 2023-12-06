package cookbook.controller;

import java.io.IOException;
import java.util.ArrayList;
import cookbook.DBUtils;
import cookbook.HelpHeading;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;


public class GetHelpController {

  private DBUtils db = DBUtils.getInstance();

  @FXML
  private Label tutorial;

  @FXML
  private Label contact;

  @FXML
  private AnchorPane helpAnchor;

  @FXML
  ScrollPane helpScroll;

  @FXML
  private TextField searchBar;




  double chosenLayout  = 176.0;
  double unChosenLayout = 179.0;
  String chosenStyle = "-fx-Background-Color: #212529; -fx-alignment: center; -fx-background-radius: 5, 5, 0, 0; -fx-pref-width: 88; -fx-pref-height: 29; -fx-font-size: 16.5";
  String notChosenStyle = "-fx-Background-Color: #adb5bd; -fx-alignment: center; -fx-background-radius: 5, 5, 0, 0; -fx-pref-width: 86; -fx-pref-height: 27;";

  String color =  "#212529";
  public void initialize() throws Exception {
    tutorial.setStyle(chosenStyle);
    tutorial.setLayoutY(chosenLayout);
    loadHelp();


    tutorial.setOnMouseClicked(e -> {

      loadHelp();
      
     
      tutorial.setStyle(chosenStyle);
      tutorial.setLayoutY(chosenLayout);
      contact.setStyle(notChosenStyle);
      contact.setLayoutY(unChosenLayout);  
      
    });


    contact.setOnMouseClicked(e -> {

      loadAboutUs();
      
      tutorial.setStyle(notChosenStyle);
      tutorial.setLayoutY(unChosenLayout);
      contact.setStyle(chosenStyle);
      contact.setLayoutY(chosenLayout);

    });

    searchBar.setOnAction(Event -> {
      String search = searchBar.getText();
      ArrayList<Parent> helpCards = helpSearch(search);
      if (helpCards != null) {
        if (helpCards.size() != 0) {
          VBox container = new VBox();
          container.setPrefWidth(helpScroll.getWidth());
          int gPaneColumn = 0;
          int gPanerow = 0;
          int gPaneGeneral = 0;
          GridPane theGrid = new GridPane();
          container.getChildren().addAll(theGrid);
          theGrid.setStyle("-fx-Background-Color: white;");
          container.setStyle("-fx-Background-Color: white;");
          for (var i : helpCards){
            gPaneGeneral++;
            theGrid.add(i, gPaneColumn, gPanerow, 1, 1);
            gPaneColumn++;
            if (gPaneColumn % 3 == 0 && gPaneGeneral != 0) {
              gPaneColumn = gPaneColumn % 3;
              gPanerow++;
            }
            theGrid.setHgap(10);
            theGrid.setVgap(10);
            helpScroll.setContent(container);
          }
        }
      }
      
    });

  }


  private void loadHelp(){
    ArrayList<HelpHeading> arr = db.getHelpData();
    VBox container = new VBox();
    container.setPrefWidth(helpScroll.getWidth());
    int gPaneColumn = 0;
    int gPanerow = 0;
    int gPaneGeneral = 0;
    GridPane theGrid = new GridPane();
    container.getChildren().addAll(theGrid);
    theGrid.setStyle("-fx-Background-Color: white;");
    container.setStyle("-fx-Background-Color: white;");
    for (var i : arr){
      gPaneGeneral++;
      try {
        // Gets the user object from the stage.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/HelpCard.fxml"));
        Parent helpCard = loader.load();
        HelpCardController controller = loader.getController();
        controller.setTitle(i.getHeading());
        controller.setInstructions(i.getSetOfInstructions());
        theGrid.add(helpCard, gPaneColumn, gPanerow, 1, 1);
        gPaneColumn++;
        if (gPaneColumn % 3 == 0 && gPaneGeneral != 0) {
          gPaneColumn = gPaneColumn % 3;
          gPanerow++;
        }
        
      } catch (IOException err) {
        // TODO Auto-generated catch block
        err.printStackTrace();
      }

      theGrid.setHgap(10);
      theGrid.setVgap(10);
      helpScroll.setContent(container);
    }
    
    
  }


  private void loadAboutUs() {
    try {
      Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("AboutUs.fxml"));
      helpScroll.setContent(root);
    } catch (IOException p) {
      p.printStackTrace();
    }
  }


  /**
   * Helps search for a help heading.
   *
   * @param title the heading to search.
   * @return relevant help cards.
   */
  public ArrayList<Parent> helpSearch(String title) {
    if (title.isBlank()) {
      loadHelp();
      return null;
    }
    try {
      String newTitle = "";
      if (title.length() != 1) {
        newTitle += Character.toUpperCase(title.toCharArray()[0]);
        boolean whiteSpace = false;
        for (char c : title.substring(1, title.length()).toCharArray()) {
          if (Character.isWhitespace(c)) {
            newTitle += c;
            whiteSpace = true;
            continue;
          } else if (whiteSpace == true) {
            newTitle += Character.toUpperCase(c);
            whiteSpace = false;
            continue;
          } else {
            newTitle += c;
            continue;
          }
        }
      }
      if (title.length() == 1) {
        newTitle = title;
      }
      ArrayList<HelpHeading> titles = db.getHelpData();
      HelpHeading helpheading = null;
      ArrayList<Parent> cards = new ArrayList<>();
      for (HelpHeading h : titles) {
        if (h.getHeading().contains(newTitle)) {
          helpheading = h;
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/HelpCard.fxml"));
          Parent helpCard = loader.load();
          HelpCardController controller = loader.getController();
          controller.setTitle(helpheading.getHeading());
          controller.setInstructions(helpheading.getSetOfInstructions());
          cards.add(helpCard);
        }
      }
      return cards;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
