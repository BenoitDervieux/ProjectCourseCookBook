package cookbook.controller;

import java.io.IOException;
import java.util.ArrayList;

import cookbook.DBUtils;
import cookbook.Tag;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import java.awt.Toolkit;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Window;
import javafx.stage.Stage;

public class AddTagController {
    @FXML
    private Button addTagButton, saveTagBtn;
    @FXML
    private HBox deleteTag, tagHBox, errorMsgHBox;
    @FXML
    private BorderPane tagBorderPane;
    @FXML
    private TextField tagName;
    @FXML
    private VBox tagVBox;
    @FXML
    private Label tagLabel;
    @FXML
    private HBox tagLabelHbox;

    //CreateRecipeController crc = new CreateRecipeController();


    private ArrayList<String> tags = new ArrayList<>();
    private DBUtils dbUtils = DBUtils.getInstance();
		private ContextMenu dropdownMenu = new ContextMenu();
    private boolean tagsUpdated = false;

    public void initialize() {

        
        addTagButton.setOnAction(e -> addTagHandler());;
        tagName.setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.ENTER) {
						addTagHandler();
					}            
			});

        saveTagBtn.setOnAction(event -> {
            for (Node node : tagVBox.getChildren()) {
                if (node instanceof HBox) {
                    HBox tagHBox = (HBox) node;
                    for (Node h : tagHBox.getChildren()) {
                        if (h instanceof HBox) {
                            HBox hb = (HBox) h;
                            Label tagLabel = (Label) hb.getChildren().get(0);
                            tags.add(tagLabel.getText());
                        }
                        
                    }
                    
                }
            }
						tagsUpdated = true;
            tagVBox.getScene().getWindow().hide();
        });

				tagName.setContextMenu(dropdownMenu);

        tagName.textProperty().addListener((observable, oldValue, newValue) -> {
          if(!newValue.trim().isEmpty()){
						ArrayList<Tag> tags = dbUtils.getAllTagsByPhrase(newValue);
						dropdownMenu.getItems().clear();
						
            for(var t: tags){
            	MenuItem menuItem = new MenuItem(t.getName());
              menuItem.setOnAction(e -> {
              // Set the selected tag name as the text of the TextField
              tagName.setText(menuItem.getText());
                    
              // Close the dropdown menu
              dropdownMenu.hide();
            	});
            	dropdownMenu.getItems().add(menuItem);
            }

						 // Show the dropdown menu below the TextField

					  Window window = tagName.getScene().getWindow();
						Bounds bounds = tagName.getBoundsInLocal();
						Bounds screenBounds = tagName.localToScreen(bounds);
		 
						double anchorX = screenBounds.getMinX();
						double anchorY = screenBounds.getMaxY();

						dropdownMenu.setMinWidth(tagName.getWidth());
	  				dropdownMenu.show(window, anchorX, anchorY);
					}
            
        });

			tagName.focusedProperty().addListener((observable, oldValue, newValue) -> {
					if (!newValue) {
							dropdownMenu.hide();
					}
			});
        

    }

		public void addTagHandler() {
			String tagNameText = tagName.getText().trim();

			// Sets the first error message when the user want to add an empty textfield.
			if (tagNameText.isEmpty()) {
					Label errorMsg = new Label("You can't add an empty tag");
					errorMsg.setTextFill(Color.RED);
					errorMsg.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 14));
					errorMsgHBox.setAlignment(Pos.CENTER);

					tagName.setStyle("-fx-border-color: red;");
					Toolkit.getDefaultToolkit().beep();
					
					if (errorMsgHBox.getChildren().isEmpty()) {
							errorMsgHBox.getChildren().add(errorMsg);
					} else {
							errorMsgHBox.getChildren().set(0, errorMsg);
					}

			//sets the error message when the user what to add more than 5 tags to a recipe.
			} else if (tagVBox.getChildren().size() >= 10) {
					Label errorMsg = new Label("You can only add 10 tags to a recipe");
					errorMsg.setTextFill(Color.RED);
					errorMsg.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
					errorMsgHBox.setAlignment(Pos.CENTER);

					tagName.setStyle("-fx-border-color: red;");
					Toolkit.getDefaultToolkit().beep();
					
					if (errorMsgHBox.getChildren().isEmpty()) {
							errorMsgHBox.getChildren().add(errorMsg);
					} else {
							errorMsgHBox.getChildren().set(0, errorMsg);
					}

			} else {
				
				printTag(tagNameText);
				errorMsgHBox.getChildren().clear();
			}

		

	};

	public void printTag(String tagNameText){
			// Add the tags to the Vbox
			Label tagLabel = new Label(tagNameText);
					
			tagLabel.setFont(Font.font("Roboto Light", FontWeight.LIGHT, 16));
			HBox tagLabelHbox = new HBox();

			Button deleteTagBtn = new Button();
			deleteTagBtn.setStyle("-fx-background-color: #e4e4e4; -fx-background-radius: 15px; -fx-text-fill: black; fx-font-size: 14px;");
			deleteTagBtn.setText("delete");
			deleteTagBtn.setOnAction(deleteEvent -> {
					tagVBox.getChildren().remove(tagLabelHbox);
					errorMsgHBox.getChildren().clear();
					tagName.setStyle("-fx-border-color: #4d90fe;");
			});

			//Add the tag and the delete button to the tagLabelHBox
			HBox tagHBox = new HBox();
			tagLabel.setAlignment(Pos.CENTER);
			tagHBox.getChildren().add(tagLabel);
			tagHBox.setStyle("-fx-background-color: #ffacac; -fx-background-radius: 5px; -fx-border-radius: 5px;");
			HBox.setHgrow(tagHBox, Priority.ALWAYS);
			HBox.setHgrow(deleteTagBtn, Priority.NEVER);
			tagLabelHbox.getChildren().addAll(tagHBox, deleteTagBtn);

			tagLabelHbox.setSpacing(10);
			tagLabelHbox.setPadding(new Insets(0, 10, 0, 15));
			tagLabelHbox.setPrefSize(140, 25);
			tagHBox.setAlignment(Pos.CENTER);
			deleteTagBtn.setAlignment(Pos.CENTER_RIGHT);

			tagVBox.getChildren().add(tagVBox.getChildren().size(), tagLabelHbox);
	
			tagVBox.setSpacing(8);
			tagName.clear();
			tagName.setStyle("-fx-border-color: #4d90fe;");

	}
	
	public ArrayList<String> getTags () {
		return tags;
	}

	public boolean getTagsUpdated(){
		return tagsUpdated;
	}

	public void setExistingTags(ArrayList<Tag> tagsArr){
		for(var t : tagsArr){
			printTag(t.getName());
		}
	}
	
 
}

