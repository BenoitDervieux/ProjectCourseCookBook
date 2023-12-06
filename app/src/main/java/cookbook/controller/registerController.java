package cookbook.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cookbook.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class registerController {
    private DBUtils db = DBUtils.getInstance();
    @FXML
    private Label registerError;
    @FXML
    private TextField registerEmail, registerUsername, registerPassword, repeatPassword, showRepeatPasswordField, showPasswordField;
    @FXML
    private Button createAccountButton, backToLoginButton;
    @FXML
    private ImageView hidePasswordIcon, showPasswordIcon;
    @FXML
    private StackPane showPasswordStack;

    public void initialize() throws Exception {
        registerError.setTextFill(Color.RED);
        // Event for when the user presses the create account button.
        createAccountButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                register();
            }
        });

        repeatPassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                register();
            }
            
        });

        // Event that takes the user back to the login screen.
        backToLoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // Sets the scene back to the login screen.
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
                    Stage window = (Stage)backToLoginButton.getScene().getWindow();
                    window.setScene(new Scene(root));
                } catch (Exception e) {
                    System.out.println("An error has occured: " + e.getMessage());
                }
            }
            
        });

        showPasswordIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String text = registerPassword.getText();
                showPasswordField.setText(text);
                showPasswordField.setVisible(true);
                showPasswordField.positionCaret(text.length());
                String text2 = repeatPassword.getText();
                showRepeatPasswordField.setText(text2);
                showPasswordStack.setVisible(true);
                showRepeatPasswordField.positionCaret(text2.length());
            }
            
        });

        hidePasswordIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String text = showPasswordField.getText();
                registerPassword.setText(text);
                showPasswordField.setVisible(false);
                registerPassword.positionCaret(text.length());
                String text2 = showRepeatPasswordField.getText();
                repeatPassword.setText(text2);
                showPasswordStack.setVisible(false);
                repeatPassword.positionCaret(text2.length());
            }
            
        });
    }
    
    // Validates the email entered by the user.
    private Boolean valEmail(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    private void register() {
        TextField[] details = new TextField[] {registerEmail, registerUsername, registerPassword, repeatPassword};
        // Checks if the fields are empty
        // Sets border to red if empty else set border to empty
        for (TextField tf : details) {
            if (tf.getText().isBlank()) {
                tf.setBorder(new Border(
                    new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                registerError.setText("Empty fields!");
            } else {
                tf.setBorder(Border.EMPTY);
            }
        }
        // Checks if fields are empty after for loop
        for (TextField tf : details) {
            if (tf.getText().isBlank()) {
                return;
            }
        }
        // Checks if the email is valid.
        if (!valEmail(registerEmail.getText())) {
            registerEmail.setBorder(new Border(
                new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            registerError.setText("Please enter a valid email!");
            return;
        }

        // Checks if the passwords match.
        if (!registerPassword.getText().equals(repeatPassword.getText())) {
            registerPassword.setBorder(new Border(
                new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            repeatPassword.setBorder(new Border(
                new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                registerError.setText("Passwords do not match!");
            return;
        }
        try {
            // Sets the scene back to the login scene, after user creates account
            // Checks registration with database
            db.checkRegistration(registerEmail, registerUsername, registerPassword);
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            Stage window = (Stage)createAccountButton.getScene().getWindow();
            window.setScene(new Scene(root));
        } catch (IllegalArgumentException e) {
            registerError.setText(e.getMessage());
        } catch (Exception e) {
            System.out.println("An error has occured: " + e.getMessage());
        }
    }

}
