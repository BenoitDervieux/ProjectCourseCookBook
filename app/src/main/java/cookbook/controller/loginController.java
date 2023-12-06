package cookbook.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import cookbook.DBUtils;
import cookbook.Recipe;
import cookbook.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class loginController {
    private DBUtils db = DBUtils.getInstance();
    @FXML
    private Label loginError;
    @FXML
    private TextField loginUsername;
    @FXML
    private TextField loginPassword;
    @FXML
    private Hyperlink forgotPassword;
    @FXML
    private Hyperlink createAccount;
    @FXML
    private Button loginButton;
    @FXML
    private ImageView showPasswordIcon;
    @FXML
    private StackPane passwordStack;
    @FXML
    private ImageView hidePasswordIcon;
    @FXML
    private TextField showPasswordField;


    public void initialize() throws Exception {

        // Eventhandler for the login button
        // Is activated when user presses login
        loginButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                login();
            }
            
        });

        loginPassword.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                login();
            }
            
        });

        // Makes the password visible for the user when pressed.
        showPasswordIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String text = loginPassword.getText();
                showPasswordField.setText(text);
                passwordStack.setVisible(true);
                showPasswordField.positionCaret(text.length());
            }
            
        });

        // Makes the password invisible for the user when pressed.
        hidePasswordIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String text = showPasswordField.getText();
                loginPassword.setText(text);
                passwordStack.setVisible(false);
                loginPassword.positionCaret(text.length());
            }
            
        });

        // If the user wants to create an account,
        // this will take the user to the registration.
        createAccount.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    // Sets the registration scene to the stage.
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
                    Scene scene = new Scene(root);
                    Stage window = (Stage)createAccount.getScene().getWindow();
                    window.setScene(scene);
                } catch (Exception e) {
                    System.out.println("An error has occured: " + e.getMessage());
                }
            }
            
        });
    }

    private void login() {
        // Sets error text to red.
        loginError.setTextFill(Color.RED);

        // Checks if username is empty
        if (loginUsername.getText().isBlank()) {
            loginUsername.setBorder(new Border(
            new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            loginError.setText("Please enter a username.");
            return;
        } else {
            loginUsername.setBorder(Border.EMPTY);
        }

        // Checks if password is empty
        if (loginPassword.getText().isBlank()) {
            loginPassword.setBorder(new Border(
            new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            loginError.setText("Please enter a password.");
            return;
        } else {
            loginPassword.setBorder(Border.EMPTY);
        }
        try {
            // Sets new scene to the stage if password matches with the username.
            db.checkLogin(loginUsername.getText(), loginPassword.getText());
            User loggedInUser = db.getUser(loginUsername.getText());
            db.setUser(loggedInUser);
            /*ArrayList<Recipe> theRecipes = db.getAllRecipes(loggedInUser.getUserId());
            db.setAllRecipes(theRecipes);*/
            Stage stage = (Stage) loginUsername.getScene().getWindow();
            stage.setUserData(loggedInUser);
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Home.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage)loginButton.getScene().getWindow();
            window.setScene(scene);
        } catch (IllegalArgumentException e) {
            loginError.setText(e.getMessage());
        } catch (Exception e) {
            System.out.println("An error has occured: " + e.getMessage());
        }
    }
}
