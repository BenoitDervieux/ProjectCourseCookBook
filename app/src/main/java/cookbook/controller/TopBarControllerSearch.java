package cookbook.controller;

import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Statement;

import cookbook.model.RecipeSearchModel;
import cookbook.DBUtils;

import cookbook.DatabaseConnection;
import cookbook.Message;
import cookbook.RecipeMessage;
import cookbook.Tag;
import cookbook.User;
import cookbook.Recipe;
import cookbook.RecipeBox;
import cookbook.User;
import cookbook.Recipe;
import cookbook.RecipeBox;
import cookbook.User;
import javafx.beans.value.ChangeListener;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

/**
 * This is the top-bar of the application.
 */
public class TopBarControllerSearch {

  @FXML
  AnchorPane cardAnchor, homePane;

  @FXML
  ScrollPane cardScroll;

  @FXML
  ImageView imageView;

  @FXML
  Label nameLabel;

  @FXML
  ImageView menuIcon, logo;

  @FXML
  TextField searchBar;

  @FXML
  Rectangle rect;

  @FXML
  VBox suggestionBox;

  @FXML
  AnchorPane encorePane;

  @FXML
  private HBox browseButton, weekSelection, homeButton, newRecipe;

  @FXML
  private HBox byTagButton;

  Image proPic;
  boolean menuClosed = true;

  boolean rectangleClosed = true;

  private ObservableList<String> items = FXCollections.observableArrayList();
  private ListView<String> suggestionListView = new ListView<>();
  private ListView<String> searchResultListView = new ListView<>();
  private double opacitySuggestion = 0;
  private boolean menuFade = false;
  private String recipeQuery = "";
  private String queryName = "";
  private DBUtils dbu = DBUtils.getInstance();
  private User user = dbu.getUser();
  private MainController mainController;
  DatabaseConnection connectNow;
  Connection connectDB;

  /**
   * Initializer method for the image-view.
   */
  public void initialize() {
    
    // Returns to home when clicked on the logo
    logo.setOnMouseClicked(event -> {
      try {
        FXMLLoader rightMenu = new FXMLLoader(getClass().getClassLoader().getResource("Home.fxml"));
        Parent root = rightMenu.load();

        Parent homeRoot = encorePane.getParent();
        Scene homeScene = homeRoot.getScene();
        Scene scene = new Scene(root);
        Stage stage = (Stage) homeScene.getWindow();
        stage.setScene(scene);
        stage.show();

      } catch (IOException p) {
        p.setStackTrace(null);
      }

    });
    // this will set a default image if no image is set externally
    proPic = new Image("/images/def.jpg");
    imageView.setImage(proPic);
    // size of the image-view
    imageView.setFitWidth(50);
    imageView.setFitHeight(50);
    // used to make the image into a circle
    Circle clip = new Circle(imageView.getFitWidth() / 2, imageView.getFitHeight() / 2, imageView.getFitWidth() / 2);
    // set the clip of the ImageView
    imageView.setClip(clip);

    // Allow the user to choose a profile picture
    imageView.setOnMouseClicked(Event -> {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CreateRecipe.fxml"));
        Parent root = loader.load();
        CreateRecipeController cr = loader.getController();
        FXMLLoader loader2 = new FXMLLoader(getClass().getClassLoader().getResource("RightMenu.fxml"));
        Parent root2 = loader2.load();
        RightMenu rm = loader2.getController();
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
          String name = "u" + user.getUserId();
          saveImage(saveImage, name);
          setImage(dbu.getUser().getImage());
          rm.setImage(dbu.getUser().getImage());
        }

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    searchBar.setFocusTraversable(false);

    connectNow = new DatabaseConnection();
    connectDB = connectNow.getDBConnection();

    searchResultListView.setCellFactory(param -> new ListCell<String>() {

      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          setText(null);
        } else {
          setText(item);
        }
      }
    }

    );

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
      // this will set a default image if no image is set externally
      proPic = new Image("/images/def.jpg");
      imageView.setImage(proPic);

    }
  }

  /**
   * set the user name dynamically.
   * 
   * @param name name to be set.
   */
  public void setName(String name) {
    nameLabel.setText(name);
  }

  /**
   * This function trigger the burger menu and deploy the right menu
   */
  public void openMenu() {

    // create a timeline animation to rotate the menuIcon
    Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(menuIcon.rotateProperty(), 0)),
        new KeyFrame(Duration.seconds(0.2), new KeyValue(menuIcon.rotateProperty(), 90)));
    timeline.play();

    // load the right menu
    try {
      FXMLLoader rightMenu = new FXMLLoader(getClass().getClassLoader().getResource("RightMenu.fxml"));
      Parent root = rightMenu.load();

      Parent homeRoot = encorePane.getParent();
      Scene homeScene = homeRoot.getScene();
      Scene scene = new Scene(root);
      Stage stage = (Stage) homeScene.getWindow();
      stage.setScene(scene);
      stage.show();

    } catch (IOException p) {
      p.setStackTrace(null);
    }

  }

  /**
   * Handles the search bar function.

   * @param event is when the user clicks on the search bar.
   */
  @FXML
  public void opac(MouseEvent event) {

    // Condition for when the search bar is clicked and the background has opacity
    if (rectangleClosed) {

      // Chanuka's method to activate the opacity of the background behind
      double rectangleFrom = rectangleClosed ? 0 : 0.33;
      double rectangleTo = rectangleClosed ? 0.33 : 0;

      // Size of the suggestion box when no query
      suggestionBox.setPrefHeight(0);
      Interpolator interpolator = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);
      Timeline timeline = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(rect.heightProperty(), 0)),
          new KeyFrame(Duration.millis(500), new KeyValue(rect.heightProperty(), 800, interpolator)));

      timeline.play();

      // Fade of the background
      FadeTransition fadeRectangle = new FadeTransition(Duration.millis(500), rect);
      fadeRectangle.setFromValue(rectangleFrom);
      fadeRectangle.setToValue(rectangleTo);
      fadeRectangle.play();

      rect.setOpacity(0.30);
      // Change the state of the rectangle after
      fadeRectangle.setOnFinished(e -> rectangleClosed = !rectangleClosed);

      if (suggestionBox.getChildren().isEmpty()) {
        suggestionBox.getChildren().addAll(suggestionListView);
      }

      suggestionBox.setVisible(false);
      // suggestionBox.setSpacing(2);
      suggestionBox.setPadding(new Insets(0));
      suggestionBox.setStyle("-fx-background-color: lightgrey;");
      suggestionBox.setMaxHeight(600);

      // This is the function that will handles the sql queries and the way we will
      // display things
      // We look for every change in our search bar using an observable method
      searchBar.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
          // Here we look if it starts with a "#" si that we know it's a tag or not
          if (newValue.startsWith("#")) {
            // This method is supposed to copy the entered string by subtracting the #
            // Example #salad become salad
            String minusHashtag = newValue.substring(1);
            if (minusHashtag.length() != 0) {
              // We send this query to get the tag results
              recipeQuery = ("SELECT DISTINCT t.name AS rname FROM chanuka1.recipes AS r JOIN chanuka1.recipe_tags AS rt ON r.recipe_id = rt.recipe_id JOIN chanuka1.tags AS t ON rt.tag_id = t.tag_id WHERE t.name LIKE '%"
                  + minusHashtag.toLowerCase() + "%';");
            }
          } else {
            // We send this query to get the name result
            recipeQuery = ("SELECT DISTINCT r.name AS rname FROM chanuka1.recipes AS r JOIN chanuka1.recipe_ingredients AS ri ON r.recipe_id = ri.recipe_id JOIN chanuka1.ingredients AS ing ON ri.ingredient_id = ing.ingredient_id WHERE r.name LIKE '%"
                + newValue + "%' OR ing.name LIKE '%" + newValue + "%';");
          }
          items.clear();
          try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(recipeQuery);
            while (queryOutput.next()) {
              if (newValue.startsWith("#")) {
                queryName = queryOutput.getString("rname").toLowerCase();
              } else {
                queryName = queryOutput.getString("rname").toLowerCase();
                // Populate the Observable List

              }
              items.add(queryName.toLowerCase());
            }
          } catch (SQLException e) {
            Logger.getLogger(RecipeSearchController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
          }
          FilteredList<String> filteredList = new FilteredList<>(items, item -> {
            if (newValue.startsWith("#")) {
              return item.toLowerCase().contains(newValue.substring(1).toLowerCase());
            } else {
              return true;
            }
          });
          String noMatch = "No dish or ingredient with this name";
          List<String> displayList = new ArrayList<>(filteredList);
          // This is the filtered list that investigate if we have the
          if (newValue.isEmpty() || newValue.equals("#")) {
            suggestionBox.setVisible(false);
            suggestionListView.setItems(null);
            opacitySuggestion = 0;
            menuFade = false;
          } else if (displayList.isEmpty()) {
            suggestionBox.setVisible(true);
            suggestionBox.setPrefHeight(25);
            displayList.add(noMatch);
            // replacing this with new list
            searchResultListView.setItems(FXCollections.observableArrayList(displayList));
          } else {
            suggestionBox.setVisible(true);
            suggestionListView.setItems(FXCollections.observableArrayList(displayList));
            int size = displayList.size() * 25;
            FadeTransition fadeSuggestion = new FadeTransition(Duration.millis(150), suggestionBox);
            if (menuFade == false) {
              opacitySuggestion = 0;
            } else if (menuFade == true) {
              opacitySuggestion = 1;
            }
            fadeSuggestion.setFromValue(opacitySuggestion);
            fadeSuggestion.setToValue(1);
            fadeSuggestion.play();
            menuFade = true;
            suggestionBox.setPrefHeight(size);
          }

          searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && rectangleClosed == false && !newValue.isEmpty()) {
              // Perform desired action
              if (newValue.startsWith("#")) {
                // prints the hashtag recipes if starts by an #
                StringBuilder toSend = new StringBuilder(newValue);
                String stringToSend = toSend.deleteCharAt(0).toString();
                getRecipeByTag(stringToSend);
                unfocus(event);
              } else {
                // prints the ingredient recipe if press enter
                getRecipeByIngredient(newValue);
                unfocus(event);
              }
            }
          });

          // handles when someone clicks on a recipe
          suggestionListView.setOnMouseClicked(e -> {
            String name = suggestionListView.getSelectionModel().getSelectedItem();

            try {
              if (newValue.startsWith("#")) {
                // get a special tag if starts by #
                getRecipeByTag(name);
                unfocus(e);
              } else {
                // get a special recipe if doesn't start with #
                PreparedStatement getByName = connectDB
                    .prepareStatement("select * from recipes where name = ? limit 1");
                getByName.setString(1, name);
                ResultSet rs = getByName.executeQuery();

                while (rs.next()) {
                  // gets the tags and messages for the recipes.
                  ArrayList<Message> messageList = dbu.getRecipeMessages(rs.getInt(1));
                  ArrayList<Tag> tagList = dbu.getAllTagsByID(rs.getInt(1), user.getUserId());
                  Recipe recipe = new Recipe(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                      rs.getInt(5), rs.getFloat(6));
                  recipe.setUserId(rs.getInt(7));
                  recipe.setFavourite(dbu.checkIsFavorite(user.getUserId(), recipe.getRecipeId()));
                  recipe.setMessages(messageList);
                  recipe.setTags(tagList);
                  recipe.setImage("src/main/resources/images/Pot au feu.jpg");
                  setSearchedRecipe(recipe);
                }

                unfocus(e);
              }
            } catch (Exception ex) {
              System.out.println("Error in getting recipe by ingredient from search bar: " + ex.getMessage());
            }

          });
        }
      });
    }
  }

  /**
   * Get recipes according to a tag name in the search bar.

   * @param tag is the name of the tag.
   */
  public void getRecipeByTag(String tag) {

    ArrayList<Recipe> recipes = dbu.getAllTaggedRecipes(tag);

    try {
      Parent laHbox = encorePane.getParent();
      mainController.setBooleans();
      AnchorPane lAnchor = (AnchorPane) laHbox.getParent();
      ScrollPane mainView = (ScrollPane) lAnchor.lookup("#cardScroll");
      VBox container = (VBox) mainView.getContent();
      container.getChildren().clear();
      String nameOfLabel = "#" + tag;
      Label label = new Label(nameOfLabel);

      label.setStyle(
          "-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");

      int gPaneColumn = 0;
      int gPanerow = 0;
      int gPaneGeneral = 0;

      GridPane theGrid = new GridPane();
      theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
      container.getChildren().addAll(label, theGrid);
      container.setStyle("-fx-background-color: white;");
      // cardScroll.setContent(container);
      // cardScroll.setStyle("-fx-background-color: white;");
      for (Recipe e : recipes) {
        gPaneGeneral++;
        try {
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
          Parent card = fxmlLoader.load();
          RecipeBoxController recipeToAdd = fxmlLoader.getController();
          recipeToAdd.setRecipe(e);
          theGrid.add(card, gPaneColumn, gPanerow, 1, 1);
          gPaneColumn++;
          if (gPaneColumn % 4 == 0 && gPaneGeneral != 0) {
            gPaneColumn = gPaneColumn % 4;
            gPanerow++;
          }
        } catch (IOException p) {
          p.printStackTrace();
        }

      }
      theGrid.setHgap(10);
      theGrid.setVgap(10);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Get recipes according to an ingredient name in the search bar.

   * @param ingredient is the ingredient.
   */
  public void getRecipeByIngredient(String ingredient) {

    ArrayList<Recipe> recipes = dbu.getRecipeByIngredient(ingredient);

    try {
      Parent laHbox = encorePane.getParent();
      mainController.setBooleans();
      AnchorPane lAnchor = (AnchorPane) laHbox.getParent();
      ScrollPane mainView = (ScrollPane) lAnchor.lookup("#cardScroll");
      VBox container = (VBox) mainView.getContent();
      container.getChildren().clear();
      String nameOfLabel = "Ingredient: " + ingredient;
      Label label = new Label(nameOfLabel);
      label.setStyle(
          "-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");

      int gPaneColumn = 0;
      int gPanerow = 0;
      int gPaneGeneral = 0;

      GridPane theGrid = new GridPane();
      theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
      container.getChildren().addAll(label, theGrid);
      container.setStyle("-fx-background-color: white;");
      for (Recipe e : recipes) {
        gPaneGeneral++;
        try {
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
          Parent card = fxmlLoader.load();
          RecipeBoxController recipeToAdd = fxmlLoader.getController();
          recipeToAdd.setRecipe(e);
          theGrid.add(card, gPaneColumn, gPanerow, 1, 1);
          gPaneColumn++;
          if (gPaneColumn % 4 == 0 && gPaneGeneral != 0) {
            gPaneColumn = gPaneColumn % 4;
            gPanerow++;
          }
        } catch (IOException p) {
          p.printStackTrace();
        }

      }
      theGrid.setHgap(10);
      theGrid.setVgap(10);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  /**
   * This function takes the grey rectangle back to where it was before.

   * @param event is the event that triggers the rectangle to undeploy.
   */
  @FXML
  public void unfocus(Event event) {

    if (rectangleClosed == false) {
      rect.requestFocus();
      double rectangleFrom = 0.33;
      double rectangleTo = 0;
      suggestionBox.setPrefHeight(0);
      suggestionBox.setVisible(false);
      // create a timeline animation to rotate the menuIcon
      FadeTransition fadeRectangle = new FadeTransition(Duration.millis(500), rect);
      fadeRectangle.setFromValue(rectangleFrom);
      fadeRectangle.setToValue(rectangleTo);
      fadeRectangle.play();
      rectangleClosed = true;
      Interpolator interpolator = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);
      Timeline timeline = new Timeline(
          new KeyFrame(Duration.ZERO, new KeyValue(rect.heightProperty(), 800)),
          new KeyFrame(Duration.millis(500), new KeyValue(rect.heightProperty(), 0, interpolator)));
      timeline.play();
    }

  }

  /**
   * Returns the text in the Searchbar.
   *
   * @return search text
   */
  public String getSearchText() {
    return searchBar.getText();
  }

  /*
   * RecipeBoxController rb = new RecipeBoxController();
   * 
   * public void printTaggedRecipes(String tag) {
   * ArrayList<Recipe> recipes = dbu.getAllTaggedRecipes(tag);
   * FXMLLoader loader = new
   * FXMLLoader(getClass().getClassLoader().getResource("Home.fxml"));
   * try {
   * Parent root = loader.load();
   * MainController taggedRecipes = loader.getController();
   * taggedRecipes.printAll(recipes);
   * } catch (Exception e) {
   * e.printStackTrace();
   * }
   * 
   * }
   * }
   */
  public void saveImage(BufferedImage bImage, String name) {
    try {
      ImageIO.write(bImage, "jpg",
          new File("src/main/resources/user_images/" + name + ".jpg")); // change 'png' to the
      dbu.getUser().setImage("src/main/resources/user_images/" + name + ".jpg");
    } catch (IOException e) {
      System.out.println("profile image not set");
    }
  }

  /**
   * Set the main controller.

   * @param mainController is the main controller.
  */
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  /**
   * Set the searched recipe.

   * @param recipe is the recipe.
   */
  public void setSearchedRecipe(Recipe recipe) {
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ViewRecipe.fxml"));
    try {
      Parent root = loader.load();
      ViewRecipeController controller = loader.getController();
      // set user before initializing to avoid favorite issues
      controller.setUser(this.user);
      controller.setTags(recipe.getTags());
      controller.initializeWithRecipe(recipe);

      Parent laHbox = encorePane.getParent();
      mainController.setBooleans();
      AnchorPane lAnchor = (AnchorPane) laHbox.getParent();
      ScrollPane mainView = (ScrollPane) lAnchor.lookup("#cardScroll");
      VBox container = (VBox) mainView.getContent();
      container.getChildren().clear();
      container.getChildren().add(root);
    } catch (Exception error) {
      error.printStackTrace();
    }

  }
}
