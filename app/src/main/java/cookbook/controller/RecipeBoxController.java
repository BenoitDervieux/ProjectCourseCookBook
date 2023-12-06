package cookbook.controller;

import java.awt.Color;
import java.awt.Paint;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.checkerframework.checker.units.qual.C;

import cookbook.DBUtils;
import cookbook.Recipe;
import cookbook.User;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class RecipeBoxController {
    @FXML
    private Button addButton, saveWeekButton, shareButton;

    @FXML
    private HBox pictureBox;

    @FXML
    private Label recipeName, summaryLabel;
    @FXML
    private StackPane hearts, recipeStack;
    @FXML
    private ImageView favouriteButton, removeFavouriteButton, recipeImageV;
    @FXML
    private Shape oneStar, twoStar, threeStar, fourStar, fiveStar;
    @FXML
    private Pane addToWeekPane, recipeCard;
    @FXML
    private AnchorPane weekAnchor;
    @FXML
    private ScrollPane weekScroll;
    @FXML
    private VBox weeksVBox;

    private Recipe recipe;
    private boolean favourite;
    private Stage stage;
    private MainController mainController;
    DBUtils db = DBUtils.getInstance();
    public void initialize() {

        Rectangle clipRectangle = new Rectangle(recipeImageV.getFitWidth(), recipeImageV.getFitHeight());
        clipRectangle.setArcWidth(15);
        clipRectangle.setArcHeight(15);
        recipeImageV.setClip(clipRectangle);

  
        /**
         * This is the favourite button, when the user wants to add to favourites.
         */
        hearts.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                
                if (favouriteButton.isVisible()) {
                    favouriteButton.setVisible(false);
                    removeFavouriteButton.setVisible(true);
                    recipe.setFavourite(true);
                    db.addtoFavourite(recipe, db.getUser().getUserId());
                } else {
                    removeFavouriteButton.setVisible(false);
                    favouriteButton.setVisible(true);
                    recipe.setFavourite(false);
                    db.removeFromFavourite(recipe, db.getUser().getUserId());
                }
            }
            
        });

        /**
         * This is the weekly button, when the user wants to add to weekly.
         */
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            int i = 0;
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("weekbox.fxml"));
                    Parent root = loader.load();
                    weekboxController wb = loader.getController();
                    Stage ingredientStage = new Stage();
                    ingredientStage.initModality(Modality.APPLICATION_MODAL);
                    ingredientStage.initOwner(stage);
                    ingredientStage.setScene(new Scene(root));
                    wb.setStage(ingredientStage);
                    ingredientStage.setUserData(getUser());
                    wb.setCard(recipe);
                    wb.setRecipe(recipe);
                    ingredientStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        
        });
        
    
        shareButton.setOnAction(Event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("recipeMessages.fxml"));
            Parent root;
            try {
                root = loader.load();
                sendRecipeController wb = loader.getController();
                Stage sendRecipeStage = new Stage();
                sendRecipeStage.initModality(Modality.APPLICATION_MODAL);
                sendRecipeStage.initOwner(stage);
                sendRecipeStage.setScene(new Scene(root));
                wb.setStage(sendRecipeStage);
                sendRecipeStage.setUserData(getUser());
                wb.setCard(recipe);
                wb.setRecipe(recipe);
                sendRecipeStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        });

        recipeImageV.setOnMouseClicked(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ViewRecipe.fxml"));
            try {
                Parent root = loader.load();
                ViewRecipeController controller = loader.getController();
                controller.setMainController(this.mainController);
                controller.setBooleansFromControlller();
                //set user before initializing to avoid favorite issues
                controller.setUser(getUser());
                controller.setTags(recipe.getTags());
                controller.setBackUpImage(recipeImageV.getImage());
                controller.initializeWithRecipe(recipe);



                Scene mainScene = hearts.getScene();
                Parent mainRoot = mainScene.getRoot();
                VBox mainView = (VBox) mainRoot.lookup("#contentContainer");
                mainView.getChildren().clear();
                mainView.getChildren().add(root);
                
                // add an event handler to go to main screen
                mainScene.setOnKeyPressed(KeyEvent -> {
                    if (KeyEvent.getCode() == KeyCode.ESCAPE) {
                        getCurrentPage(root);
                    }
                });

               
               
            } catch (Exception error) {
               error.printStackTrace();
            }
        
        });
    }

    public User getUser() {
        Stage stage = (Stage) hearts.getScene().getWindow();
        User user = (User) stage.getUserData();
        return user;             
    }

    @FXML
    void fiveStarClicked(MouseEvent event) {

    }

    @FXML
    void fourStarClicked(MouseEvent event) {

    }

    @FXML
    void oneStarClicked(MouseEvent event) {

    }

    @FXML
    void threeStarClicked(MouseEvent event) {

    }

    @FXML
    void twoStarClicked(MouseEvent event) {

    }

    /**
     * Set the name of the recipe
     *
     * @param name to be set.
     */
    public void setName(String name) {
        recipeName.setText(name);

    }
    
    public void setLabel(String label) {
        summaryLabel.setText(label);
    }

    public void setImage(String path, Recipe recipe) {
        InputStream stream;
        
        try {
            stream = new FileInputStream(path);
            Image image = new Image(stream);
            recipeImageV.setImage(image);
            
            // adds a tooltip to the image where the description appears when hovering
            Tooltip tooltip = new Tooltip(recipe.getDescription());
            Tooltip.install(recipeImageV, tooltip);
            tooltip.setWrapText(true); 
            tooltip.setMaxHeight(125);
            tooltip.setMaxWidth(250);
        } catch (FileNotFoundException e) {
          // set default image
          Image image =new Image("/recipe_images/def.jpg");
          recipeImageV.setImage(image);
          System.out.println("No image set, setting default image");
        }
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public boolean isFavourite() {
        if (this.recipe.isFavourite()) {
            return true;
        }
        return false;
    }
    /**
     * Sets the recipe details for the pane card
     * @param recipe is the recipe to be printed.
     */
    public void setRecipe(Recipe recipe) {
        
        this.setName(recipe.getName());
        this.setLabel(recipe.getDescription());
        
        String imageName = recipe.getRecipeId() + ".jpg";
        String imagePath = "src/main/resources/recipe_images/" + imageName;

        this.setImage(imagePath, recipe);
        recipe.setUserId(recipe.getUserId());
        this.recipe = recipe;
        int randomNumber = (int) recipe.getRating();
        //fiveStar, fourStar, threeStar, twoStar, oneStar
        if (randomNumber >= 1) {
            oneStar.setStyle("-fx-fill: #ffe066");
        }
        if (randomNumber >= 2) {
            twoStar.setStyle("-fx-fill: #ffe066");
        }
        if (randomNumber >= 3) {
            threeStar.setStyle("-fx-fill: #ffe066");
        }
        if (randomNumber >= 4) {
            fourStar.setStyle("-fx-fill: #ffe066");
        }
        if (randomNumber >= 5) {
            fiveStar.setStyle("-fx-fill: #ffe066");
        }

        // set the favorite button
        setFavoriteButton();
    }

    /**
     * This method creates a recipe card/Fxml file, with an image, name, summary, button and favourite button.
     * @param recipe is the recipe that is going to be printed on the card.
     * @return a pane with recipe details.
     */
    public StackPane createCard(Recipe recipe) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("RecipeBox.fxml"));       
        try {
            StackPane root = (StackPane) loader.load();
            RecipeBoxController theRecipe = loader.getController();
            theRecipe.setRecipe(recipe);
          return root;
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
          return null;
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
        
        
    }
    
    public HBox getCurrentPage(Parent subRoot) {
        Scene scene = subRoot.getScene();
        Parent root = scene.getRoot();
        VBox mainSelection = (VBox) root.lookup("#mainSelection");
        HBox currentPage;
        MouseEvent event = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, true, false, false, null);
        
        for (Node node : mainSelection.getChildren()) {
            
            if (node instanceof HBox && node.isDisabled()) {     
                currentPage = (HBox) node;
                currentPage.setDisable(false);
                currentPage.fireEvent(event);
            } 
        }
        return null;
    }

    /**
     * sets the favorite button
     */
    private void setFavoriteButton(){
        
        if(recipe.isFavourite()){
            favouriteButton.setVisible(false);
            removeFavouriteButton.setVisible(true);
        }
        
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
      }
    
}
    
