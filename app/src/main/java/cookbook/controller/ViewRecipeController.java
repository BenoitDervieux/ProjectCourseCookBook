package cookbook.controller;

import java.util.ArrayList;

import cookbook.Comment;
import cookbook.DBUtils;
import cookbook.Ingredient;
import cookbook.Recipe;
import cookbook.Tag;
import cookbook.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.awt.Toolkit;
import java.io.File;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class ViewRecipeController {

  @FXML
  ImageView heroImage, editBtnName, addToFavoriteBtn, editTagsBtn, editBtnDesc, editBtnIngredients, editBtnInstructions;
  @FXML
  TextField portionText;
  @FXML
  private TextArea txtAreaDesc;
  @FXML
  Label portionLabel, nameLabel;
  @FXML
  private HBox tagBox, buttonHBox, tagContainer;
  @FXML
  private Button plusPortionBtn, minusPortionBtn;

  @FXML
  private VBox ingredientsVBox, instructionsVBox, commentVBox;

  @FXML
  VBox ingredientsContainer, instructionsContainer, commentVContainer;

  @FXML
  ListView<String> lstViewInstructions;

  @FXML
  ListView<String> lstViewComments;

  @FXML
  HBox commentContainer;

  @FXML
  SVGPath oneStar, twoStar, threeStar, fourStar, fiveStar;


  private Image mainImage;
  private boolean isFavorite = false;
  private Recipe recipe;
  private DBUtils dbUtils = DBUtils.getInstance();
  private TextField comTextField;
  private ObservableList<String> commentsList = FXCollections.observableArrayList();
  private User user;
  private ArrayList<Tag> tags = new ArrayList<>();
  private String stringComment;
  private Comment comment;
  private String tempText;
  private int rating = 0;
  private MainController mainController;
  private Image backUpImage;

  private void setTempText(String text) {
    this.tempText = text;
  }

  private String getTempText() {
    return tempText;
  }

  public void initialize() {

    // default label for name
    setName("Name of the Recipe");
    //this.mainController.setBooleans();

    // this will set a default image if no image is set externally
    mainImage = new Image("/recipe_images/def.jpg");
    heroImage.setImage(mainImage);

    // set a clip to apply rounded border to the original image.
    Rectangle clip = new Rectangle(
        //heroImage.getFitWidth() * 0.73, heroImage.getFitHeight());
        heroImage.getFitWidth(), heroImage.getFitHeight());

    clip.setArcWidth(20);
    clip.setArcHeight(20);

    // set the clip of the ImageView
    heroImage.setClip(clip);

    // change the ingredients when the portion is directly changed
    portionText.textProperty().addListener((observable, oldValue, newValue) -> {
      try {
        String currentCount = oldValue.trim();
        if (currentCount != "") {
          int count = Integer.parseInt(currentCount);
          updateIngredientPortions(count);
        }
      } catch (Exception e) {
        portionText.setText("" + recipe.getPortion());
        loadIngredients(recipe.getRecipeId());
      }
      txtAreaDesc.setFont(Font.font("Roboto", FontPosture.ITALIC, 19));
    });


    // selects the servings to be overwritten
    portionText.setOnMouseClicked(event -> {
      portionText.selectAll();
    });

    // remove focus on enter
    portionText.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        portionText.getParent().requestFocus();
        try {
          String currentCount = portionText.getText().trim();
          // check validity
          Integer.parseInt(currentCount);

        } catch (Exception e) {
          Alert invalidType = new Alert(AlertType.ERROR, "Invalid entry for portion");
          invalidType.showAndWait();
          // reset the ingredients
          portionText.setText("" + recipe.getPortion());
          loadIngredients(recipe.getRecipeId());
        }
      }
    });

    editTagsBtn.setOnMouseClicked(event -> {

      try {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("TagsCard.fxml"));
        Parent root = loader.load();
        AddTagController tagsController = loader.getController();
        if(user.getUserId() != recipe.getUserId()){
            
          tagsController.setExistingTags(getEditableTags(tags));
        } else {
          tagsController.setExistingTags(tags);
        }
        Stage tagStage = new Stage();
        tagStage.initModality(Modality.APPLICATION_MODAL);
        tagStage.setScene(new Scene(root));
        tagStage.showAndWait();

        if (tagsController.getTagsUpdated()) {
          ArrayList<String> newTags = tagsController.getTags();
          updateTags(newTags);
        }
      } catch (Exception e) {
        System.out.println("An error has occured: " + e.getMessage());
      }
    });

    // editRating.setOnMouseClicked(event -> {
    // System.out.println("You clicked on the add rating pen!");
    // });

    oneStar.setOnMouseClicked(event ->  {
      passRating(1);
      dbUtils.setRating(recipe, rating, user);
    });
    twoStar.setOnMouseClicked(event -> {
      passRating(2);
      dbUtils.setRating(recipe, rating, user);
    });
    threeStar.setOnMouseClicked(event -> {
      passRating(3);
      dbUtils.setRating(recipe, rating, user);
    });
    fourStar.setOnMouseClicked(event -> {
      passRating(4);
      dbUtils.setRating(recipe, rating, user);
    });
    fiveStar.setOnMouseClicked(event -> {
      passRating(5);
      dbUtils.setRating(recipe, rating, user);
    });

  }

  public void passRating(int passedRating) {

    rating = passedRating;

    oneStar.setFill(Color.BLACK);
    twoStar.setFill(Color.BLACK);
    threeStar.setFill(Color.BLACK);
    fourStar.setFill(Color.BLACK);
    fiveStar.setFill(Color.BLACK);

    switch (rating) {
      case 1:
        oneStar.setFill(Color.valueOf("#ffd43b"));
        break;
      case 2:
        oneStar.setFill(Color.valueOf("#ffd43b"));
        twoStar.setFill(Color.valueOf("#ffd43b"));
        break;
      case 3:
        oneStar.setFill(Color.valueOf("#ffd43b"));
        twoStar.setFill(Color.valueOf("#ffd43b"));
        threeStar.setFill(Color.valueOf("#ffd43b"));
        break;
      case 4:
        oneStar.setFill(Color.valueOf("#ffd43b"));
        twoStar.setFill(Color.valueOf("#ffd43b"));
        threeStar.setFill(Color.valueOf("#ffd43b"));
        fourStar.setFill(Color.valueOf("#ffd43b"));
        break;
      case 5:
        oneStar.setFill(Color.valueOf("#ffd43b"));
        twoStar.setFill(Color.valueOf("#ffd43b"));
        threeStar.setFill(Color.valueOf("#ffd43b"));
        fourStar.setFill(Color.valueOf("#ffd43b"));
        fiveStar.setFill(Color.valueOf("#ffd43b"));
        break;
    }

    
  }

  /**
   * Event handler for the favorite button click.
   *
   * @param e the event
   */
  public void favoriteButtonHandler(Event e) {
    // change the favorite status
    isFavorite = !isFavorite;
    // get the current user

    if (isFavorite) {
      // change Icon
      addToFavoriteBtn.setImage(new Image(getClass().getResourceAsStream("/util/redHeart.png")));
      // update DB
      dbUtils.addtoFavourite(recipe, user.getUserId());
    } else {
      // change Icon
      addToFavoriteBtn.setImage(new Image(getClass().getResourceAsStream("/util/empty_heart.png")));
      // update DB
      dbUtils.removeFromFavourite(recipe, user.getUserId());
    }

  }

  /**
   * Initialize view with a recipe.
   *
   * @param r the recipe
   */
  public void initializeWithRecipe(Recipe r) {
    this.recipe = r;
    int resId = r.getRecipeId();
    setName(r.getName());
    try {
      // setImage(resId);
      File imageFile = new File("src/main/resources/recipe_images/" + r.getRecipeId() + ".jpg");
      if (imageFile.exists()) {
          Image mainImage = new Image(imageFile.toURI().toString());
          heroImage.setImage(mainImage);
      } else {
        heroImage.setImage(backUpImage);
      }
    } catch (Exception e) {
      System.out.println("issue in loading image");
    }
   
    setDescription(r.getDescription());
    setPortion(r.getPortion());
    dbUtils.getComments(resId);
    loadComment(resId);

    // loading tags
    loadTags(resId);

    // loading ingredients
    loadIngredients(resId);

    // leading instructions
    loadInstructions(resId);

    // set favorite button
    isFavorite = dbUtils.checkIsFavorite(user.getUserId(), r.getRecipeId());
    if (isFavorite) {
      // change Icon
      addToFavoriteBtn.setImage(new Image(getClass().getResourceAsStream("/util/redHeart.png")));
    } else {
      // change Icon
      addToFavoriteBtn.setImage(new Image(getClass().getResourceAsStream("/util/empty_heart.png")));
    }
  }

  /**
   * Increase the servings by one.
   */
  public void increaseCount() {
    String currentCount = portionText.getText().trim();
    try {
      int newCount = Integer.parseInt(currentCount) + 1;
      portionText.setText(String.valueOf(newCount));
    } catch (Exception e) {
      Alert invalid = new Alert(AlertType.ERROR, "Invalid entry for portion");
      invalid.showAndWait();
      // reset the ingredients
      portionText.setText("" + recipe.getPortion());
      loadIngredients(recipe.getRecipeId());
      e.printStackTrace();
    }

  }

  /**
   * Decrease the servings by one.
   */
  public void decreaseCount() {
    String currentCount = portionText.getText().trim();
    try {
      int newCount = Integer.parseInt(currentCount) - 1;
      if (newCount > 0) {
        portionText.setText(String.valueOf(newCount));
      } else {
        Alert invalidSize = new Alert(AlertType.ERROR, "Minimum portion you can set is 1");
        invalidSize.showAndWait();
      }

    } catch (Exception e) {
      Alert invalidType = new Alert(AlertType.ERROR, "Invalid entry for portion");
      invalidType.showAndWait();
      // reset the ingredients
      portionText.setText("" + recipe.getPortion());
      loadIngredients(recipe.getRecipeId());
    }

  }

  /**
   * This method will load the ingredients to the screen.
   */
  public void loadIngredients(int recipeId) {
    ArrayList<Ingredient> ingredients = dbUtils.getIngredients(recipeId);
    ingredientsContainer.getChildren().clear();
    ingredientsContainer.setSpacing(15);
    for (var i : ingredients) {
      HBox ingredientContainerHBox = new HBox();
      ingredientContainerHBox.setAlignment(Pos.CENTER_LEFT);
      ingredientContainerHBox.setSpacing(10);
      ingredientContainerHBox.setPadding(new Insets(10, 10, 10, 10));
      ingredientContainerHBox.setStyle("-fx-border-color: #ccc; -fx-background-radius: 5px; -fx-background-color: #f1f3f5; -fx-border-radius: 5px;");

      Label lName = new Label();
      String unitName = i.getUnit();
      // incase of unit name is null st. will be used
      unitName = unitName == null ? "st. " : unitName;
      // add the s at the end if the amount is larger than 1
      unitName = i.getAmount() > 1 && !unitName.equals("st. ") ? unitName + "s" : unitName;
      lName.setText(unitName + " of " + i.getName());
      lName.setFont(Font.font("Roboto Light", FontWeight.LIGHT, FontPosture.ITALIC, 18));
 
      Label lPortion = new Label();
      lPortion.setText("" + i.getAmount());
      lPortion.setFont(Font.font("Roboto Light", FontWeight.LIGHT, FontPosture.ITALIC, 18));

      ingredientContainerHBox.getChildren().addAll(lPortion, lName);
      ingredientsContainer.getChildren().add(ingredientContainerHBox);

    }
  }

  /**
   * This method will update the ingredients based on the current servings
   * requested.
   */
  public void updateIngredientPortions(int prevServings) {

    for (Node node : ingredientsContainer.getChildren()) {
      HBox ingredientContainerHBox = (HBox) node;
      Label tPortion = (Label) ingredientContainerHBox.getChildren().get(0);
      double currentPortion = Double.parseDouble(tPortion.getText());
      int requiredPortionSize = Integer.parseInt(portionText.getText());
      double newPortion;
      newPortion = currentPortion / prevServings * requiredPortionSize;
      tPortion.setText(String.format("%.2f", newPortion));
    }
  }

  /**
   * Loads the tags of a recipe and adds to display.
   *
   * @param recipeId the ID of the recipe to be displayed
   */
  public void loadTags(int recipeId) {

    for (var t : tags) {
      printTag(t.getName());
    }
  }

  /**
   * Updates the tags incase the tags are changed
   * 
   * @param tags
   */
  public void updateTags(ArrayList<String> tagsArr) {
    tagContainer.getChildren().clear();
    /*for(var t: tagsArr){
      System.out.println("updated tags list:"+ t);
    }*/
    
    ArrayList<Tag> permaTags = getPermaTagsAsTags(tags);
    
    // update the database
    tags = dbUtils.updateRecipeTags(user.getUserId(), recipe, tagsArr);

    if(user.getUserId() != recipe.getUserId()){
      tags.addAll(permaTags);
    }
      
    for (var  t : tags) {
      printTag(t.getName());
    } 

    recipe.setTags(tags);
  }

  /**
   * Load's instructions based on a recipe ID.
   *
   * @param recipeId the recipe id
   */
  public void loadInstructions(int recipeId) {
    ArrayList<String> instructions = dbUtils.getInstructions(recipeId);
    int step = 1;
    instructionsContainer.setSpacing(15);
    for (var inst : instructions) {
      HBox instruction = new HBox();
      instruction.setSpacing(10);
      instruction.setPadding(new Insets(10, 10, 10, 10));
      instruction.setStyle("-fx-border-color: #ccc; -fx-background-radius: 5px; -fx-border-radius: 5px;");
      Label stepLabel = new Label(String.valueOf(step) + ". ");
      stepLabel.setFont(Font.font("Roboto Light", FontWeight.LIGHT, FontPosture.ITALIC, 18));
      Label instructionLabel = new Label();
      instructionLabel.setText(inst);
      instructionLabel.setFont(Font.font("Roboto Light", FontWeight.LIGHT, FontPosture.ITALIC, 18));
      instructionLabel.setWrapText(true);
      instructionLabel.setMaxWidth(650);
      instruction.getChildren().addAll(stepLabel, instructionLabel);
      instructionsContainer.getChildren().add(instruction);

      step++;
    }
  }

  /**
   * print the a tags.
   * 
   * @param tag tag name
   */
  public void printTag(String tag) {

    Label label = new Label(tag);
    label.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 18));
    HBox tagHBox = new HBox();
    tagHBox.setPadding(new Insets(3, 5, 3, 5));
    label.setAlignment(Pos.CENTER);
    label.setStyle("-fx-font-style: italic; -fx-font-weight: bold;");
    tagHBox.setStyle("-fx-background-color: #ffacac; -fx-background-radius: 5px; -fx-border-radius: 5px;");
    tagHBox.getChildren().add(label);
    tagContainer.setPadding(new Insets(3, 5, 3, 5));
    tagContainer.setSpacing(10);
    tagContainer.getChildren().add(tagHBox);

  }

  /**
   * Set the name of the displayed recipe.
   *
   * @param name
   */
  public void setName(String name) {
    nameLabel.setText(name);
  }

  /**
   * Set an image .
   *
   * @param path to the image.
   */
  public void setImage(int recipeId) {
    Image mainImage = new Image(getClass().getResource("/recipe_images/" + recipeId + ".jpg").toString());
    heroImage.setImage(mainImage);
  }

   /**
   * Set an image .
   *
   * @param image the image.
   */
  public void setBackUpImage(Image image) {
    backUpImage = image;
  }

  public void setDescription(String desc) {
    txtAreaDesc.setText(desc);
  }

  public void setPortion(int portion) {
    portionText.setText(String.valueOf(portion));
  }

  public void setUser(User u) {
    this.user = u;
  }

  public void setTags(ArrayList<Tag> t) {
    this.tags = t;
  }

  public void loadComment(int recipeId) {
    ArrayList<Comment> comments = dbUtils.getComments(recipeId);
    commentVContainer.getChildren().clear();

    for (var c : comments) {
      boolean notUserComment = false;
      VBox commentbox = null;
      if (user.getUserId() == c.getUserId()) {
        commentbox = setComment(c.getUserName(), c.getRecipeId(), c.getUserId(), c.getComment());
        if (!commentVContainer.getChildren().contains(commentbox)) {
          commentVContainer.getChildren().add(commentbox);
        }
      } else {
        commentbox = setComment(c.getUserName(), c.getRecipeId(), c.getUserId(), c.getComment());
        notUserComment = true;
        if (!commentVContainer.getChildren().contains(commentbox)) {
          commentbox.getChildren().remove(2);
          commentVContainer.getChildren().add(commentbox);
        }
      }
      for (Node n : commentbox.getChildren()) {
        if (n instanceof TextArea) {
          TextArea textarea = (TextArea) n;
          textarea.setEditable(false);
          if (notUserComment == true) {
            textarea.setId("comment-area");
            textarea.getStylesheets().add(getClass().getResource("/commentB.css").toExternalForm());
          }
        } else if (n instanceof HBox) {
          HBox hbox = (HBox) n;
          Node h = hbox.getChildren().get(1);
          if (h instanceof HBox) {
            HBox hbox2 = (HBox) h;
            Node commentbtn = hbox2.getChildren().get(2);
            if (commentbtn instanceof Button) {
              Button cmtBtn = (Button) commentbtn;
              cmtBtn.setDisable(true);
            }
          }
        }
      }
    }
  }

  public void addCommentButton(Event e) {
    VBox comment = setComment(user.getUsername(), recipe.getRecipeId(), user.getUserId(), "");
    if (!commentVContainer.getChildren().contains(comment)) {
      commentVContainer.getChildren().add(0, comment);
    }
  }

  public VBox setComment(String userName, int recipeId, int userId, String text) {
    VBox commentBox = new VBox();
    commentBox.setStyle("-fx-background-radius: 15px;");

    HBox commentHBox = new HBox();
    commentBox.setPrefSize(742, 165);
    commentBox.setPadding(new Insets(3, 10, 3, 10));

    MenuButton menuBtn = new MenuButton("...");
    menuBtn.setPrefWidth(50);
    menuBtn.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
    menuBtn.setCursor(Cursor.HAND);
    MenuItem editBtn = new MenuItem("Edit");
    MenuItem deleteBtn = new MenuItem("Delete");

    menuBtn.getItems().addAll(editBtn, deleteBtn);

    menuBtn.setOnMouseEntered(mouseEvent -> {
      menuBtn.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 15px");
    });

    menuBtn.setOnMouseExited(mouseEvent -> {
      menuBtn.setStyle("-fx-background-color: transparent;");
    });

    TextArea commentArea = new TextArea(text);
    deleteBtn.setOnAction(deleteEvent -> {
      Comment theComment = dbUtils.getOneComment(userId, recipeId, commentArea.getText(), userName);
      if (theComment == null) {
        commentVContainer.getChildren().remove(commentBox);
      } else {
        dbUtils.deleteComment(theComment.getCommentID(), recipeId, userId);
        commentVContainer.getChildren().remove(commentBox);
      }

    });

    Label username = new Label(" " + userName);
    username.setFont(Font.font("Roboto", FontWeight.EXTRA_BOLD, 14));
    Button addCommentBtn = new Button("Comment");
    addCommentBtn.setStyle(
        "-fx-background-color: #FED916; -fx-background-radius: 15px; -fx-text-fill: black; fx-font-size: 14px;");

    addCommentBtn.setOnMouseEntered(mouseEvent -> {
      addCommentBtn.setCursor(Cursor.HAND);
    });

    HBox commentError = new HBox();
    commentError.setAlignment(Pos.CENTER_LEFT);
    commentError.setPrefWidth(400);
    commentError.setPadding(new Insets(0, 5, 0, 5));

    Label counter = new Label("");

    HBox btnHBox = new HBox();
    btnHBox.setAlignment(Pos.CENTER_RIGHT);
    btnHBox.setSpacing(10);
    btnHBox.setPrefWidth(300);
    btnHBox.getChildren().addAll(counter, menuBtn, addCommentBtn);
    btnHBox.setVisible(false);

    commentHBox.setPadding(new Insets(3, 5, 3, 10));
    commentHBox.setAlignment(Pos.CENTER_RIGHT);
    commentHBox.setSpacing(10);
    commentHBox.getChildren().addAll(commentError, btnHBox);

    commentArea.setId("comment-area");
    commentArea.getStylesheets().add(getClass().getResource("/commentA.css").toExternalForm());


    commentArea.setFont(Font.font("Roboto light", FontWeight.LIGHT, 16));
    commentArea.setWrapText(true);
    commentArea.setPrefSize(740, 90);
    commentArea.setMinHeight(90);

    // Get the amount of characters being typed
    commentArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        textTyped(event, commentArea, counter);
      }
    });
    commentBox.setSpacing(3);
    commentBox.getChildren().addAll(username, commentArea, commentHBox);

    commentBox.setOnMouseEntered(mouseEvent -> {
      btnHBox.setVisible(true);
      counter.setVisible(true);
    });

    commentBox.setOnMouseExited(mouseEvent -> {
      counter.setVisible(false);
    });

    addCommentBtn.setOnAction(Event -> {
      stringComment = commentArea.getText();
      Comment theComment = dbUtils.getOneComment(userId, recipeId, tempText, userName);
      String errorMessage = checkComment(stringComment);
      if (errorMessage.equals("")) {
        commentArea.setEditable(false);

        addCommentBtn.setDisable(true);
        commentError.getChildren().clear();
        if (theComment != null) {
          dbUtils.updateComment(theComment.getCommentID(), recipeId, userId, commentArea.getText());
        } else {
          dbUtils.addComment(stringComment, recipeId, userId);
        }
      } else {
        Label errorMsg = new Label(errorMessage);
        errorMsg.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
        errorMsg.setTextFill(Color.RED);
        commentError.getChildren().clear();
        commentError.getChildren().add(errorMsg);
        Toolkit.getDefaultToolkit().beep();
      }
    });

    editBtn.setOnAction(editEvent -> {
      setTempText(commentArea.getText());
      commentArea.setEditable(true);
      addCommentBtn.setDisable(false);
    });

    return commentBox;

  }

  void textTyped(KeyEvent event, TextArea commentArea, Label counter) {
    String commentText = commentArea.getText();
    int numChar = commentText.length();
    counter.setText(numChar + "/330");
  }

  public String checkComment(String comment) {
    if (comment.trim().length() < 1) {
      return "You can't add an empty comment";
    } else if (comment.length() > 330) {
      return "The comment is too long!";
    } else {
      return "";
    }

  }

  /**
   * Gives a list of tags a user is authorized to edit
   *
   * @param tags all tags
   * @return editable tags
   */
  public ArrayList<Tag>  getEditableTags(ArrayList<Tag> tags){
    ArrayList<Tag> editableTags = new ArrayList<>();
    for(var t: tags){
      if(t.getUserID() == user.getUserId()){
        editableTags.add(t);
      }
    }

    return editableTags;
  }


  /**
   * Get the set of permanent tags.
   *
   * @param tags all tags
   * @return permanent tags as string
   */
  public ArrayList<String>  getPermaTags(ArrayList<Tag> tags){
    ArrayList<String> permaTags = new ArrayList<>();
    //System.out.println("user is"+ user.getUserId());
    for(var t: tags){
      if(t.getUserID() != user.getUserId()){
        permaTags.add(t.getName());
      }
    }

    return permaTags;
  }

  /**
   * Get the set of permanent tags..
   *
   * @param tags all tags
   * @return permanent tags as tags
   */
  public ArrayList<Tag>  getPermaTagsAsTags(ArrayList<Tag> tags){
    ArrayList<Tag> permaTags = new ArrayList<>();
    //System.out.println("user is"+ user.getUserId());
    for(var t: tags){
      if(t.getUserID() != user.getUserId()){
        permaTags.add(t);
      }
    }

    return permaTags;
  }

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void setBooleansFromControlller() {
    if(this.mainController != null){
      this.mainController.setBooleans();
    }
    
  }
}

