package cookbook.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.impl.conn.SystemDefaultRoutePlanner;

import cookbook.DBUtils;
import cookbook.DatabaseConnection;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class CardsModifyDeleteUserController {

    private DBUtils dbU = DBUtils.getInstance();

    private DatabaseConnection db;

    @FXML
    private Circle circlePicture;
    @FXML
    private ImageView imageProfile;


    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    @FXML
    private Circle redCircle;
    @FXML
    private Circle hoverCircle;
    @FXML
    private ImageView deleteImage;

    @FXML
    private Pane paneAdminCard;

    String nameT = ""; 
    String emailT = "";
    String passwordT = ""; 
    String primaryKey = "";
    String displayPassword = "********";
    StringBuilder realPass = new StringBuilder();
    
    /**
     * Constructor that establishes a connection.
     */
    public CardsModifyDeleteUserController() {
        this.db = new DatabaseConnection();
    }
    public void initialize() {

        //ImageView profilePicture = new ImageView(new Image("images/1619378021617.jpg"));
        Circle cercle = new Circle(19, 22, 18, Color.RED);
        imageProfile.setClip(cercle);
        
        // Name label and textField
        nameField.setEditable(false);
        nameField.setVisible(false);
        nameLabel.setVisible(true);
        nameLabel.setText(nameT);

        // Email label and textField
        emailField.setEditable(false);
        emailField.setVisible(false);
        emailLabel.setVisible(true);
        emailLabel.setText(emailT);

        // Password label and textField
        passwordField.setEditable(false);
        passwordField.setVisible(false);
        passwordLabel.setVisible(true);

        passwordLabel.setText(displayPassword);
        realPass.setLength(0);

        
        // click name
        String name = nameLabel.getText();
        nameLabel.setOnMouseClicked(eName -> {
            long lastClickTime = System.currentTimeMillis();
            if (eName.getClickCount() == 2) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < 500) {
                    clickedName(null, name);
                }
                lastClickTime = clickTime;
            }
        });

        // click on the email
        String email = emailLabel.getText();
        emailLabel.setOnMouseClicked(eEmail -> {
            long lastClickTime = System.currentTimeMillis();
            if (eEmail.getClickCount() == 2) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < 500) {
                    clickedEmail(null, email);
                }
                lastClickTime = clickTime;
            }
            
        });

        // click on the password
        passwordLabel.setOnMouseClicked(ePassword -> {
            long lastClickTime = System.currentTimeMillis();
            if (ePassword.getClickCount() == 2) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < 500) {
                    clickedPassword(null, passwordT);
                }
                lastClickTime = clickTime;
            }
        });



        // This is the hoover for when the mouse is on the round
        Interpolator interpolator = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);
        hoverCircle.setOnMouseEntered(onmouse -> {
            Timeline growDeleteImageX = new Timeline (
                new KeyFrame(Duration.ZERO, new KeyValue(deleteImage.scaleXProperty(), 0)),
                new KeyFrame(Duration.millis(200), new KeyValue(deleteImage.scaleXProperty(), 3, interpolator))
            );
            growDeleteImageX.play();
            Timeline growDeleteImageY = new Timeline (
                new KeyFrame(Duration.ZERO, new KeyValue(deleteImage.scaleYProperty(), 0)),
                new KeyFrame(Duration.millis(200), new KeyValue(deleteImage.scaleYProperty(), 3, interpolator))
            );
            growDeleteImageY.play();
            FadeTransition fadeSmallDot = new FadeTransition(Duration.millis(100), redCircle);
            fadeSmallDot.setFromValue(1);
            fadeSmallDot.setToValue(0);
            fadeSmallDot.play();
        });
        // This is the animation for when the mouse exists the round
        hoverCircle.setOnMouseExited(onmouse -> {
            Timeline shrinkDeleteImageX = new Timeline (
                new KeyFrame(Duration.ZERO, new KeyValue(deleteImage.scaleXProperty(), 3)),
                new KeyFrame(Duration.millis(200), new KeyValue(deleteImage.scaleXProperty(), 0, interpolator))
            );
            shrinkDeleteImageX.play();
            Timeline shrinkDeleteImageY = new Timeline (
                new KeyFrame(Duration.ZERO, new KeyValue(deleteImage.scaleYProperty(), 3)),
                new KeyFrame(Duration.millis(200), new KeyValue(deleteImage.scaleYProperty(), 0, interpolator))
            );
            shrinkDeleteImageY.play();
            FadeTransition fadeSmallDot = new FadeTransition(Duration.millis(100), redCircle);
            fadeSmallDot.setFromValue(0);
            fadeSmallDot.setToValue(1);
            fadeSmallDot.play();
        });

        // Delete User
        hoverCircle.setOnMouseClicked(event -> {
            //Retrieve user from card, delete user
            
            String userName = nameLabel.getText();
            String userEmail = emailLabel.getText();

            String delUserQuery = "DELETE FROM chanuka1.users WHERE name = ? AND email = ?";
            
            try {
                Connection connection = db.getDBConnection();
                PreparedStatement statement = connection.prepareStatement(delUserQuery);
                // check on username and email, in case if same username
                statement.setString(1, userName);  // Set the first parameter (name)
                statement.setString(2, userEmail); // Set the second parameter (email)
                
                int rowDeleted = statement.executeUpdate();
                if (rowDeleted > 0) {
                    System.out.println("Deleted");

                    //Remove user Card displaying
                    Node cardRemove = hoverCircle.getParent();
                    Pane container = (Pane) cardRemove.getParent();
                    container.getChildren().remove(cardRemove);
                } else {
                    System.out.println("Not deleted, mby error");
                }

                statement.close();
                connection.close();
            } catch (SQLException e) {
            e.printStackTrace();
            }

        });
        
    }

    /**
     * Method that handles when clicked on the name field.

     * @param event is the event.
     * @param aVoir is the name already present.
     */
    public void clickedName(ActionEvent event, String aVoir){
        //Kill focus
        emailField.setEditable(false);
        emailField.setVisible(false);
        passwordField.setEditable(false);
        passwordField.setVisible(false);
        // Show Label
        passwordLabel.setVisible(true);
        emailLabel.setVisible(true);

        nameField.setStyle("-fx-text-box-border: None ; -fx-focus-color: None ;");
        
        nameField.setText(nameT);
        nameField.setEditable(true);
        nameField.setVisible(true);
        nameLabel.setVisible(false);
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String enteredName = nameField.getText();
                if (enteredName.isBlank() || checkName(enteredName)) {
                  // set the style of the TextField to make it appear red
                  nameField.setStyle("-fx-text-box-border: None ; -fx-focus-color: red ;");
              } else {
                nameLabel.setText(enteredName);
                nameField.setText(enteredName);
                nameT = enteredName;
                dbU.modifyUser2(nameT, emailT, passwordT, primaryKey);
                initialize();
              }
            }
        });
        // When someone clicks outside, the focus exists
        Parent rootField = nameField.getScene().getRoot();
        rootField.setOnMouseClicked(ev -> {
            nameField.setText(nameT);
            if (ev.getTarget() != nameField && nameField.isFocused() ) {
                String enteredName = nameField.getText();
                paneAdminCard.requestFocus();
                if (enteredName.isEmpty()) {
                  // set the style of the TextField to make it appear red
                  nameField.setStyle("-fx-text-box-border: None ; -fx-focus-color: red ;");
              } else {
                nameLabel.setText(enteredName);
                nameField.setText(enteredName);
                nameT = enteredName;
                initialize();
              }
            }
        });
    }

    /**
     * Method that handles when clicked on the email field.

     * @param event is the event.
     * @param aVoir is the email already present.
     */
    public void clickedEmail(ActionEvent event, String aVoir){
        // Kill focus
        nameField.setEditable(false);
        nameField.setVisible(false);
        passwordField.setEditable(false);
        passwordField.setVisible(false);
        // Show Label
        nameLabel.setVisible(true);
        passwordLabel.setVisible(true);

        emailField.setStyle("-fx-text-box-border: None ; -fx-focus-color: None ;");

        emailField.setText(emailT);
        emailField.setEditable(true);
        emailField.setVisible(true);
        emailLabel.setVisible(false);
        emailField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String enteredEmail = emailField.getText();
                if (!valEmail(enteredEmail)) {
                    // set the style of the TextField to make it appear red
                    emailField.setStyle("-fx-text-box-border: None ; -fx-focus-color: red ;");
                } else if (enteredEmail.isEmpty() || checkEmail(enteredEmail)) {
                    // set the style of the TextField to make it appear red
                    emailField.setStyle("-fx-text-box-border: None ; -fx-focus-color: red ;");   
                } else {
                emailField.setStyle("");
                emailLabel.setText(enteredEmail);
                emailField.setText(enteredEmail);
                emailT = enteredEmail;
                System.out.println(passwordT);
                dbU.modifyUser2(nameT, emailT, passwordT, primaryKey);
                initialize();
                }
            }
        });
        // When someone clicks outside, the focus exists
        Parent rootField = emailField.getScene().getRoot();
        rootField.setOnMouseClicked(ev -> {
            emailField.setText(emailT);
            if (ev.getTarget() != emailField && emailField.isFocused() ) {
                paneAdminCard.requestFocus();
                String enteredEmail = emailField.getText();
                if (!valEmail(enteredEmail)) {
                    // set the style of the TextField to make it appear red
                    emailField.setStyle("-fx-text-box-border: None ; -fx-focus-color: red ;");
                } else if (enteredEmail.isEmpty() || checkEmail(enteredEmail)) {
                    // set the style of the TextField to make it appear red
                    emailField.setStyle("-fx-text-box-border: None ; -fx-focus-color: red ;");                            
                } else {
                
                emailField.setStyle("");
                emailLabel.setText(enteredEmail);
                emailField.setText(enteredEmail);
                emailT = enteredEmail;
                initialize();
                }
            }
        });
    }

    /**
     * Method that handles when clicked on the password field.

     * @param event is the event.
     * @param aVoir is the password already present.
     */
    public void clickedPassword(ActionEvent event, String aVoir){
        //Kill focus
        nameField.setEditable(false);
        nameField.setVisible(false);
        emailField.setEditable(false);
        emailField.setVisible(false);
        // Show Label
        nameLabel.setVisible(true);
        emailLabel.setVisible(true);

        passwordField.setStyle("-fx-text-box-border: None ; -fx-focus-color: None ;");

        realPass.setLength(0);

        

        passwordField.setText(passwordT);
        passwordField.setEditable(true);
        passwordField.setVisible(true);
        passwordLabel.setVisible(false);
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Get the last character in the text field
            String subNewValue = newValue;
            String lastChar = subNewValue.substring(subNewValue.length() - 1);
            // Replace all characters except the last one with stars
            String maskedText = subNewValue.substring(0, subNewValue.length() - 1).replaceAll(".", "*");
            // Set the masked text with the last character appended
            passwordField.setText(maskedText + lastChar);

        });
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode().isDigitKey() || e.getCode().isLetterKey()) {
                String lastChar = e.getText();
                realPass.append(lastChar);
                System.out.println("THE PASSWORD NOW IS " + realPass.toString());
            } else if (e.getCode() == KeyCode.BACK_SPACE && passwordField.getCaretPosition() > 0
            && !Character.isWhitespace(passwordField.getText().charAt(passwordField.getCaretPosition() - 1))) {
                realPass.setLength(0);
                passwordField.setText("");
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
                realPass.deleteCharAt(realPass.length() - 1);
                System.out.println("THE PASSWORD NOW IS " + realPass.toString());
            } else if (e.getCode() == KeyCode.DELETE) {
                realPass.setLength(0);
                passwordField.setText("");
            }
            if (e.getCode() == KeyCode.ENTER) {
                String enteredPassword = realPass.toString();
                if (enteredPassword.length() < 6) {
                    // set the style of the TextField to make it appear red
                    passwordField.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                } else if (enteredPassword.isEmpty()) {
                    // set the style of the TextField to make it appear red
                    passwordField.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");                 
                } else {
                passwordField.setStyle("");
                passwordLabel.setText(enteredPassword);
                passwordField.setText(enteredPassword);
                if (!enteredPassword.equals(passwordT)) {
                  passwordT = dbU.getMd5(enteredPassword);
                } else {
                  passwordT = enteredPassword;
                }
                dbU.modifyUser2(nameT, emailT, passwordT, primaryKey);
                initialize();
                }
            }
        });
        // When someone clicks outside, the focus exists
        Parent rootField = passwordField.getScene().getRoot();
        rootField.setOnMouseClicked(ev -> {
            passwordField.setText(passwordT);
            if (ev.getTarget() != passwordField && passwordField.isFocused() ) {
                System.out.println("Clicked outside clicked clicked outside");
                String enteredPassword = realPass.toString();
                if (enteredPassword.length() < 6) {
                    // set the style of the TextField to make it appear red
                    passwordField.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                } else if (enteredPassword.isEmpty()) {
                    // set the style of the TextField to make it appear red
                    passwordField.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");  
                } else {
                passwordField.setStyle("");
                passwordLabel.setText(displayPassword);
                passwordField.setText(enteredPassword);
                passwordT = realPass.toString();
                initialize();
                }
            }
        });
    }

    /**
     * Sets information in the card.

     * @param name is the name of the user.
     * @param email is the email of the user.
     * @param password is the password of the user.
     * @param pKey is the pKey of the user.
     */
    public void setInformation(String name, String email, String password, String pKey) {
        nameT = name;
        emailT = email;
        passwordT = password;
        primaryKey = pKey;
        nameLabel.setText(nameT);
        emailLabel.setText(emailT);
        passwordLabel.setText(displayPassword);
        nameField.setText(nameT);
        emailField.setText(emailT);
        passwordField.setText(passwordT);
    }

    /**
     * Validate the email field.

     * @param email is the email.
     * @return true or false.
     */
    private Boolean valEmail(String email) {
      String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
      Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
      Matcher matcher = emailPattern.matcher(email);
      return matcher.find();
    }
  
     /** Checks the registration so that it is valid. */
    private boolean checkEmail(String email) {
      try {
        String query = "SELECT users.email FROM chanuka1.users";
        Connection connection = db.getDBConnection();
        Statement statement = connection.createStatement();
        ResultSet emailSet = statement.executeQuery(query);
        while (emailSet.next()) {
          if (emailSet.getString(1).equals(email)) {

            return true;
          }
        }
        return false;
      } catch (SQLException e) {
        return false;
      }
    
    }

    /**
     * Check if the name exists in the database
     * @param name is the name of the user.
     * @return a true or false.
     */
    private boolean checkName(String name) {
      try {

        String query = "SELECT users.name FROM chanuka1.users";
        Connection connection = db.getDBConnection();
        Statement statement = connection.createStatement();
        ResultSet nameSet = statement.executeQuery(query);
        while (nameSet.next()) {
          if (nameSet.getString(1).equals(name)) {
            return true;
          }
        
        }
        return false;
      } catch (SQLException e) {
        return false;
      }
    }
    
}