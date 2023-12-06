package cookbook.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.units.qual.A;

import cookbook.CreateRecipe;
import cookbook.DBUtils;
import cookbook.DatabaseConnection;
import cookbook.Recipe;
import cookbook.RecipeBox;
import cookbook.Tag;
import cookbook.User;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * This is the top-bar of the application.
 */
public class MainController {

  @FXML
  HBox topBarContainer;

  @FXML
  ImageView imageView;

  @FXML
  Label errorLabel, browsingLabel, browseLabel;

  @FXML
  ImageView menuIcon;

  @FXML
  TextField searchBar;
  @FXML
  private AnchorPane cardAnchor, homePane;
  @FXML
  private ScrollPane cardScroll;
  @FXML
  private Pane cardPane;
  @FXML
  private HBox browseButton, weekSelection, homeButton, newRecipe, byTagButton, shoppingList, byIngredientButton;
  @FXML
  private BorderPane createRecipeBorderPane;
  private CreateRecipe createRecipe;

  @FXML
  private StackPane recipeStack;


  boolean bBrowse = false;
  boolean bIngredient = false;
  boolean bTag = false;
  int totalBrowse = 0;
  int totalIngredient = 0;
  int totalTag = 0;

  Image proPic;
  boolean menuClosed = true;

  double xlayout = 0.0;
  double ylayout = 0.0;
  DBUtils db = DBUtils.getInstance();

  int ingredientTrace = 2;
  int ingredientTrack = 0;

  private final DatabaseConnection dbc;
  private DBUtils dbu = DBUtils.getInstance();
  ArrayList<IngredientManager> poolOfRecipes;
  ArrayList<IngredientManager> bufferRecipes;


  public MainController() {
    this.dbc = new DatabaseConnection(); 
  }

  /**
   * Initializer method for the image-view.
   */
  public void initialize() {
    //Set up the scroll pane for the home
    cardScroll.setContent(new Group());
    // Set up the vbox of the scroll pann
    VBox container = new VBox();
    container.setId("contentContainer");
    cardScroll.setContent(container);
    cardScroll.setStyle("-fx-background-color: white;");

    // Select the home tab
    disableLabels(homeButton);

    //Get the favorite recipes
    ArrayList<Recipe> favouriteRecipes = db.getFavouriteRecipes(db.getUser().getUserId());

    // Fill the home scroll pane if the user has favorite recipes
    if (!favouriteRecipes.isEmpty()) {
      // Create a label for it
      Label label = new Label("Your favourite recipes");
      label.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
      int gPaneColumn = 0;
      int gPanerow = 0;
      int gPaneGeneral = 0;
      GridPane theGrid = new GridPane();
      theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
      container.getChildren().addAll(label, theGrid);
      container.setStyle("-fx-background-color: white;");
      // Fills everythign in a Grid
      for (Recipe z : favouriteRecipes) {
        gPaneGeneral++;
        try {
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
          Parent card = fxmlLoader.load();
          RecipeBoxController recipeToAdd = fxmlLoader.getController();
          recipeToAdd.setMainController(this);
          recipeToAdd.setRecipe(z);
          theGrid.add(card, gPaneColumn, gPanerow, 1, 1);
          gPaneColumn++;
      if (gPaneColumn % 4 == 0 && gPaneGeneral != 0) {
          gPaneColumn = gPaneColumn % 4;
          gPanerow++;
      }
      } catch (IOException p) {
          p.printStackTrace();
      }
      theGrid.setHgap(10);
      theGrid.setVgap(10);
        }
      }
    
    // initialized two array lists for the infinite scroll
    poolOfRecipes = new ArrayList<>();
    bufferRecipes = new ArrayList<>();
    
    // get the number of ingredients and hashtags
    Connection connection = null;
    Statement statement = null;
    ResultSet queryOutput = null;
    String queryT = "select count(distinct tags.name) as count from chanuka1.tags;";
    String queryIngr = "select count(distinct ingredients.name) as count from chanuka1.ingredients;";
    
        try {
            connection = dbc.getDBConnection();
            statement = connection.createStatement();
        
            // Execute first query
            queryOutput = statement.executeQuery(queryT);
            if (queryOutput.next()) {
                String resultTag = queryOutput.getString("count");
                totalTag = Integer.parseInt(resultTag);
            }
        
            // Execute second query
            queryOutput = statement.executeQuery(queryIngr);
            if (queryOutput.next()) {
                String resultIngr = queryOutput.getString("count");
                totalIngredient = Integer.parseInt(resultIngr);
            }
        } catch (SQLException e) {
            // Handle any SQL errors
            e.printStackTrace();
        } finally {
            // Close the resources
            if (queryOutput != null) {
                try {
                    queryOutput.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        ingredientTrace = 3;
        ingredientTrack = 0;

    // Load the top bar to asociate it in the home menu
    try {
      // Gets the user object from the stage.
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/TopBar_search.fxml"));
      Parent newScene = loader.load();
      TopBarControllerSearch tb = loader.getController();
      tb.setMainController(this);
      FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/RightMenu.fxml"));
      Parent root = loader2.load();
      RightMenu rm = loader2.getController();
      tb.setName(db.getUser().getUsername());
      tb.setImage(db.getUser().getImage());
      rm.setImage(db.getUser().getImage());
      rm.setName(db.getUser().getUsername());
      topBarContainer.getChildren().add(newScene);

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    // Comes back to home if clicked on it
    homeButton.setOnMouseClicked(event -> {
      initialize();
      disableLabels(homeButton);
    });

    // Fetch all the recipes if clicked on browse button
    browseButton.setOnMouseClicked(e -> {
      // booleans method to trigger or not the infinite scroll
      bBrowse = true;
      bIngredient = false;
      bTag = false;
      cardScroll.setContent(container);

      try {
        container.getChildren().clear();
        // Get all the recipes
        ArrayList<Recipe> recipes = dbu.getAllRecipes(db.getUser().getUserId());
        // create a new vbox to have the label first and then the grid
        //create the label
        Label label = new Label("Browse Recipes");
        // style the label using css
        label.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
        // here we gonna count the column a bit handmade so that we have only 4 cards
        int gPaneColumn = 0;
        int gPanerow = 0;
        int gPaneGeneral = 0;
        GridPane theGrid = new GridPane();
        theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
        container.getChildren().addAll(label, theGrid);
        container.setStyle("-fx-background-color: white;");
        // Print all the recipe in a grid
        for (Recipe r : recipes) {
          gPaneGeneral++;
          try {
            // Loading of the recipe box
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
            Parent card = fxmlLoader.load();
            RecipeBoxController recipeToAdd = fxmlLoader.getController();
            recipeToAdd.setMainController(this);
            recipeToAdd.setRecipe(r);
            theGrid.add(card, gPaneColumn, gPanerow, 1, 1);
            gPaneColumn++;
        if (gPaneColumn % 4 == 0 && gPaneGeneral != 0) {
            gPaneColumn = gPaneColumn % 4;
            gPanerow++;
        }
        } catch (IOException p) {
            p.printStackTrace();
        }
        theGrid.setHgap(10);
        theGrid.setVgap(10);

        }
        
      } catch (Exception p) {
        p.printStackTrace();
      }

      disableLabels(browseButton);

    });

    // Get the first 3 tag and their recipes when clicked on tag
    // print those recipes in a grid
    byTagButton.setOnMouseClicked(e -> {
      cardScroll.setContent(container);
      container.getChildren().clear();
      cardScroll.setVvalue(0);
      bBrowse = false;
      bIngredient = false;
      bTag = true;
      ArrayList<IngredientManager> dearRecipes = new ArrayList<>();
      ingredientTrack = 0;
      dearRecipes = getRecipesByTagNumber(ingredientTrack);
      if (!dearRecipes.isEmpty()) {
        String firstref = dearRecipes.get(0).getName();
        Label label = new Label("#" + firstref);
        label.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
        int gPaneColumn = 0;
        int gPanerow = 0;
        int gPaneGeneral = 0;
        GridPane theGrid = new GridPane();
        theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
        container.getChildren().addAll(label, theGrid);
        container.setStyle("-fx-background-color: white;");
        for (IngredientManager z : dearRecipes) {
          if (!firstref.equals(z.getName())) {
            firstref = z.getName();
            Label label2 = new Label("#" + firstref);
            label2.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
            theGrid = new GridPane();
            theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
            gPaneColumn = 0;
            gPanerow = 0;
            gPaneGeneral = 0;
            container.getChildren().addAll(label2, theGrid);
            theGrid.setHgap(10);
            theGrid.setVgap(10);
          }
          gPaneGeneral++;
          try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
            Parent card = fxmlLoader.load();
            RecipeBoxController recipeToAdd = fxmlLoader.getController();
            recipeToAdd.setMainController(this);
            recipeToAdd.setRecipe(z.getRecipe());
            theGrid.add(card, gPaneColumn, gPanerow, 1, 1);
            gPaneColumn++;
        if (gPaneColumn % 4 == 0 && gPaneGeneral != 0) {
            gPaneColumn = gPaneColumn % 4;
            gPanerow++;
        }
        } catch (IOException p) {
            p.printStackTrace();
        }
        theGrid.setHgap(10);
        theGrid.setVgap(10);
          }
        }
        disableLabels(byTagButton);
    });

    // Get the first 3 ingredients and their recipes when clicked on tag
    // print those recipes in a grid
    byIngredientButton.setOnMouseClicked(e -> {
      cardScroll.setContent(container);
      container.getChildren().clear();
      cardScroll.setVvalue(0);
      bBrowse = false;
      bIngredient = true;
      bTag = false;
      ArrayList<IngredientManager> dearRecipes = new ArrayList<>();
      ingredientTrack = 0;
      
      dearRecipes = getRecipesByIngredientNumber(ingredientTrack);
      if (!dearRecipes.isEmpty()) {
        String firstref = dearRecipes.get(0).getName();
        Label label = new Label("Ingredient: " + firstref);
        label.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
        int gPaneColumn = 0;
        int gPanerow = 0;
        int gPaneGeneral = 0;
        GridPane theGrid = new GridPane();
        theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
        container.getChildren().addAll(label, theGrid);
        container.setStyle("-fx-background-color: white;");
        for (IngredientManager z : dearRecipes) {
          if (!firstref.equals(z.getName())) {
            firstref = z.getName();
            Label label2 = new Label(firstref);
            label2.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
            theGrid = new GridPane();
            theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
            gPaneColumn = 0;
            gPanerow = 0;
            gPaneGeneral = 0;
            container.getChildren().addAll(label2, theGrid);
            theGrid.setHgap(10);
            theGrid.setVgap(10);
          }
          gPaneGeneral++;
          try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
            Parent card = fxmlLoader.load();
            RecipeBoxController recipeToAdd = fxmlLoader.getController();
            recipeToAdd.setMainController(this);
            recipeToAdd.setRecipe(z.getRecipe());
            theGrid.add(card, gPaneColumn, gPanerow, 1, 1);
            gPaneColumn++;
        if (gPaneColumn % 4 == 0 && gPaneGeneral != 0) {
            gPaneColumn = gPaneColumn % 4;
            gPanerow++;
        }
        } catch (IOException p) {
            p.printStackTrace();
        }
        theGrid.setHgap(10);
        theGrid.setVgap(10);
          }
        }
        disableLabels(byIngredientButton);
    });

    // Trigger the infinite scroll for the tags and the ingredients recipes
    cardScroll.setOnScroll(event -> {
      // count the number of labels to know what to
      // delete in the Vbox in the scroll pane
      ObservableList<Node> elements = container.getChildren();
      int endnumberLabel = 0;
      for (Node element : elements) {
        if (element instanceof Label) {
          endnumberLabel++;                        
        }
      } 
            if (cardScroll.getVvalue() == 0.0 && endnumberLabel > 2) {
            } else {
              // Check which section is true to charge the right recipes
              if (bTag == true) {
                int numberOfLabel = 0;                
                for (Node element : elements) {
                  if (element instanceof Label) {
                    numberOfLabel++;
                  }
                }
                // Fetch the recipe according to ingredients
                // Print max 10 recipes, if fetch more than
                // 10 recipes, a buffer system is used to
                // print the remaining first 
                if (bufferRecipes.isEmpty()) {
                  System.out.println("The buffer is empty");
                  ingredientTrack += ingredientTrace;
                  if (ingredientTrack > totalTag) {
                    ingredientTrack = 0;
                  }
                  poolOfRecipes.clear();
                  poolOfRecipes = getRecipesByTagNumber(ingredientTrack); 
                } else if (!bufferRecipes.isEmpty() && bufferRecipes.size() < 6){
                  ingredientTrack += ingredientTrace;
                  if (ingredientTrack > totalTag) {
                    ingredientTrack = 0;
                  }
                  poolOfRecipes.clear();
                  poolOfRecipes = getRecipesByIngredientNumber(ingredientTrack);
                  for (IngredientManager l : bufferRecipes) {
                    poolOfRecipes.add(l);
                  }
                  bufferRecipes.clear();
                } else if (!bufferRecipes.isEmpty() && bufferRecipes.size() > 5) {
                  poolOfRecipes.clear();
                  for (IngredientManager l : bufferRecipes) {
                    poolOfRecipes.add(l);
                  }
                  bufferRecipes.clear();
                } 

                if (poolOfRecipes.size() >= 10) {
                  for (int i = 10; i < poolOfRecipes.size(); i++) {
                    bufferRecipes.add(poolOfRecipes.get(i));
                  }
                  int limit = poolOfRecipes.size();
                  for (int j = 10; j < limit; j++) {
                    poolOfRecipes.remove(poolOfRecipes.size() - 1);
                  }
                }
                
                // Print the fetched recipes
                if (!poolOfRecipes.isEmpty()) {
                  String firstref = poolOfRecipes.get(0).getName();
                  Label label = new Label("#" + firstref);
                  label.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
                  int gPaneColumn = 0;
                  int gPanerow = 0;
                  int gPaneGeneral = 0;
                  GridPane theGrid = new GridPane();
                  theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
                  container.getChildren().addAll(label, theGrid);
                  
                  container.setStyle("-fx-background-color: white;");
                  for (IngredientManager z : poolOfRecipes) {
                      if (!firstref.equals(z.getName())) {
                        firstref = z.getName();
                        Label label2 = new Label("#" + firstref);
                        label2.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
                        theGrid = new GridPane();
                        theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
                        gPaneColumn = 0;
                        gPanerow = 0;
                        gPaneGeneral = 0;
                        container.getChildren().addAll(label2, theGrid);
                        theGrid.setHgap(10);
                        theGrid.setVgap(10);
                      }
                      gPaneGeneral++;
                      try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
                        Parent card = fxmlLoader.load();
                        RecipeBoxController recipeToAdd = fxmlLoader.getController();
                        recipeToAdd.setMainController(this);
                        recipeToAdd.setRecipe(z.getRecipe());
                        theGrid.add(card, gPaneColumn, gPanerow, 1, 1);
                        gPaneColumn++;
                    if (gPaneColumn % 4 == 0 && gPaneGeneral != 0) {
                        gPaneColumn = gPaneColumn % 4;
                        gPanerow++;
                    }
                    } catch (IOException p) {
                        p.printStackTrace();
                    }
                    theGrid.setHgap(10);
                    theGrid.setVgap(10); 
                    }
                    for (int p = 0; p < numberOfLabel; p++) {
                      container.getChildren().remove(0);
                      container.getChildren().remove(0);
                    }                  
                  }
                  cardScroll.setVvalue(0.0);

              } else if (bIngredient == true) { 
                /*
                 * this section is for the ingredients
                 */
                int numberOfLabel = 0;                
                for (Node element : elements) {
                  if (element instanceof Label) {
                    numberOfLabel++;
                  }
                }  
                if (bufferRecipes.isEmpty()) {
                  System.out.println("The buffer is empty");
                  ingredientTrack += ingredientTrace;
                  if (ingredientTrack > totalIngredient) {
                    ingredientTrack = 0;
                  }
                  poolOfRecipes.clear();
                  poolOfRecipes = getRecipesByIngredientNumber(ingredientTrack); 
                } else if (!bufferRecipes.isEmpty() && bufferRecipes.size() < 6){
                  ingredientTrack += ingredientTrace;
                  if (ingredientTrack > totalIngredient) {
                    ingredientTrack = 0;
                  }
                  poolOfRecipes.clear();
                  poolOfRecipes = getRecipesByIngredientNumber(ingredientTrack);
                  for (IngredientManager l : bufferRecipes) {
                    poolOfRecipes.add(l);
                  }
                  bufferRecipes.clear();
                } else if (!bufferRecipes.isEmpty() && bufferRecipes.size() > 5) {
                  poolOfRecipes.clear();
                  for (IngredientManager l : bufferRecipes) {
                    poolOfRecipes.add(l);
                  }
                  bufferRecipes.clear();
                } 

                if (poolOfRecipes.size() >= 10) {
                  for (int i = 10; i < poolOfRecipes.size(); i++) {
                    bufferRecipes.add(poolOfRecipes.get(i));
                  }
                  int limit = poolOfRecipes.size();
                  for (int j = 10; j < limit; j++) {
                    System.out.println("Le j c'est " + bufferRecipes.size());
                    poolOfRecipes.remove(poolOfRecipes.size() - 1);
                  }
                }
                
                if (!poolOfRecipes.isEmpty()) {
                  String firstref = poolOfRecipes.get(0).getName();
                  Label label = new Label("Ingredient: " + firstref);
                  label.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
                  int gPaneColumn = 0;
                  int gPanerow = 0;
                  int gPaneGeneral = 0;
                  GridPane theGrid = new GridPane();
                  theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
                  container.getChildren().addAll(label, theGrid);
                  
                  container.setStyle("-fx-background-color: white;");
                  for (IngredientManager z : poolOfRecipes) {
                      if (!firstref.equals(z.getName())) {
                        firstref = z.getName();
                        Label label2 = new Label("Ingredient: " + firstref);
                        label2.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
                        theGrid = new GridPane();
                        theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
                        gPaneColumn = 0;
                        gPanerow = 0;
                        gPaneGeneral = 0;
                        container.getChildren().addAll(label2, theGrid);
                        theGrid.setHgap(10);
                        theGrid.setVgap(10);
                      }
                      gPaneGeneral++;
                      try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
                        Parent card = fxmlLoader.load();
                        RecipeBoxController recipeToAdd = fxmlLoader.getController();
                        recipeToAdd.setMainController(this);
                        recipeToAdd.setRecipe(z.getRecipe());
                        theGrid.add(card, gPaneColumn, gPanerow, 1, 1);
                        gPaneColumn++;
                    if (gPaneColumn % 4 == 0 && gPaneGeneral != 0) {
                        gPaneColumn = gPaneColumn % 4;
                        gPanerow++;
                    }
                    } catch (IOException p) {
                        p.printStackTrace();
                    }
                    theGrid.setHgap(10);
                    theGrid.setVgap(10); 
                    }
                    for (int p = 0; p < numberOfLabel; p++) {
                      container.getChildren().remove(0);
                      container.getChildren().remove(0);
                    }                  
                  }
                  cardScroll.setVvalue(0.0);
              }
            }
  
    });


    /**
     * This is the weekly selection button, it will show all the recipes in the
     * users weekly.
     */
    weekSelection.setOnMouseClicked(new EventHandler<MouseEvent>() {
      
      @Override
      public void handle(MouseEvent event) {
        String[] days = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        LocalDate date = LocalDate.now();
        DateTimeFormatter wtf = DateTimeFormatter.ofPattern("w");
        int weekNow = Integer.parseInt(date.format(wtf));
        cardScroll.setContent(container);
        disableLabels(weekSelection);
        /*on this line is Mohammed code
        // Gets the user object from the stage.
        Stage stage = (Stage) cardAnchor.getScene().getWindow();
        User u = (User) stage.getUserData();
        MenuButton weekMenu = new MenuButton("Week " + Integer.toString(weekNow));
        // Checks the nodes in the page and clears the page.
        // Adding a menuButton so the user can choose a week.
        cardAnchor.getChildren().remove(0, cardAnchor.getChildren().size());
        cardAnchor.getChildren().add(weekMenu);
        */

        Stage stage = (Stage) cardScroll.getScene().getWindow();
        User u = (User) stage.getUserData();
        MenuButton weekMenu = new MenuButton("Week " + Integer.toString(weekNow));
        container.getChildren().clear();
        container.getChildren().add(weekMenu);
        for (String day : days) {
          printweekly(weekNow, day, container);
        }

        for (int i : db.getWeeks(u.getUserId())) {
          MenuItem week = new MenuItem("Week " + i);
          weekMenu.getItems().add(week);
          // Handle all the choices in the menu button.
          week.setOnAction(new EventHandler<ActionEvent>() {
            int weekNumber = 0;

            @Override
            public void handle(ActionEvent event) {
              container.getChildren().clear();
              container.getChildren().add(weekMenu);
              String text = week.getText();
              if (text.length() <= 6) {
                weekNumber = Integer.parseInt(text.substring(text.length() - 1, text.length()));
              } else {
                weekNumber = Integer.parseInt(text.substring(text.length() - 2, text.length()));
              }
              weekMenu.setText(week.getText());
              // Prints the weekly recipes based on the week.
              for (String day : days) {
                printweekly(weekNumber, day, container);
              }
            }

          });
        }
      }
      
    });

    /**
     * Starts a new stage so the user can create a recipe.
     */
    newRecipe.setOnMouseClicked(Event -> {
      bBrowse = false;
      bIngredient = false;
      bTag = false;
      cardScroll.setContent(container);
      disableLabels(newRecipe);
      try {
        Stage stage = (Stage) browseButton.getScene().getWindow();
        User u = (User) stage.getUserData();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CreateRecipe.fxml"));
        Parent root = loader.load();
        CreateRecipeController crc = loader.getController();
        crc.setUserData(u);
        crc.setCardAnchor(cardAnchor);
        container.getChildren().clear();
        container.getChildren().add(root);
        cardScroll.setContent(new Group());
        cardScroll.setContent(container);

      } catch (Exception e1) {
        e1.printStackTrace();
      }

    });

    /**
     * Starts new stage to display shopping list
     */
    shoppingList.setOnMouseClicked(Event -> {
      cardScroll.setContent(container);
      disableLabels(shoppingList);
      try {
        Stage stage = (Stage) browseButton.getScene().getWindow();
        User user = (User) stage.getUserData();
        stage.getScene().setUserData(user);
        
        // added new fxml, errors on others and ease of testing
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ShoppingCart.fxml"));
        
        // custom controller factory for fxmlloader, using lambda expression
        // sets properties before initialize, customizes the controller instantiation process for the loader
        loader.setControllerFactory(clazz -> {
          if (clazz == ShoppingCartController.class) {
            ShoppingCartController cart = new ShoppingCartController();
            //cart.setCardAnchor(cardAnchor);
            cart.setUser(db.getUser());
            return cart;
          } else {
            try {
              return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }
        });

        Parent root = loader.load();
        ShoppingCartController cart = loader.getController();
        cart.setUser(db.getUser());
        cart.setCardAnchor(cardAnchor);
        container.getChildren().clear();
        container.getChildren().add(root);
        cardScroll.setContent(new Group());
        cardScroll.setContent(container);
        
        //container.getChildren().setAll(root);
        
        //cart.setCardAnchor(cardAnchor);
        //cardAnchor.getChildren().setAll(root);
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    });

}

    // this will set a default image if no image is set externally 
    /* 
    proPic = new Image("/images/def.jpg"); 

    imageView.setImage(proPic);

    //size of the image-view
    imageView.setFitWidth(50); 
    imageView.setFitHeight(50); 

    // used to make the image into a circle 
    Circle clip = new Circle(imageView.getFitWidth()/2, imageView.getFitHeight()/2, imageView.getFitWidth()/2);

    // set the clip of the ImageView
    imageView.setClip(clip);

  public void fireHomeEvent() {
    Event.fireEvent(homeButton, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
        0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
        true, true, true, true, true, true, null));
  }

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
      e.printStackTrace();
    }

  }
  /**
   * set the user name dynamically.
   * 
   * @param name name to be set.
   */
  /*
   * public void setName(String name) {
   * nameLabel.setText(name);
   * }
   */

  /**
   * Returns the text in the Searchbar.
   *
   * @return search text
   */
  public String getSearchText() {
    return searchBar.getText();
  }

  //RecipeBoxController rb = new RecipeBoxController();

  /**
   * This will print the weekly recipes based on the week.
   * Prints all days of the week with the recipes for each day.
   * 
   * @param weekNo is the week chosen.
   * @param day    is the day of the week
   */
  public void printweekly(int weekNo, String day, VBox container) {
    Stage stage = (Stage) browseButton.getScene().getWindow();
    User u = (User) stage.getUserData();
    ArrayList<Recipe> recipes = db.getWeeklyRecipes(weekNo, u.getUserId(), day);
    if (recipes.size() != 0) {
      Label label = new Label(day);
      label.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-fill: #343a40; -fx-font-size: 32px; -fx-padding: 8px 32px 8px 32px;");
      container.getChildren().add(label);
    }
    /*double layout = 0;
    if (container.getChildren().size() == 1) {
      layout = 50;
    } else {
      container.getChildren().get(container.getChildren().size() - 1);
      double height = container.boundsInParentProperty().getValue().getHeight();
      layout = container.getLayoutY() + height + 100;
    }
    this.cardAnchor.getChildren().add(pane);
    pane.setLayoutY(layout);*/
    printToScreen(recipes, container);
  }

  /**
   * Prints all the recipes in the database, for browsing.
   */
  /*public void printAll() {
    ArrayList<Recipe> recipes = db.getRecipes();
    printToScreen(recipes, cardAnchor, cardScroll);
  }*/

  /**
   * Prints all the recipes that is taken as a paramater.
   * 
   * @param recipes is the recipes to be printed.
   */
  /*public void printAll(ArrayList<Recipe> recipes) {
    ArrayList<Recipe> distinctRecipes = new ArrayList<>();

    for (Recipe r : recipes) {
      boolean duplicate = false;
      for (int i = recipes.indexOf(r) + 1; i < recipes.size(); i++) {
        if ((r.getRecipeId() == recipes.get(i).getRecipeId())) {
          duplicate = true;
          break;
        }
      }
      if (!duplicate) {
        distinctRecipes.add(r);
      }
    }
    printToScreen(distinctRecipes, cardAnchor, cardScroll);
  }*/

  /**
   * Prints all the favourite recipes of the user.
   */
  /*public void printFavourites() {
    ArrayList<Recipe> recipes = db.getFavouriteRecipes(db.getUser().getUserId());
    printToScreen(recipes, cardAnchor, cardScroll);
  }*/

  /**
   * This method is used to do all the printing.
   * It creates a card of the recipe by calling the create card method in the
   * RecipeBoxController.
   * Checks if the recipe is in the favourites of the user, if it is then the
   * favourite button will be red.
   * Checks if the recipe is in the weekly of the user, if it is then the add
   * button will be red and the text will be "remove"
   * 
   * @param recipes    is the recipes to be printed.
   * @param cardAnchor is the AnchorPane that holds the recipe cards.
   * @param cardScroll is holding the anchorpane so the user can scroll.
   */
  private void printToScreen(ArrayList<Recipe> recipes, VBox container) {

      bBrowse = false;
      bIngredient = false;
      bTag = false;
      if (!recipes.isEmpty()) {
        int gPaneColumn = 0;
        int gPanerow = 0;
        int gPaneGeneral = 0;
        GridPane theGrid = new GridPane();
        theGrid.setStyle("-fx-padding: 8px 32px 16px 32px;");
        container.getChildren().add(theGrid);
        container.setStyle("-fx-background-color: white;");
        for (Recipe z : recipes) {
          gPaneGeneral++;
          try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));
            Parent card = fxmlLoader.load();
            RecipeBoxController recipeToAdd = fxmlLoader.getController();
            recipeToAdd.setMainController(this);
            recipeToAdd.setRecipe(z);
            theGrid.add(card, gPaneColumn, gPanerow, 1, 1);
            gPaneColumn++;
        if (gPaneColumn % 4 == 0 && gPaneGeneral != 0) {
            gPaneColumn = gPaneColumn % 4;
            gPanerow++;
        }
        } catch (IOException p) {
            p.printStackTrace();
        }
        theGrid.setHgap(10);
        theGrid.setVgap(10);
          }
        }

    /*// Gets the user object from the stage.
    cardScroll.setContent(container);

    // Gets all the users favourites.
    ArrayList<Recipe> favRecipes = db.getFavouriteRecipes(db.getUser().getUserId());

    double ylayout = 50;

    // Define the dimensions and padding for each card.
    double cardWidth = 170.0;
    double cardHeight = 320.0;
    double xPadding = 5.0;
    double yPadding = 10.0;

    // Loop through each recipe and create a card for it.
    for (int i = 0; i < recipes.size(); i++) {
      Recipe r = recipes.get(i);
      StackPane card = rb.createCard(r);

      // Check if the recipe is in the user's favourite recipes
      if (favRecipes.stream().anyMatch(fav -> fav.getRecipeId() == r.getRecipeId())) {
        Pane pane = (Pane) card.getChildren().get(0);
        VBox vbox = (VBox) pane.getChildren().get(0);
        HBox hbox = (HBox) vbox.getChildren().get(1);
        StackPane stackpane = (StackPane) hbox.getChildren().get(1);
        ImageView heart = (ImageView) stackpane.getChildren().get(1);
        ImageView emptyHeart = (ImageView) stackpane.getChildren().get(0);
        heart.setVisible(true);
        emptyHeart.setVisible(false);
      }
      // Add the card to the cardAnchor
      cardAnchor.getChildren().add(card);
      // Calculate the row and column numbers of the current card.
      int row = i / 5;
      int col = i % 5;

      // Calculate the new x and y layout coordinates based on the row and column
      // numbers.
      double x = col * (cardWidth + xPadding);
      double y = ylayout + row * (cardHeight + yPadding);

      // Update the layout coordinates of the card.
      card.setLayoutX(x);
      card.setLayoutY(y);

    }*/

  }

  /*public void printAllTagged(String tag) {
    ArrayList<Recipe> recipes = db.getAllTaggedRecipes(tag);
    printAll(recipes);
  }*/

  public void emulateButtonClick(ArrayList<Recipe> recipes) {

    MouseEvent mouseEvent = new MouseEvent(
        MouseEvent.MOUSE_CLICKED,
        byTagButton.getLayoutX(),
        byTagButton.getLayoutY(),
        byTagButton.getLayoutX(),
        byTagButton.getLayoutY(),
        MouseButton.PRIMARY, 1,
        false, false, false, false,
        true, false, false, true, true, true, null);

    byTagButton.fireEvent(mouseEvent);
  }

  /**
   * This method fetch the recipes by calling 3 ingredients
   * according to the number there are in the whole list.

   * @param a is the number to fetch in the list of all ingredients.
   * @return an array of Ingredient Manager.
   */
  public ArrayList<IngredientManager> getRecipesByIngredientNumber(int a) {
    ArrayList<IngredientManager> allIngredient = new ArrayList<>();
    try {
      String queryIngre = "select distinct ingredients.name from chanuka1.ingredients limit 3 offset " + a +";";
      Connection connectionIngre = dbc.getDBConnection();
      Statement statementIngre = connectionIngre.createStatement();
      ResultSet queryOutputIngre = statementIngre.executeQuery(queryIngre);

      while(queryOutputIngre.next()) {
        String ingredients = queryOutputIngre.getString("name");
        ArrayList<Recipe> recipes = dbu.getRecipeByIngredient(ingredients);
        for (Recipe r : recipes) {
          allIngredient.add(new IngredientManager(ingredients, r));
        }
      }
    } catch (Exception p) {
      p.printStackTrace();
    }
    return allIngredient;
  }

  /**
   * This method fetch the recipes by calling 3 tags
   * according to the number there are in the whole list.

   * @param a is the number to fetch in the list of all tags.
   * @return an array of Ingredient Manager.
   */
  public ArrayList<IngredientManager> getRecipesByTagNumber(int a) {
    ArrayList<IngredientManager> allTag = new ArrayList<>();
    try {
      String queryIngre = "select distinct tags.name from chanuka1.tags limit 3 offset " + a +";";
      Connection connectionIngre = dbc.getDBConnection();
      Statement statementIngre = connectionIngre.createStatement();
      ResultSet queryOutputIngre = statementIngre.executeQuery(queryIngre);

      while(queryOutputIngre.next()) {
        String tags = queryOutputIngre.getString("name");
        ArrayList<Recipe> recipes = dbu.getAllTaggedRecipes(tags);
        for (Recipe r : recipes) {
          allTag.add(new IngredientManager(tags, r));
        }
      }
    } catch (Exception p) {
      p.printStackTrace();
    }
    return allTag;
  }

  /**
   * This method disable the lables.

   * @param h is a Hbox where the labels are.
   */
  private void disableLabels(HBox h){
    HBox[] labels = {browseButton, weekSelection, homeButton, newRecipe, byTagButton, byIngredientButton};

    for(HBox i : labels){
      if(i == h){
        i.setDisable(true);
      } else {
        i.setDisable(false);
      }
      
    }
  }

  /**
   * Set the booleans to false to prevent infinite scroll
   */
  public void setBooleans() {
      bBrowse = false;
      bIngredient = false;
      bTag = false;
  }

}

