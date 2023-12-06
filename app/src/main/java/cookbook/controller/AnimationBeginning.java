package cookbook.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;

import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.util.ResourceBundle;

import cookbook.Cookbook;
import cookbook.DBUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.RotateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;



public class AnimationBeginning implements Initializable{

    @FXML
    private Shape circleOne;

    @FXML
    private Shape circleTwo;

    @FXML
    private ImageView pizza;

    @FXML
    private ImageView ther;

    @FXML
    private Button butStartCooking;

    @FXML
    private ImageView recipro;

    @FXML
    private AnchorPane parentContainer;

    @FXML
    private Pane myPane;

    @FXML
    private Rectangle rectangle;

    private DBUtils db = DBUtils.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // This is the transition for the first black round
        ScaleTransition transition_circle_black = new ScaleTransition(Duration.millis(1500), circleOne);
        FadeTransition fade_circle_black = new FadeTransition(Duration.millis(200), circleOne);
        Interpolator interpolator = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);
        transition_circle_black.setFromX(0);
        transition_circle_black.setFromY(0);
        transition_circle_black.setToX(120);
        transition_circle_black.setToY(120);
        transition_circle_black.setInterpolator(Interpolator.SPLINE(0.5, 0.8, 0.25, 1));
        fade_circle_black.setDelay(Duration.millis(1000));
        fade_circle_black.setFromValue(1.0);
        fade_circle_black.setToValue(0.0);

        // This is the transition for the second white round
        ScaleTransition transition_circle_white = new ScaleTransition(Duration.millis(1500), circleTwo);
        FadeTransition fade_circle_white = new FadeTransition(Duration.millis(200), circleOne);
        transition_circle_white.setDelay(Duration.millis(500));
        transition_circle_white.setFromX(0);
        transition_circle_white.setFromY(0);
        transition_circle_white.setToX(120);
        transition_circle_white.setToY(120);
        transition_circle_white.setInterpolator(Interpolator.SPLINE(0.5, 0.8, 0.25, 1));
        fade_circle_white.setDelay(Duration.millis(1200));
        fade_circle_white.setFromValue(1);
        fade_circle_white.setToValue(0);

        // This one is the rotation of the pizza
        RotateTransition rotationPizza = new RotateTransition(Duration.millis(750), pizza);
        FadeTransition fadePizza = new FadeTransition(Duration.millis(10), pizza);
        rotationPizza.setDelay(Duration.millis(1200));
        rotationPizza.setFromAngle(81);
        rotationPizza.setToAngle(-18);
        rotationPizza.setInterpolator(Interpolator.SPLINE(0.5, 0.8, 0.05, 1));
        fadePizza.setDelay(Duration.millis(1200));
        fadePizza.setFromValue(0.0);
        fadePizza.setToValue(1.0);

        // This one is the translation of the R
        TranslateTransition moveTheR = new TranslateTransition(Duration.millis(450), ther);
        FadeTransition fadeR = new FadeTransition(Duration.millis(10), ther);
        moveTheR.setDelay(Duration.millis(1450));
        moveTheR.setFromX(-150);
        moveTheR.setToX(0);
        moveTheR.setInterpolator(Interpolator.SPLINE(0.22, 0.90, 1, 1));
        fadeR.setDelay(Duration.millis(1500));
        fadeR.setFromValue(0.0);
        fadeR.setToValue(1.0);

        // This one is the translation and fade of the Recipro
        TranslateTransition titleMove = new TranslateTransition(Duration.millis(750), recipro);
        FadeTransition fadeTitle = new FadeTransition(Duration.millis(750), recipro);
        titleMove.setDelay(Duration.millis(2000));
        titleMove.setFromY(150);
        titleMove.setToY(0);
        titleMove.setInterpolator(Interpolator.SPLINE(0.22, 0.85, 0.99, 1));
        fadeTitle.setDelay(Duration.millis(2000));
        fadeTitle.setFromValue(0.0);
        fadeTitle.setToValue(1.0);
        fadeTitle.setInterpolator(Interpolator.EASE_IN);

        // This one is the apparition of the button
        FadeTransition button = new FadeTransition(Duration.millis(1000), butStartCooking);
        button.setDelay(Duration.millis(2500));
        button.setFromValue(0.0);
        button.setToValue(1.0);

        //Here are all the transition played
        transition_circle_white.play();
        transition_circle_black.play();
        rotationPizza.play();
        moveTheR.play();
        fade_circle_black.play();
        fade_circle_white.play();
        fadePizza.play();
        fadeR.play();
        button.play();
        titleMove.play();
        fadeTitle.play();
    }

    /**
     * Function that goes to the login page.

     * @param event is the click event.
     * @throws IOException
     */
    @FXML
    private void loadSecond(ActionEvent event) throws IOException {
        // Transition fade rectangle
        FadeTransition rectangleFade = new FadeTransition(Duration.millis(1000), rectangle);
        rectangleFade.setFromValue(0);
        rectangleFade.setToValue(1);
        rectangleFade.play();

        // transition button fade
        FadeTransition buttonFade = new FadeTransition(Duration.millis(1000), butStartCooking);
        buttonFade.setFromValue(1);
        buttonFade.setToValue(0);
        buttonFade.play();

        buttonFade.setOnFinished(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
                Scene scene = butStartCooking.getScene();

                //root.translateYProperty().set(scene.getHeight());
                //root.translateXProperty().set(scene.getWidth() / 4);
                parentContainer.getChildren().add(root);
            } catch (Exception error) {
                error.printStackTrace();
              }

            

        });
    
        

    }

    
}