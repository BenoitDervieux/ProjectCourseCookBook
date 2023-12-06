package cookbook.controller;

import java.util.ArrayList;


import cookbook.DBUtils;
import cookbook.Ingredient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import java.awt.Toolkit;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;

public class AddIngredientController {
    
    @FXML
    private Button addIngredientBtn, saveIngredientBtn;
    @FXML
    private BorderPane ingredientBorderPane;
    @FXML
    private ChoiceBox<String> ingredientUnit;
    @FXML
    private TextField ingredientAmount, ingredientTextField;
    @FXML
    private HBox ingredientHBox, ingredientErrorMsgHBox;
    @FXML
    private VBox ingredientVBox;

    DBUtils db = DBUtils.getInstance();


    private ArrayList<String> ingredients = new ArrayList<>();
    private ArrayList<Ingredient> ingredients2 = new ArrayList<>();
    
    
    public void initialize() {

        ArrayList<String> unitList = new ArrayList<>();
        unitList.clear();
        unitList = db.getAllUnits();

        ingredientUnit.getItems().addAll(unitList);

        ingredientUnit.setConverter(new StringConverter<String>() {
            // Set a title when nothing is selected yet.
            @Override
            public String toString(String object) {
                return object == null ? "Select Unit" : object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
            
        });

        

        EventHandler<ActionEvent> addIngredientHandler = event -> {

            String ingredient = ingredientTextField.getText().trim();
            String amount = ingredientAmount.getText().trim();
            String unit = ingredientUnit.getValue();

            if (ingredient.isEmpty()) {
                Label ingredientError = new Label("You can't add an empty ingredient");
                ingredientError.setTextFill(Color.RED);
                ingredientError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
                ingredientErrorMsgHBox.setAlignment(Pos.CENTER);

                ingredientTextField.setStyle("-fx-border-color: red;");
                Toolkit.getDefaultToolkit().beep();

                if (ingredientErrorMsgHBox.getChildren().isEmpty()) {
                    ingredientErrorMsgHBox.getChildren().add(ingredientError);
                } else {
                    ingredientErrorMsgHBox.getChildren().set(0, ingredientError);
                }
            
            } else if (amount.isEmpty()) {
                Label ingredientError = new Label("You can't add an ingredient without an amount");
                ingredientError.setTextFill(Color.RED);
                ingredientError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
                ingredientErrorMsgHBox.setAlignment(Pos.CENTER);

                ingredientAmount.setStyle("-fx-border-color: red;");
                Toolkit.getDefaultToolkit().beep();

                if (ingredientErrorMsgHBox.getChildren().isEmpty()) {
                    ingredientErrorMsgHBox.getChildren().add(ingredientError);
                } else {
                    ingredientErrorMsgHBox.getChildren().set(0, ingredientError);
                }
            
            } else if(!isInteger(amount)) {
                Label ingredientError = new Label("The Amount must be an integer.");
                ingredientError.setTextFill(Color.RED);
                ingredientError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
                ingredientErrorMsgHBox.setAlignment(Pos.CENTER);

                ingredientAmount.setStyle("-fx-border-color: red;");
                Toolkit.getDefaultToolkit().beep();

                if (ingredientErrorMsgHBox.getChildren().isEmpty()) {
                    ingredientErrorMsgHBox.getChildren().add(ingredientError);
                } else {
                    ingredientErrorMsgHBox.getChildren().set(0, ingredientError);
                }

            } else if (unit == null) {
                Label ingredientError = new Label("You must select a unit");
                ingredientError.setTextFill(Color.RED);
                ingredientError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
                ingredientErrorMsgHBox.setAlignment(Pos.CENTER);

                ingredientUnit.setStyle("-fx-border-color: red;");
                Toolkit.getDefaultToolkit().beep();

                if (ingredientErrorMsgHBox.getChildren().isEmpty()) {
                    ingredientErrorMsgHBox.getChildren().add(ingredientError);
                } else {
                    ingredientErrorMsgHBox.getChildren().set(0, ingredientError);  
                } 

            } else {
                HBox ingredientHBox = new HBox();
                Image checkImage = new Image("file:src/main/resources/util/check.png");
                ImageView starImageImageView = new ImageView(checkImage);
                starImageImageView.setFitHeight(14);
                starImageImageView.setFitWidth(14);
                HBox checkHBox = new HBox();
                checkHBox.setPrefSize(15, 15);
                checkHBox.getChildren().add(starImageImageView);

                

                Label ingredientLabel = new Label(amount + " " + unit + " " + ingredient);
                Ingredient newIngredient = new Ingredient(ingredient, Double.parseDouble(amount), unit);
                ingredients2.add(newIngredient);
                ingredientLabel.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));

                Button deleteIngredientBtn = new Button("delete");
                deleteIngredientBtn.setStyle("-fx-background-color: #e4e4e4; -fx-background-radius: 15px; -fx-text-fill: black; fx-font-size: 14px;");
                deleteIngredientBtn.setOnAction(deleteEvent -> {
                    ingredientVBox.getChildren().remove(ingredientHBox);
                    ingredientErrorMsgHBox.getChildren().clear();
                    ingredientTextField.setStyle("-fx-border-color: #4d90fe;");
                });

                ingredientHBox.setAlignment(Pos.CENTER_LEFT);
                checkHBox.setAlignment(Pos.CENTER_LEFT);
                ingredientLabel.setAlignment(Pos.CENTER_LEFT);

                HBox checkAndLabelHBox = new HBox();
                checkAndLabelHBox.setAlignment(Pos.CENTER_LEFT);
                checkAndLabelHBox.getChildren().addAll(checkHBox, ingredientLabel);
                checkAndLabelHBox.setSpacing(5);
                
                HBox.setHgrow(checkAndLabelHBox, Priority.ALWAYS);
                
                ingredientHBox.getChildren().addAll(checkAndLabelHBox, deleteIngredientBtn);
                deleteIngredientBtn.setAlignment(Pos.CENTER_RIGHT);
                HBox.setHgrow(deleteIngredientBtn, Priority.NEVER);

                ingredientHBox.setSpacing(5);
                ingredientHBox.setPadding(new Insets(0, 30, 0, 10));
                ingredientHBox.setPrefSize(410, 30);

                ingredientVBox.getChildren().add(ingredientHBox);

                ingredientVBox.setSpacing(5);

                ingredientTextField.clear();
                ingredientTextField.setStyle("-fx-border-color: #4d90fe;");

                ingredientAmount.clear();
                ingredientAmount.setStyle("-fx-border-color: #4d90fe;");

                ingredientUnit.getSelectionModel().clearSelection();
                ingredientUnit.setStyle("-fx-border-color: #4d90fe;");

                ingredientErrorMsgHBox.getChildren().clear();
                ingredientAmount.requestFocus();


            }

        };

        addIngredientBtn.setOnAction(addIngredientHandler);
        
        ingredientTextField.setOnAction(addIngredientHandler);

        saveIngredientBtn.setOnAction(event -> {
            for (Node node : ingredientVBox.getChildren()) {
                if (node instanceof HBox) {
                    HBox ingredientHBox = (HBox) node;
                    for (Node h: ingredientHBox.getChildren()) {
                        if (h instanceof HBox) {
                            HBox hbox = (HBox) h;
                            Node checkAndLabelHBox = hbox.getChildren().get(0);
                            if (checkAndLabelHBox instanceof HBox) {
                                Node ingredientLabel = hbox.getChildren().get(1);
                                String label = ((Label) ingredientLabel).getText();
                                ingredients.add(label);
                            }
                        }
                        
                    }
                }
            }
            ingredientVBox.getScene().getWindow().hide();
        });

        ingredientAmount.setOnAction(event -> {
            ingredientUnit.show();
            ingredientUnit.requestFocus();
        });

        ingredientUnit.setOnAction(event -> {
            ingredientTextField.requestFocus();
        });
        

        ingredientAmount.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                ingredientUnit.requestFocus();
                event.consume();
            }
        });
    


    }
    
    public ArrayList<Ingredient> getIngredientObjects() {
        return ingredients2;
    }

    private boolean isInteger(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }



}

