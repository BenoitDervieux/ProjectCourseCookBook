package cookbook.controller;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cookbook.DBUtils;
import cookbook.Ingredient;
import cookbook.Recipe;
import cookbook.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.shape.Rectangle;
import java.awt.Toolkit;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.imageio.ImageIO;


public class CreateRecipeController {

    private Stage stage;

    @FXML
    private HBox enterInstructionBtn, addTagBtn, enterIngredientBtn, createRecipeErrorMsgHBox;
    @FXML
    private Label addTagLabel, counterLabel, nameLabel;
    @FXML
    private VBox displayIngredientsVbox, displayInstructionVbox, createRecipeVBox;
    @FXML
    private HBox ingredientHbox, instructionHbox, HBoxForTags, portionHBox;
    @FXML
    private ImageView recipeImage;
    @FXML
    private HBox tagContainerHBox;
    @FXML
    private TextField recipeName, portionTextField;
    @FXML
    private Button saveRecipeBtn, editImageBtn;
    @FXML
    private TextArea summaryTextArea;
    @FXML
    private BorderPane createRecipeBorderPane;
    @FXML
    private ScrollPane createRecipeScrollPane;
    @FXML
    private VBox summaryVbox;
    @FXML
    private StackPane imageStackPane;
    @FXML
    void textTyped(KeyEvent event) {
        counterLabel.setText(summaryTextArea.getLength() + "/290");
    }


    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> instructions = new ArrayList<>();
    private String summary;
    private String name;
    private int portion;
    private DBUtils db = DBUtils.getInstance();
    private BufferedImage saveImage;

    private AnchorPane cardAnchor;

    private User user;

    public void setUserData(User user) {
        this.user = user;
    }

    public void setCardAnchor(AnchorPane cardAnchor) {
        this.cardAnchor = cardAnchor;
    }

    private ArrayList<String> ingredients = new ArrayList<>();
    private ArrayList<Ingredient> ingredients2 = new ArrayList<>();

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    
    public void initialize() {

        enterInstructionBtn.setOnMouseClicked(event -> {
            try {
                
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("InstructionCard.fxml"));
                Parent root = loader.load();
                Stage instructionStage = new Stage();
                instructionStage.initModality(Modality.APPLICATION_MODAL);
                instructionStage.initOwner(stage);
                instructionStage.setScene(new Scene(root));
                instructionStage.showAndWait();

                // Get the controller for the TagsCard.fxml file
                AddInstructionController instructionController = loader.getController();
                instructions = instructionController.getInstructions();
                printInstructions(instructions);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        enterIngredientBtn.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("IngredientCard.fxml"));
                Parent root = loader.load();
                Stage ingredientStage = new Stage();
                ingredientStage.initModality(Modality.APPLICATION_MODAL);
                ingredientStage.initOwner(stage);
                ingredientStage.setScene(new Scene(root));
                ingredientStage.showAndWait();

                AddIngredientController ingredientController = loader.getController();
                ingredients = ingredientController.getIngredients();
                ingredients2 = ingredientController.getIngredientObjects();
                printIngedients(ingredients);
            } catch (Exception e) {
                System.out.println("An error has occured: " + e.getMessage()); 
            }
        });

        addTagBtn.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("TagsCard.fxml"));
                Parent root = loader.load();
                Stage tagStage = new Stage();
                tagStage.initModality(Modality.APPLICATION_MODAL);
                tagStage.initOwner(stage);
                tagStage.setScene(new Scene(root));
                tagStage.showAndWait();

                // Get the controller for the TagsCard.fxml file
                AddTagController tagsController = loader.getController();
                tags = tagsController.getTags();
                printTags(tags);
                

            } catch (Exception e) {
                System.out.println("An error has occured: " + e.getMessage());
            }
        });

        editImageBtn.setOnAction(Event -> {
            selectImage();
        });
        
        recipeImage.setOnMouseClicked(Event -> {
            if (!editImageBtn.isVisible()){
                selectImage();
            }  
        });
        
        summaryTextArea.setOnMouseClicked(Event -> {
            summaryTextArea.setEditable(true);
        });
        recipeName.setOnAction(Event -> {
            recipeName.setEditable(false);
            name = recipeName.getText();
        });
        recipeName.setOnMouseClicked(Event -> {
            recipeName.setEditable(true);
        });

        portionTextField.setOnAction(Event -> {
            portion = Integer.parseInt(portionTextField.getText());
        });



        saveRecipeBtn.setOnAction(Event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CreateRecipe.fxml"));
            Parent root;
            try {
                boolean validInputs = validateInput();
                if(validInputs){
                    summary = summaryTextArea.getText();
                    name = recipeName.getText();
                    portion = Integer.parseInt(portionTextField.getText());
                    root = loader.load();
                    Stage stage = (Stage) saveRecipeBtn.getScene().getWindow();
                    int recipe_id = db.addRecipe(name, summary, instructions, ingredients2, tags, portion, user.getUserId());
                    saveImage(saveImage, String.valueOf(recipe_id));

                    //Clear all the fields
                    recipeName.setText("");
                    summaryTextArea.setText("");
                    portionTextField.setText("");
                    counterLabel.setText("");
                    recipeImage.setImage(null);
                    imageStackPane.setStyle("-fx-background-color: #adb5bd; -fx-background-radius: 20px;");
                    editImageBtn.setVisible(true);
                    instructions.clear();
                    ingredients.clear();
                    ingredients2.clear();
                    tags.clear();
                    tagContainerHBox.getChildren().clear();
                    displayInstructionVbox.getChildren().clear();
                    displayIngredientsVbox.getChildren().clear();
                    

                    Label savedLabel = new Label("A new recipe was created successfully");
                    savedLabel.setFont(Font.font("Roboto", FontWeight.EXTRA_BOLD, 24));
                    savedLabel.setTextFill(Color.RED);
                    createRecipeErrorMsgHBox.setAlignment(Pos.CENTER);
                    if (createRecipeErrorMsgHBox.getChildren().isEmpty()) {
                        createRecipeErrorMsgHBox.getChildren().add(savedLabel);
                    } else {
                        createRecipeErrorMsgHBox.getChildren().set(0, savedLabel);
                    }
                    ArrayList<Recipe> recipes = db.getAllRecipes(db.getUser().getUserId());
                    db.setAllRecipes(recipes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        });

     
    }

    public void selectImage() {
        Stage stage = (Stage) editImageBtn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        InputStream input;
        try {
            if (selectedFile != null) {
                input = new FileInputStream(selectedFile);
                Image image = new Image(input);
                recipeImage.setImage(image);
                recipeImage.setFitWidth(imageStackPane.getWidth());
                recipeImage.setFitHeight(imageStackPane.getHeight());

                int height = (int) image.getHeight();
                int width = (int) image.getWidth();
                saveImage = getBufferedImage(selectedFile, height, width);
                editImageBtn.setVisible(false);
                imageStackPane.setStyle("-fx-background-color: transparent");
            }  
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void printTags(ArrayList<String> tags) {
        tagContainerHBox.getChildren().clear();
        tagContainerHBox.setSpacing(7);

        for (String s : tags) {
            Label label = new Label(s);
            label.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
            HBox tagBox = new HBox();
            tagBox.setPadding(new Insets(3, 5, 3, 5));
            label.setAlignment(Pos.CENTER);
            tagBox.setStyle("-fx-background-color: #ffacac; -fx-background-radius: 5px; -fx-border-radius: 5px;");
            tagBox.getChildren().add(label);
            tagContainerHBox.setPadding(new Insets(3, 5, 3, 5));

            tagContainerHBox.getChildren().add(tagBox);
            
        } 
    }
    public void printInstructions(ArrayList<String> instructions) {
        displayInstructionVbox.getChildren().clear();
        displayInstructionVbox.setAlignment(Pos.CENTER_LEFT);
        displayInstructionVbox.setPadding(new Insets(0, 6, 0, 10));
        for (int i = 0; i < instructions.size(); i++) {
            int k = i + 1;
            Label label = new Label(k + ". " + instructions.get(i));
            label.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
            displayInstructionVbox.getChildren().add(label);
        }
    }

    public void printIngedients(ArrayList<String> ingredients) {
        displayIngredientsVbox.getChildren().clear();
        displayIngredientsVbox.setSpacing(10);

        for (String s: ingredients) {
            Label label = new Label(s);
            label.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));

            HBox ingredientHbox = new HBox();
            ingredientHbox.setAlignment(Pos.CENTER_LEFT);
            ingredientHbox.setPadding(new Insets(0, 6, 0, 10));
            ingredientHbox.getChildren().add(label);
            displayIngredientsVbox.setSpacing(10);
            displayIngredientsVbox.setPadding(new Insets(0, 5, 10, 5));
            displayIngredientsVbox.getChildren().add(ingredientHbox);
        }
    }



    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }


    public void setStage(Stage stage) {
        this.stage = stage;


    }

    public BufferedImage getBufferedImage(File file, int height, int width){
        BufferedImage image  = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        try {
            image = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public void saveImage(BufferedImage bImage, String name){
        try {
            ImageIO.write(bImage, "jpg", 
            new File("src/main/resources/recipe_images/" + name +".jpg")); // change 'png' to the 
            System.out.println("saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateInput() {
        String name = recipeName.getText().trim();
        String portion = portionTextField.getText().trim();
        String summary = summaryTextArea.getText().trim();
        Image image = recipeImage.getImage();
        boolean isValid = true;
        
        if (image == null) {
            isValid = false;
            Label recipeError = new Label("You can't add a recipe without an image");
            recipeError.setTextFill(Color.RED);
            recipeError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
            createRecipeErrorMsgHBox.setAlignment(Pos.CENTER);

            imageStackPane.setStyle("-fx-border-color: red;");
            Toolkit.getDefaultToolkit().beep();

            if (createRecipeErrorMsgHBox.getChildren().isEmpty()) {
                createRecipeErrorMsgHBox.getChildren().add(recipeError);
            } else {
                createRecipeErrorMsgHBox.getChildren().set(0, recipeError);
            }

        } else if (name.isEmpty()) {
            isValid = false; 
            Label recipeError = new Label("You can't add a recipe a recipe without a name");
            recipeError.setTextFill(Color.RED);
            recipeError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
            createRecipeErrorMsgHBox.setAlignment(Pos.CENTER);

            recipeName.setStyle("-fx-border-color: red;");
            imageStackPane.setBorder(new Border(new BorderStroke(null, null, null, null)));
            Toolkit.getDefaultToolkit().beep();

            if (createRecipeErrorMsgHBox.getChildren().isEmpty()) {
                createRecipeErrorMsgHBox.getChildren().add(recipeError);
            } else {
                createRecipeErrorMsgHBox.getChildren().set(0, recipeError);
            }
        } else if (summary.isEmpty()) {
            isValid = false; 
            Label recipeError = new Label("You can't add a recipe without a summary");
            recipeError.setTextFill(Color.RED);
            recipeError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
            createRecipeErrorMsgHBox.setAlignment(Pos.CENTER);

            recipeName.setStyle("-fx-background-color: #f1f3f5, #f1f3f5");
            imageStackPane.setBorder(new Border(new BorderStroke(null, null, null, null)));
            summaryTextArea.setStyle("-fx-border-color: red;");
            Toolkit.getDefaultToolkit().beep();

            if (createRecipeErrorMsgHBox.getChildren().isEmpty()) {
                createRecipeErrorMsgHBox.getChildren().add(recipeError);
            } else {
                createRecipeErrorMsgHBox.getChildren().set(0, recipeError);  
            }
            
        } else if (summary.length() > 290) {
            isValid = false; 
            Label recipeError = new Label("The summary is too long, please divide it.");
            recipeError.setTextFill(Color.RED);
            recipeError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
            createRecipeErrorMsgHBox.setAlignment(Pos.CENTER);

            recipeName.setStyle("-fx-background-color: #f1f3f5, #f1f3f5");
            imageStackPane.setBorder(new Border(new BorderStroke(null, null, null, null)));
            summaryTextArea.setStyle("-fx-border-color: red;");
            Toolkit.getDefaultToolkit().beep();

            if (createRecipeErrorMsgHBox.getChildren().isEmpty()) {
                createRecipeErrorMsgHBox.getChildren().add(recipeError);

            } else {
                createRecipeErrorMsgHBox.getChildren().set(0, recipeError);
            }
        } else if (portion.isEmpty() || Integer.valueOf(portion) < 1) {
            isValid = false; 
            Label recipeError = new Label("Invalid entry for portion");
            recipeError.setTextFill(Color.RED);
            recipeError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
            createRecipeErrorMsgHBox.setAlignment(Pos.CENTER);

            recipeName.setStyle("-fx-background-color: #f1f3f5, #f1f3f5");
            summaryTextArea.setStyle("-fx-background-color: #f1f3f5, #f1f3f5");
            
            imageStackPane.setBorder(new Border(new BorderStroke(null, null, null, null)));
            portionTextField.setStyle("-fx-border-color: red;");
            Toolkit.getDefaultToolkit().beep();

            if (createRecipeErrorMsgHBox.getChildren().isEmpty()) {
                createRecipeErrorMsgHBox.getChildren().add(recipeError);
            } else {
                createRecipeErrorMsgHBox.getChildren().set(0, recipeError);
            }
        
        } else if(!isInteger(portion)) {
            isValid = false; 
            Label recipeError = new Label("The portion must be an integer.");
            recipeError.setTextFill(Color.RED);
            recipeError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
            createRecipeErrorMsgHBox.setAlignment(Pos.CENTER);

            recipeName.setStyle("-fx-background-color: #f1f3f5, #f1f3f5");
            summaryTextArea.setStyle("-fx-background-color: #f1f3f5, #f1f3f5");
            
            imageStackPane.setBorder(new Border(new BorderStroke(null, null, null, null)));
            portionTextField.setStyle("-fx-border-color: red;");
            Toolkit.getDefaultToolkit().beep();

            if (createRecipeErrorMsgHBox.getChildren().isEmpty()) {
                createRecipeErrorMsgHBox.getChildren().add(recipeError);
            } else {
                createRecipeErrorMsgHBox.getChildren().set(0, recipeError);
            }

        } else {
            recipeName.setStyle("-fx-background-color: #f1f3f5, #f1f3f5");
            portionTextField.setStyle("-fx-background-color: #f1f3f5, #f1f3f5");
            summaryTextArea.setStyle("-fx-background-color: #f1f3f5, #f1f3f5");
            
            imageStackPane.setBorder(new Border(new BorderStroke(null, null, null, null)));
            createRecipeErrorMsgHBox.getChildren().clear();
        }
        
        return isValid;
    };

   
 
}


