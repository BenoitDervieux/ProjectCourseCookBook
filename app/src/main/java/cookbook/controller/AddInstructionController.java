package cookbook.controller;


import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.awt.Toolkit;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AddInstructionController {
    @FXML
    private Button addInstructionBtn, saveInstruction;
    @FXML
    private HBox deleteInstruction, checkHBox, instructionHBox;
    @FXML
    private BorderPane instructionBorderPane;
    @FXML
    private TextField instructionTextField;
    @FXML
    private Label counterLBL;
    @FXML
    private VBox instructionVBox;
    @FXML
    private HBox instructionErrorMsgHBox;
    @FXML
    void instructionCounter(KeyEvent event) {
        counterLBL.setText(instructionTextField.getLength() + "/100");
    }


    private ArrayList<String> instructions = new ArrayList<>();

    public void initialize() {

        EventHandler<ActionEvent> addInstructionHandler = event -> {
            String instruction = instructionTextField.getText().trim();
            
            if (instruction.isEmpty()) {
                Label instructionError = new Label("You can't add an empty instruction");
                instructionError.setTextFill(Color.RED);
                instructionError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
                instructionErrorMsgHBox.setAlignment(Pos.CENTER);

                instructionTextField.setStyle("-fx-border-color: red;");
                Toolkit.getDefaultToolkit().beep();

                if (instructionErrorMsgHBox.getChildren().isEmpty()) {
                    instructionErrorMsgHBox.getChildren().add(instructionError);
                } else {
                    instructionErrorMsgHBox.getChildren().set(0, instructionError);
                }
            // Check if the instruction is too long
            } else if (instruction.length() > 100) {
                Label instructionError = new Label("The instruction is too long, please divide it.");
                instructionError.setTextFill(Color.RED);
                instructionError.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
                instructionErrorMsgHBox.setAlignment(Pos.CENTER);

                instructionTextField.setStyle("-fx-border-color: red;");
                Toolkit.getDefaultToolkit().beep();

                if (instructionErrorMsgHBox.getChildren().isEmpty()) {
                    instructionErrorMsgHBox.getChildren().add(instructionError);

                } else {
                    instructionErrorMsgHBox.getChildren().set(0, instructionError);
                }
            

            } else {
                HBox instructionHBox = new HBox();
                Image checkImage = new Image("file:src/main/resources/util/check.png");
                ImageView starImageImageView = new ImageView(checkImage);
                starImageImageView.setFitHeight(14);
                starImageImageView.setFitWidth(14);
                HBox checkHBox = new HBox();
                checkHBox.getChildren().add(starImageImageView);

                checkHBox.setAlignment(Pos.CENTER);

                TextField newInstTf = new TextField(instruction);
                newInstTf.setPrefSize(500, 30);
                newInstTf.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 14));
                newInstTf.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-color: #d9d9d9; -fx-border-width: 1px;");
                Button deleteBtn = new Button("delete");
                deleteBtn.setStyle("-fx-background-color: #e4e4e4; -fx-background-radius: 15px; -fx-text-fill: black; fx-font-size: 14px;");
                deleteBtn.setOnAction(deleteEvent -> {
                    instructionVBox.getChildren().removeAll(checkHBox, instructionHBox);

                });

                instructionHBox.setSpacing(7);
                instructionHBox.setPrefSize(600, 30);

                instructionHBox.setPadding(new Insets(0, 5, 0, 5));
                instructionHBox.getChildren().addAll(checkHBox, newInstTf, deleteBtn);

                instructionVBox.setSpacing(8);
                instructionVBox.setPadding(new Insets(5, 5, 5, 5));
                instructionVBox.getChildren().add(instructionHBox);

                //Make the instructions textField editable with a mouse click!
                newInstTf.setOnAction(editEvent -> {
                    newInstTf.setEditable(false);
                });
                newInstTf.setOnMouseClicked(MouseEvent -> {
                    newInstTf.setEditable(true);
                    newInstTf.requestFocus();
                });

                newInstTf.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        newInstTf.setEditable(false);
                        newInstTf.setFocusTraversable(false);
                    }
                });

                instructionTextField.clear();
                instructionTextField.setStyle("-fx-border-color: #4d90fe;");
                instructionErrorMsgHBox.getChildren().clear();
            }
        };
        addInstructionBtn.setOnAction(addInstructionHandler);
        instructionTextField.setOnAction(addInstructionHandler);

        saveInstruction.setOnAction(event -> {
            //add the savings
            for (Node n : instructionVBox.getChildren()) {
                HBox hbox = (HBox) n;
                for (Node h : hbox.getChildren()) {
                    if (h instanceof TextField) {
                        TextField tf = (TextField) h;
                        instructions.add(tf.getText());
                    }
                }
            }
            instructionVBox.getScene().getWindow().hide();
            
        });
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }


    

 
}


