package cookbook.controller;

import cookbook.DBUtils;
import cookbook.DatabaseConnection;
import cookbook.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;


public class CartIngredientsController {

  private User user;

  private DatabaseConnection db;

  @FXML
  private ImageView foodIMG;
  @FXML
  private Label ingredLabel;
  @FXML
  private Label amountLabel;

  @FXML
  private Circle redCircle;
  @FXML
  private Circle hoverCircle;
  @FXML
  private ImageView deleteImage;

  @FXML
  private Button minusButton, plusButton;
  @FXML
  private TextField amountTextField;

  @FXML
  private TextArea notesArea;

  @FXML
  private ComboBox<String> recipeComboBox;

  String ingredientName = "";
  String ingredientAmount = "";
  int ingredientID;
  int userID;
  int week;
  int shopID;

  private DBUtils dbU = DBUtils.getInstance();

  private AnchorPane cardAnchor;
  
  public CartIngredientsController() {
    this.db = new DatabaseConnection();
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setCardAnchor(AnchorPane cardAnchor) {
    this.cardAnchor = cardAnchor;
  }

  public void initialize() throws Exception {
    
    notesArea.setVisible(false);
    recipeComboBox.setVisible(false);

    recipeComboBox.setOnAction(
    new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String recipe = recipeComboBox.getSelectionModel().getSelectedItem();
        System.out.println(recipe);
      }
    });

    // This is the hoover for when the mouse is on the round
    Interpolator interpolator = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);
    hoverCircle.setOnMouseEntered(onmouse -> {
      Timeline growDeleteImageX = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(deleteImage.scaleXProperty(), 0)),
          new KeyFrame(Duration.millis(200), new KeyValue(deleteImage.scaleXProperty(), 3, interpolator)));
      growDeleteImageX.play();
      Timeline growDeleteImageY = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(deleteImage.scaleYProperty(), 0)),
          new KeyFrame(Duration.millis(200), new KeyValue(deleteImage.scaleYProperty(), 3, interpolator)));
      growDeleteImageY.play();
      FadeTransition fadeSmallDot = new FadeTransition(Duration.millis(100), redCircle);
      fadeSmallDot.setFromValue(1);
      fadeSmallDot.setToValue(0);
      fadeSmallDot.play();
    });
    // This is the animation for when the mouse exists the round
    hoverCircle.setOnMouseExited(onmouse -> {
      Timeline shrinkDeleteImageX = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(deleteImage.scaleXProperty(), 3)),
          new KeyFrame(Duration.millis(200), new KeyValue(deleteImage.scaleXProperty(), 0, interpolator)));
      shrinkDeleteImageX.play();
      Timeline shrinkDeleteImageY = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(deleteImage.scaleYProperty(), 3)),
          new KeyFrame(Duration.millis(200), new KeyValue(deleteImage.scaleYProperty(), 0, interpolator)));
      shrinkDeleteImageY.play();
      FadeTransition fadeSmallDot = new FadeTransition(Duration.millis(100), redCircle);
      fadeSmallDot.setFromValue(0);
      fadeSmallDot.setToValue(1);
      fadeSmallDot.play();
    });

    // Delete Ingredient
    hoverCircle.setOnMouseClicked(event -> {
      //String ingredientName = ingredLabel.getText();

      String delShoppingQuery = "DELETE FROM chanuka1.shopping WHERE ingredient_id = ?";
      
      /* String delShoppingQuery = "DELETE FROM chanuka1.shopping WHERE ingredient_id IN "
                                + "(SELECT ingredient_id FROM chanuka1.ingredients WHERE name = ?)"; */

      // Delete the ingredient from the shopping table
      try {
        Connection connection = db.getDBConnection();

        PreparedStatement shoppingStatement = connection.prepareStatement(delShoppingQuery);
        shoppingStatement.setInt(1, ingredientID);
        //shoppingStatement.executeUpdate();

        // Remove the card counter, if less rows = del.
        int rowDeleted = shoppingStatement.executeUpdate();
        if (rowDeleted > 0) {
          System.out.println("Deleted");

          // Remove user Card displaying
          Node cardRemove = hoverCircle.getParent();
          Pane container = (Pane) cardRemove.getParent();
          container.getChildren().remove(cardRemove);
        } else {
          System.out.println("Card not deleted, error");
        }

        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    // For amount text field, if user adds manually , update on focus lost
    amountTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue) {  
          // Focus lost textArea
          String newAmount = amountTextField.getText().trim();
          updateAmountShopping(newAmount);
      }
    });

    // Focus for notes area, updates on focus lost
    notesArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue) {  
          // Focus lost textArea
          String newNote = notesArea.getText();
          updateNotes(newNote);
      }
    });

    //For + Button : Delay database update until focus lost
    plusButton.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue) {
          // Focus lost
          String newAmount = amountTextField.getText().trim();
          updateAmountShopping(newAmount);
      }
    });

    //For - Button : Delay database update until focus lost
    minusButton.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue) {
          // Focus lost
          String newAmount = amountTextField.getText().trim();
          updateAmountShopping(newAmount);
      }
    });

    //Button increase amount
    plusButton.setOnAction(event -> {
      String currentAmount = amountTextField.getText().trim();
      try {
        Double newAmount = Double.parseDouble(currentAmount) + 1;
        String amount = Double.toString(newAmount);
        amountTextField.setText(amount);

      } catch (Exception e) {
        Alert invalid = new Alert(AlertType.ERROR, "Invalid input");
        invalid.showAndWait();
        
        System.out.println("Invalid input.");
      }

    });

    //Button decrease amount
    minusButton.setOnAction(event -> {
      String currentAmount = amountTextField.getText().trim();
      try {
        Double newAmount = Double.parseDouble(currentAmount) - 1;
        if (newAmount > 0) {
          String amount = Double.toString(newAmount);
          amountTextField.setText(amount);
        } else {
          Alert invalid = new Alert(AlertType.ERROR, "Invalid input, cannot be lower than 1");
          invalid.showAndWait();
        }
      } catch (Exception e) {
        Alert invalid = new Alert(AlertType.ERROR, "Invalid input");
        invalid.showAndWait();
        
        System.out.println("Invalid input.");
      }
      
    });

  }

  //set comboBox recipes, on ingredient name, user id and week
  public void setComboItems(String ingredientName, int userID) {
    
    //Gets all the users recipes from the weekly table in the database.
    List<String> recipes = dbU.getWeeklyRecipeOfIng(this.week, userID, ingredientName);
    recipeComboBox.setItems(FXCollections.observableArrayList(recipes));
  }

  public void setInfo(String name, int ingredientID, String amount, int userID, int shopid) {
    ingredientName = name;
    ingredientAmount = amount;
    this.ingredientID = ingredientID;
    this.userID = userID;
    this.shopID = shopid;
    
    //Add to labels
    ingredLabel.setText(name.toUpperCase());
    amountTextField.setText(amount);

    //Set notes box
    //notesArea.setText(notes);

    //Load and set img for ingredient by ingredient name
    String imageName = name.toLowerCase() + ".png";
    String imagePath = "src/main/resources/ingredient_imgs/" + imageName;
    this.setImage(imagePath);

    //set week
    Calendar calendar = Calendar.getInstance();
    int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
    this.week = currentWeek;

    // to pass ingredient name and userid
    setComboItems(name, userID);
  }

  //Ingredient Image
  public void setImage(String path) {

    try {
      InputStream stream = new FileInputStream(path);
      Image image = new Image(stream);
      foodIMG.setImage(image);

    } catch (FileNotFoundException e) {
      // e.printStackTrace();
      System.out.println("Image not found: " + this.ingredientName);
      setDefaultImg();
    }
  }

  //Ingredient Image
  public void setDefaultImg() {
    String defaultImg = "src/main/resources/ingredient_imgs/defaultimg.png";
    try {
      InputStream stream = new FileInputStream(defaultImg);
      Image defImage = new Image(stream);
      foodIMG.setImage(defImage);
    } catch (FileNotFoundException e) {
      // e.printStackTrace();
      System.out.println("Default img not found: " + defaultImg);
    }
  }

  // Update amount in shopping table for + and - buttons
  public void updateAmountShopping(String newAmount) {
    String updQuery = "UPDATE chanuka1.shopping SET i_amount = ? WHERE ingredient_id = ?";

    try {
      Connection connect = db.getDBConnection();
      PreparedStatement statement = connect.prepareStatement(updQuery);
      statement.setString(1, newAmount);
      statement.setInt(2, this.ingredientID);
      /* statement.setInt(2, this.userID);
      statement.setString(3, this.ingredientID);
      statement.setInt(4, this.week); */

      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated > 0) {
        System.out.println("Amount updated in the database.");
      } else {
        System.out.println("Failed to update i_amount in the database.");
      }
      connect.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public void updateNotes(String notes) { 
    String noteQuery = "UPDATE chanuka1.shop_ingredients SET notes = ? WHERE shop_ingredient = ?";
    System.out.println(notes);

    try  {
      Connection connect = db.getDBConnection();
      PreparedStatement statement = connect.prepareStatement(noteQuery);
      statement.setString(1, notes);
      statement.setInt(2, this.shopID);
      /* statement.setInt(2, this.userID);
      statement.setString(3, this.ingredientID);
      statement.setInt(4, this.week); */

      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated > 0) {
        System.out.println("Notes updated in the database.");
      } else {
        System.out.println("Failed to update notes in the database.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

    
}
