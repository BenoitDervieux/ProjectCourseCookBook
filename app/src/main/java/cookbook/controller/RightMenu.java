package cookbook.controller;

import cookbook.DBUtils;
import cookbook.RecipeMessage;
import cookbook.controller.MainController;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javafx.animation.FadeTransition;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;


public class RightMenu {

    private Stage stage;
    private Scene scene;

    @FXML
    private Pane PaneBack;

    @FXML
    private Rectangle GreyRect;

    @FXML
    private Rectangle WhiteRect;

    @FXML
    private HBox TheBox, userMessages;

    @FXML
    private Label logo;

    @FXML
    private TextField searchBar;

    @FXML
    private Label nameLabel;

    @FXML 
    private ImageView imageView;

    @FXML 
    private ImageView menuIcon;

    @FXML
    private HBox settingHBox, exitHBox, helpHBox, adminHBox;

    @FXML
    private AnchorPane adminAnchor;

    Image proPic;
    boolean menuClosed = true;
    Double ylayout = 0.0;
    Double xlayout = 0.0;
    DBUtils db = DBUtils.getInstance();

    public void initialize() {

        CornerRadii cornerRadii = new CornerRadii(10);

        setImage(db.getUser().getImage());
        setName(db.getUser().getUsername());
        imageView.setOnMouseClicked(Event -> {
            try {
              FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CreateRecipe.fxml"));
              Parent root = loader.load();
              CreateRecipeController cr = loader.getController();
              FXMLLoader loader2 = new FXMLLoader(getClass().getClassLoader().getResource("TopBar_search.fxml"));
              Parent root2 = loader2.load();
              TopBarControllerSearch tb = loader2.getController();
              Stage stage = (Stage) imageView.getScene().getWindow();
              FileChooser fileChooser = new FileChooser();
              fileChooser.setTitle("Open Resource File");
              fileChooser.getExtensionFilters().addAll(
                      new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                      new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                      new ExtensionFilter("All Files", "*.*"));
              File selectedFile = fileChooser.showOpenDialog(stage);
              if (selectedFile != null) {
                InputStream input = new FileInputStream(selectedFile);
                Image image = new Image(input);
                int height = (int) image.getHeight();
                int width = (int) image.getWidth();
                BufferedImage saveImage = cr.getBufferedImage(selectedFile, height, width);
                String name = "u" + db.getUser().getUserId();
                tb.saveImage(saveImage, name);
                setImage(db.getUser().getImage());
                tb.setImage(db.getUser().getImage());
              }
              
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
        });
        if (!db.getUser().isAdmin()) {
            adminHBox.setVisible(false);
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(menuIcon.rotateProperty(), 0)),
        new KeyFrame(Duration.seconds(0.2), new KeyValue(menuIcon.rotateProperty(), 90)));
        timeline.play();

        Interpolator interpolator = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);

        // Timeline for the large Pane from 0 to 880 (height)
        Timeline paneGetHeight = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(PaneBack.prefHeightProperty(), 0.0)),
            new KeyFrame(Duration.millis(1), new KeyValue(PaneBack.prefHeightProperty(), 880, interpolator))
        );
        paneGetHeight.play();

        // Timeline for the large Pane from 0 to 1200 (width)
        Timeline paneGetWide = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(PaneBack.prefWidthProperty(), 0.0)),
            new KeyFrame(Duration.millis(1), new KeyValue(PaneBack.prefWidthProperty(), 1200, interpolator))
        );
        paneGetWide.play();

        PaneBack.setPrefHeight(880);

        // Timeline for the large grey rectangle from 0 to 880 (height)
        Timeline greyRectGetHigh = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(GreyRect.heightProperty(), 0)),
            new KeyFrame(Duration.millis(1), new KeyValue(GreyRect.heightProperty(), 880, interpolator))
        );
        greyRectGetHigh.play();
        
        // Timeline for the small white rectangle from 0 to 880 (height)
        Timeline whiteRectGetHigh = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(WhiteRect.heightProperty(), 0)),
            new KeyFrame(Duration.millis(1), new KeyValue(WhiteRect.heightProperty(), 880, interpolator))
        );
        whiteRectGetHigh.play();

        // Timeline for the large grey rectangle from 0 to 1200 (width)
        Timeline greyRectGetLarge = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(GreyRect.widthProperty(), 0)),
            new KeyFrame(Duration.millis(500), new KeyValue(GreyRect.widthProperty(), 1200, interpolator))
        );
        greyRectGetLarge.play();

        // Timeline for the small white rectangle from 0 to 300 (width)
        Timeline whiteRectGetLarge = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(WhiteRect.widthProperty(), 0)),
            new KeyFrame(Duration.millis(500), new KeyValue(WhiteRect.widthProperty(), 300, interpolator))
        );
        whiteRectGetLarge.play();

        // Timeline for moving large grey rectangle from 1200 to 0 in X
        Timeline greyRectMoveX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(GreyRect.xProperty(), 1200)),
            new KeyFrame(Duration.millis(500), new KeyValue(GreyRect.xProperty(), 0, interpolator))
        );
        greyRectMoveX.play();

        // Timeline for moving small white rectangle from 1200 to 925 in X
        Timeline whiteRectMoveX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(WhiteRect.xProperty(), 1200)),
            new KeyFrame(Duration.millis(500), new KeyValue(WhiteRect.xProperty(), 925, interpolator))
        );
        whiteRectMoveX.play();

        // Fade of the logo      
        FadeTransition fadeLogo = new FadeTransition(Duration.millis(200), logo);
        fadeLogo.setFromValue(1.0);
        fadeLogo.setToValue(0.0);
        fadeLogo.play();

        //Fade of the search bar
        FadeTransition fadeSearchBar = new FadeTransition(Duration.millis(200), searchBar);
        fadeSearchBar.setFromValue(1.0);
        fadeSearchBar.setToValue(0.0);
        fadeSearchBar.play();

        // Move the "Settings"
        Timeline settingTitleMoveX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(settingHBox.layoutXProperty(), 1040)),
            new KeyFrame(Duration.millis(100), new KeyValue(settingHBox.layoutXProperty(), 1040)),
            new KeyFrame(Duration.millis(600), new KeyValue(settingHBox.layoutXProperty(), 935, interpolator))
        );
        settingTitleMoveX.play();

        // Fix the appearance from outside of the windows, not super effective
        if (settingHBox.getLayoutX() < 1040) {
            settingHBox.setVisible(false);
        } else if (settingHBox.getLayoutX() > 1040) {
            settingHBox.setVisible(true);
        }

        // Fade the settings title
        FadeTransition fadeSettingTitle = new FadeTransition(Duration.millis(500), settingHBox);
        fadeSettingTitle.setFromValue(0.0);
        fadeSettingTitle.setToValue(1.0);
        fadeSettingTitle.play();
        
        //userMessages

        Timeline settingMessagesMoveX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(userMessages.layoutXProperty(), 1040)),
            new KeyFrame(Duration.millis(100), new KeyValue(userMessages.layoutXProperty(), 1040)),
            new KeyFrame(Duration.millis(600), new KeyValue(userMessages.layoutXProperty(), 935, interpolator))
        );
        settingMessagesMoveX.play();

        // Fix the appearance from outside of the windows, not super effective
        if (userMessages.getLayoutX() > 1040) {
            userMessages.setVisible(false);
        } else if (userMessages.getLayoutX() < 1040) {
            userMessages.setVisible(true);
        }

        // Fade the settings title
        FadeTransition fadeMessageTitle = new FadeTransition(Duration.millis(500), userMessages);
        fadeMessageTitle.setFromValue(0.0);
        fadeMessageTitle.setToValue(1.0);
        fadeMessageTitle.play();

        // Move the admin Label
        Timeline settingAdminLabelMoveX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(adminHBox.layoutXProperty(), 1040)),
            new KeyFrame(Duration.millis(100), new KeyValue(adminHBox.layoutXProperty(), 1040)),
            new KeyFrame(Duration.millis(600), new KeyValue(adminHBox.layoutXProperty(), 935, interpolator))
        );
        settingAdminLabelMoveX.play();

        // Fix the appearance from outside of the windows, not super effective
        if (adminHBox.getLayoutX() < 1040) {
            adminHBox.setVisible(false);
        } else if (adminHBox.getLayoutX() > 1040) {
            adminHBox.setVisible(true);
        }

        // Fade the admin label
        FadeTransition fadeAdminLabel = new FadeTransition(Duration.millis(500), adminHBox);
        fadeAdminLabel.setFromValue(0.0);
        fadeAdminLabel.setToValue(1.0);
        fadeAdminLabel.play();
         
        //Hoover on the admin panel
        adminHBox.setOnMouseEntered(event -> {
            adminHBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
            adminHBox.setOnMouseClicked(ev -> {
                changeScene("Admin_Panel_Add.fxml");
            });

        });
        adminHBox.setOnMouseExited(event -> {
            adminHBox.setCursor(Cursor.DEFAULT);
            adminHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            
        });


        // Move the admin Label
        Timeline settingHelpLabelMoveX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(helpHBox.layoutXProperty(), 1040)),
            new KeyFrame(Duration.millis(100), new KeyValue(helpHBox.layoutXProperty(), 1040)),
            new KeyFrame(Duration.millis(600), new KeyValue(helpHBox.layoutXProperty(), 935, interpolator))
        );
        settingHelpLabelMoveX.play();

        // Fix the appearance from outside of the windows, not super effective
        if (helpHBox.getLayoutX() < 1040) {
            helpHBox.setVisible(false);
        } else if (helpHBox.getLayoutX() > 1040) {
            helpHBox.setVisible(true);
        }

        // Fade the help label
        FadeTransition fadeHelpLabel = new FadeTransition(Duration.millis(500), helpHBox);
        fadeHelpLabel.setFromValue(0.0);
        fadeHelpLabel.setToValue(1.0);
        fadeHelpLabel.play();
         
        //Hoover on the help panel
        helpHBox.setOnMouseEntered(event -> {
            helpHBox.setCursor(Cursor.HAND);
            helpHBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, cornerRadii, Insets.EMPTY)));
            helpHBox.setOnMouseClicked(ev -> {
                try {
                    // Load the Home page
                    Parent homeMenu = FXMLLoader.load(getClass().getClassLoader().getResource("Home.fxml"));
                    // Load the admin panel we want to embed in the home page
                    FXMLLoader addWindow = new FXMLLoader(getClass().getClassLoader().getResource("HelpCenter.fxml"));
                    // Load the page as a parent so that we can insert inside the anchor pane from
                    // the home page
                    Parent addWindowNode = addWindow.load();
                    // We can't get the anchor pane directly so we need to get the scroll pane first
                    ScrollPane cardScroll = (ScrollPane) homeMenu.lookup("#cardScroll");
                    // Then we need its content as a parent
                    Parent cardScrollParent = (Parent) cardScroll.getContent();
                    // from the parent, we manage to get the Anchor pane from the home, the one we couldn't get before
                    //AnchorPane realPane = (AnchorPane) cardScrollParent.lookup("#cardAnchorCSS");
                    // We then set the admin add into the anchor pane we managed to get
                    //realPane.getChildren().add(addWindowNode);
                    // then we set the anchor pane into the scroll and we create the new scene
                    cardScroll.setContent(addWindowNode);
                    Scene scene = new Scene(homeMenu);
                    Stage stage = (Stage) PaneBack.getScene().getWindow();
                    stage.setScene(scene);
                } catch (IOException p) {
                    p.getStackTrace();
                }
            });

        });
        helpHBox.setOnMouseExited(event -> {
            helpHBox.setCursor(Cursor.DEFAULT);
            helpHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            
        });
        

        // Move the exit Label
        Timeline settingExitLabelMoveX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(exitHBox.layoutXProperty(), 1040)),
            new KeyFrame(Duration.millis(100), new KeyValue(exitHBox.layoutXProperty(), 1040)),
            new KeyFrame(Duration.millis(600), new KeyValue(exitHBox.layoutXProperty(), 935, interpolator))
        );
        settingExitLabelMoveX.play();

        // Fix the appearance from outside of the windows, not super effective
        if (exitHBox.getLayoutX() < 1040) {
            exitHBox.setVisible(false);
        } else if (exitHBox.getLayoutX() > 1040) {
            exitHBox.setVisible(true);
        }

        // Fade the exit label
        FadeTransition fadeExitLabel = new FadeTransition(Duration.millis(500), exitHBox);
        fadeExitLabel.setFromValue(0.0);
        fadeExitLabel.setToValue(1.0);
        fadeExitLabel.play();
         
        //Hoover on the exit panel
        exitHBox.setOnMouseEntered(event -> {
            exitHBox.setCursor(Cursor.HAND);
            exitHBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, cornerRadii, Insets.EMPTY)));
            exitHBox.setOnMouseClicked(ev -> {
                try {
                    // Load the admin panel we want to embed in the home page
                    FXMLLoader addWindow = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
                    Parent loin = addWindow.load();
                    Scene scene = new Scene(loin);
                    Stage window = (Stage) PaneBack.getScene().getWindow();
                    window.setScene(scene);
                } catch (Exception p) {
                    p.getStackTrace();
                }
            });

        });
        exitHBox.setOnMouseExited(event -> {
            exitHBox.setCursor(Cursor.DEFAULT);
            exitHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            
        });

        userMessages.setOnMouseEntered(event -> {
            userMessages.setCursor(Cursor.HAND);
            userMessages.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, cornerRadii, Insets.EMPTY)));
            userMessages.setOnMouseClicked(ev -> {
                changeScene("userMessages.fxml");
            });

        });

        userMessages.setOnMouseExited(event -> {
            userMessages.setCursor(Cursor.DEFAULT);
            userMessages.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        });

        //nameLabel.setVisible(true);
        FadeTransition fadeNameUser = new FadeTransition(Duration.millis(500), nameLabel);
        fadeNameUser.setFromValue(0.0);
        fadeNameUser.setToValue(1.0);
        fadeNameUser.play();

        //imageView.setVisible(true);
        FadeTransition fadeImageProfile = new FadeTransition(Duration.millis(500), imageView);
        fadeImageProfile.setFromValue(0.0);
        fadeImageProfile.setToValue(1.0);
        fadeImageProfile.play();

        settingHBox.setVisible(true);
        adminHBox.setVisible(true);
        helpHBox.setVisible(true);
        exitHBox.setVisible(true);
        // settingTitle.setVisible(true);
        if (!db.getUser().isAdmin()) {
                adminHBox.setVisible(false);
            } else {
                adminHBox.setVisible(true);
            }

        menuIcon.setOnMouseClicked(evente -> {
            deployAdmin();
        });
    
      }

    public void deployAdmin(){

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(menuIcon.rotateProperty(), 0)),
        new KeyFrame(Duration.seconds(0.2), new KeyValue(menuIcon.rotateProperty(), 90)));
        timeline.play();

        Interpolator interpolator = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);

        Timeline paneShrinkHeight = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(PaneBack.prefHeightProperty(), 880.0)),
            new KeyFrame(Duration.millis(1), new KeyValue(PaneBack.prefHeightProperty(), 80, interpolator))
        );
        paneShrinkHeight.play();

        PaneBack.setPrefHeight(880);

        Timeline greyRectShrinkHeight = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(GreyRect.heightProperty(), 880)),
            new KeyFrame(Duration.millis(1), new KeyValue(GreyRect.heightProperty(), 0, interpolator))
        );
        greyRectShrinkHeight.setDelay(Duration.millis(500));
        greyRectShrinkHeight.play();

        Timeline whiteRectShrinkHeight = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(WhiteRect.heightProperty(), 880)),
            new KeyFrame(Duration.millis(1), new KeyValue(WhiteRect.heightProperty(), 0, interpolator))
        );
        whiteRectShrinkHeight.setDelay(Duration.millis(500));
        whiteRectShrinkHeight.play();

        Timeline greyRectShrinkWidth = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(GreyRect.widthProperty(), 1200)),
            new KeyFrame(Duration.millis(500), new KeyValue(GreyRect.widthProperty(), 0, interpolator))
        );
        greyRectShrinkWidth.play();

        Timeline whiteRectShrinkWidth = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(WhiteRect.widthProperty(), 300)),
            new KeyFrame(Duration.millis(500), new KeyValue(WhiteRect.widthProperty(), 0, interpolator))
        );
        whiteRectShrinkWidth.play();

        Timeline greyRectMoveBackX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(GreyRect.xProperty(), 0)),
            new KeyFrame(Duration.millis(500), new KeyValue(GreyRect.xProperty(), 1200, interpolator))
        );
        greyRectMoveBackX.play();

        Timeline whiteRectMoveBackX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(WhiteRect.xProperty(), 925)),
            new KeyFrame(Duration.millis(500), new KeyValue(WhiteRect.xProperty(), 1200, interpolator))
        );
        whiteRectMoveBackX.play();

        FadeTransition fadeLogo = new FadeTransition(Duration.millis(200), logo);
        fadeLogo.setFromValue(0.0);
        fadeLogo.setToValue(1.0);
        fadeLogo.play();

        FadeTransition fadeSearchBar = new FadeTransition(Duration.millis(400), searchBar);
        fadeSearchBar.setFromValue(0.0);
        fadeSearchBar.setToValue(1.0);
        fadeSearchBar.play();

        Timeline settingTitleMoveBackX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(settingHBox.layoutXProperty(), 935)),
            new KeyFrame(Duration.millis(500), new KeyValue(settingHBox.layoutXProperty(), 1200, interpolator))
        );
        settingTitleMoveBackX.play();
        if (settingHBox.getLayoutX() < 1040) {
            settingHBox.setVisible(true);
        } else if (settingHBox.getLayoutX() > 1040) {
            settingHBox.setVisible(false);
        }

        FadeTransition fadeSettingTitle = new FadeTransition(Duration.millis(200), settingHBox);
        fadeSettingTitle.setFromValue(1.0);
        fadeSettingTitle.setToValue(0.0);
        fadeSettingTitle.play();

        //userMessages

        Timeline settingMessageMoveBackX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(userMessages.layoutXProperty(), 935)),
            new KeyFrame(Duration.millis(500), new KeyValue(userMessages.layoutXProperty(), 1200, interpolator))
        );
        settingMessageMoveBackX.play();

        if (userMessages.getLayoutX() < 1040) {
            userMessages.setVisible(true);
        } else if (userMessages.getLayoutX() > 1040) {
            userMessages.setVisible(false);
        }

        FadeTransition fadeMessageTitle = new FadeTransition(Duration.millis(200), userMessages);
        fadeMessageTitle.setFromValue(1.0);
        fadeMessageTitle.setToValue(0.0);
        fadeMessageTitle.play();


        Timeline adminLabelMoveBackX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(adminHBox.layoutXProperty(), 935)),
            new KeyFrame(Duration.millis(500), new KeyValue(adminHBox.layoutXProperty(), 1200, interpolator))
        );
        adminLabelMoveBackX.play();
        if (adminHBox.getLayoutX() < 1040) {
            adminHBox.setVisible(true);
        } else if (adminHBox.getLayoutX() > 1040) {
            adminHBox.setVisible(false);
        }
        FadeTransition fadeAdminLabel = new FadeTransition(Duration.millis(200), adminHBox);
        fadeAdminLabel.setFromValue(1.0);
        fadeAdminLabel.setToValue(0.0);
        fadeAdminLabel.play();

        Timeline helpLabelMoveBackX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(helpHBox.layoutXProperty(), 935)),
            new KeyFrame(Duration.millis(500), new KeyValue(helpHBox.layoutXProperty(), 1200, interpolator))
        );
        helpLabelMoveBackX.play();
        if (helpHBox.getLayoutX() < 1040) {
            helpHBox.setVisible(true);
        } else if (helpHBox.getLayoutX() > 1040) {
            helpHBox.setVisible(false);
        }
        FadeTransition fadeHelpLabel = new FadeTransition(Duration.millis(200), helpHBox);
        fadeHelpLabel.setFromValue(1.0);
        fadeHelpLabel.setToValue(0.0);
        fadeHelpLabel.play();

        Timeline exitLabelMoveBackX = new Timeline (
            new KeyFrame(Duration.ZERO, new KeyValue(exitHBox.layoutXProperty(), 935)),
            new KeyFrame(Duration.millis(500), new KeyValue(exitHBox.layoutXProperty(), 1200, interpolator))
        );
        exitLabelMoveBackX.play();
        if (exitHBox.getLayoutX() < 1040) {
            exitHBox.setVisible(true);
        } else if (exitHBox.getLayoutX() > 1040) {
            exitHBox.setVisible(false);
        }
        FadeTransition fadeExitLabel = new FadeTransition(Duration.millis(200), exitHBox);
        fadeExitLabel.setFromValue(1.0);
        fadeExitLabel.setToValue(0.0);
        fadeExitLabel.play();

        //nameLabel.setVisible(true);
        FadeTransition fadeNameUser = new FadeTransition(Duration.millis(200), nameLabel);
        fadeNameUser.setFromValue(1.0);
        fadeNameUser.setToValue(0.0);
        fadeNameUser.play();

        //imageView.setVisible(true);
        FadeTransition fadeImageProfile = new FadeTransition(Duration.millis(200), imageView);
        fadeImageProfile.setFromValue(1.0);
        fadeImageProfile.setToValue(0.0);
        fadeImageProfile.play();

        //imageView.setVisible(true);
        FadeTransition fadeMenuIcon = new FadeTransition(Duration.millis(200), menuIcon);
        fadeMenuIcon.setFromValue(1.0);
        fadeMenuIcon.setToValue(0.0);
        fadeImageProfile.play();

        settingHBox.setVisible(false);
        adminHBox.setVisible(false);
        helpHBox.setVisible(false);
        exitHBox.setVisible(false);
        menuIcon.setVisible(false);


        //PauseTransition delay = new PauseTransition(Duration.millis(500));

        greyRectShrinkWidth.setOnFinished(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Home.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) PaneBack.getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException p) {
                p.getStackTrace();
            }
        });

        helpHBox.setOnMouseClicked(e -> {
            changeScene("HelpCenter.fxml");
        });

      }
    public void newScene(MouseEvent ev) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Admin_Panel_Add.fxml"));
        stage = (Stage)((Node)ev.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

        /**
     * This method sets an image to the top-bar dynamically.
     *
     * @param path the path starting from src folder.
     */
    public void setImage(String path) {
        try {
        proPic = new Image(new FileInputStream(path));
        imageView.setImage(proPic);
        } catch (Exception e) {
        //System.out.println("Profile image not set, setting default");
        // this will set a default image if no image is set externally
        proPic = new Image("/images/def.jpg");
        imageView.setImage(proPic);
        }
    }

    public void changeScene(String fxml) {
        try {
            // Load the Home page
            Parent homeMenu = FXMLLoader.load(getClass().getClassLoader().getResource("Home.fxml"));
            // Load the admin panel we want to embed in the home page
            FXMLLoader addWindow = new FXMLLoader(getClass().getClassLoader().getResource(fxml));
            // Load the page as a parent so that we can insert inside the anchor pane from
            // the home page
            Parent addWindowNode = addWindow.load();
            // We can't get the anchor pane directly so we need to get the scroll pane first
            ScrollPane cardScroll = (ScrollPane) homeMenu.lookup("#cardScroll");
            // Then we need its content as a parent
            Parent cardScrollParent = (Parent) cardScroll.getContent();
            // from the parent, we manage to get the Anchor pane from the home, the one we couldn't get before
            //AnchorPane realPane = (AnchorPane) cardScrollParent.lookup("#cardAnchorCSS");
            // We then set the admin add into the anchor pane we managed to get
            //realPane.getChildren().add(addWindowNode);
            // then we set the anchor pane into the scroll and we create the new scene
            VBox box = new VBox();
            box.setId("contentContainer");
            box.getChildren().add(addWindowNode);
            cardScroll.setContent(box);
            if (fxml.equals("userMessages.fxml")) {
                UserMessagesController um = addWindow.getController();
                ArrayList<RecipeMessage> messages = db.getUserMessages(db.getUser().getUserId());
                um.printMessages(messages);
                cardScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
            }
            Scene scene = new Scene(homeMenu);
            Stage stage = (Stage) PaneBack.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException p) {
            p.getStackTrace();
        }
    }

    public void setName(String name) {
        nameLabel.setText(name);
    }
    
}
