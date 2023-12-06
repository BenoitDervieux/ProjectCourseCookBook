package cookbook.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cookbook.DBUtils;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdminControls {

  private DBUtils db;

  @FXML
  private Label addUser, successfulLogin;

  @FXML
  private Label deleteUser, errorLabel;

  @FXML
  private AnchorPane adminAnchor;

  @FXML
  private AnchorPane cardAnchor;

  @FXML
  private ScrollPane cardScroll;

  @FXML
  private TextField userName;

  @FXML
  private TextField email;

  @FXML
  private TextField password;

  @FXML
  private Button createUserButton;

  @FXML
  private ImageView showPasswordIcon;

  @FXML
  private StackPane passwordStack;

  @FXML
  private ImageView hidePasswordIcon;

  @FXML
  private TextField showPassword;

  @FXML
  private void resetBorder(MouseEvent event) {
    TextField[] details = new TextField[] {password, email, userName};
    
    for (TextField tf : details) {
      if (tf.isFocusWithin()) {
        tf.setBorder(null);
      }
    }

  }

  public AdminControls() {
    this.db = DBUtils.getInstance();
  }

  public void initialize() throws Exception {

    password.setOnAction(Event -> {
      if (register(email, userName, password) == true) {
        userName.clear();
        email.clear();
        password.clear();
        showPassword.clear();
        errorLabel.setText("User successfully created!");
        String text = showPassword.getText();
        showPassword(text);
      }

    });

    if (addUser.getFont().getSize() == 18.0 ) {
      Timeline fadeAddUser = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(addUser.opacityProperty(), 0.3)),
          new KeyFrame(Duration.millis(300), new KeyValue(addUser.opacityProperty(), 1))
      );
      fadeAddUser.play();
      Timeline fadeDeleteUser = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(deleteUser.opacityProperty(), 1)),
          new KeyFrame(Duration.millis(300), new KeyValue(deleteUser.opacityProperty(), 0.3))
      );
      fadeDeleteUser.play();
    } else {
      Timeline fadeAddUser = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(addUser.opacityProperty(), 1)),
          new KeyFrame(Duration.millis(300), new KeyValue(addUser.opacityProperty(), 0.3))
      );
      fadeAddUser.play();
      Timeline fadeDeleteUser = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(deleteUser.opacityProperty(), 0.3)),
          new KeyFrame(Duration.millis(300), new KeyValue(deleteUser.opacityProperty(), 1))
      );
      fadeDeleteUser.play();
    }

    deleteUser.setOnMouseClicked(e -> {
      try {

        toTheModify(e);
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    });

    addUser.setOnMouseClicked(e -> {
      try {
        toTheAdd(e);
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    });


    createUserButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (register(email, userName, password) == true) {
          userName.clear();
          email.clear();
          password.clear();
          showPassword.clear();
          errorLabel.setText("User successfully created!");
          String text = showPassword.getText();
          showPassword(text);
        }

      }

    });


    // Makes the password visible for the user when pressed.
    showPasswordIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        String text = showPassword.getText();
        showPassword(text);
    
      }

    });

    // Makes the password invisible for the user when pressed.
    hidePasswordIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        String text = password.getText();
        showPassword.setText(text);
        showPassword.setVisible(true);
        password.setVisible(false);
        hidePasswordIcon.setVisible(false);
        showPasswordIcon.setVisible(true);
        showPassword.positionCaret(text.length());
      }

    });


  }

  public void toTheModify(MouseEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Admin_Panel_Modify_Delete.fxml"));
    Scene scene = deleteUser.getScene();
    adminAnchor.getChildren().add(root);
  }

  public void toTheAdd(MouseEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Admin_Panel_Add.fxml"));
    Scene scene = addUser.getScene();
    adminAnchor.getChildren().add(root);
  }

 /*  private void createUser() {
    try {
      register(email, userName, password);
    } catch (IllegalArgumentException e) {
      errorLabel.setText(e.getMessage());
      //remove(errorLabel);

    }
  }*/
  private boolean register(TextField email, TextField userName, TextField password) {
    TextField[] details = new TextField[] {email, userName, password};
    // Checks if the fields are empty
    // Sets border to red if empty else set border to empty
    for (TextField tf : details) {
      if (tf.getText().isBlank()) {
        tf.setBorder(new Border(
            new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        errorLabel.setText("Empty fields!");
        
      } else {
        tf.setBorder(Border.EMPTY);
      }
    }
    // Checks if fields are empty after for loop
    for (TextField tf : details) {
      if (tf.getText().isBlank()) {
        return false;
      }
    }
    // Checks if the email is valid.
    if (!valEmail(email.getText())) {
      email.setBorder(new Border(
          new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
      errorLabel.setText("Please enter a valid email!");
      return false;

    }
    
    if (password.getText().length() < 6) {
      // set the style of the TextField to make it appear red
        password.setBorder(new Border(
          new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
      errorLabel.setText("Please enter a valid password!");
      return false;
    }
  
    try {
      // Sets the scene back to the login scene, after user creates account
      // Checks registration with database
      db.checkRegistration(email, userName, password);
      return true;
    } catch (IllegalArgumentException e) {
      errorLabel.setText(e.getMessage());
      return false;
    } catch (Exception e) {
      System.out.println("An error has occured: " + e.getMessage());
      return false;
    }
  }

  private Boolean valEmail(String email) {
    String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = emailPattern.matcher(email);
    return matcher.find();
  }

  public void showPassword(String text) {
    password.setText(text);
    password.setVisible(true);
    showPassword.setVisible(false);
    hidePasswordIcon.setVisible(true);
    showPasswordIcon.setVisible(false);
    password.positionCaret(text.length());

  }
}
