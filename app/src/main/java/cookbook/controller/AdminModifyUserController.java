package cookbook.controller;

import cookbook.DBUtils;
import cookbook.User;
import cookbook.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AdminModifyUserController {

  private final DatabaseConnection db;

  @FXML private TextField displayName;
  
  @FXML private TextField userName;

  @FXML private TextField password;

  @FXML private Button saveChangesButton;

  @FXML private Button deleteUserButton;

  @FXML private ComboBox<User> userComboBox;

  @FXML private TextField searchBar;

  @FXML private ListView<String> listView;

  @FXML
  private ScrollPane paneScroller;

  @FXML
  private VBox theVbox;

  @FXML
  private Label deleteUser;

  @FXML
  private AnchorPane adminAnchor;

  @FXML
  private Label addUser;

  private String userQuery = "";

  private ObservableList<String> users = FXCollections.observableArrayList();
  private List<String[]> listOfUsers = new ArrayList<>();


  /**
   * Contructor that establish the connection to the database
   */
  public AdminModifyUserController() {
    this.db = new DatabaseConnection();
  }

  public void initialize() throws Exception {
    

    // Delayed search mechanism for skip lagg when typing
    // delays query search by set milliseconds
    final Timeline searchTimeline = new Timeline();
    searchTimeline.setCycleCount(1);

      // Switch to the add user panel
      addUser.setOnMouseClicked(e -> {
        try {
            toTheAdd(e);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }   
    });

    
    // Delay search from user input for better user exp, no lagg
    final KeyFrame searchKeyFrame = new KeyFrame(Duration.millis(1), event -> {
      //user search from input
      performSearch();
    });

    searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
      searchTimeline.getKeyFrames().setAll(searchKeyFrame);
      searchTimeline.playFromStart();
    });

    // Load all users without delay if the search bar is empty, inital start
    if (searchBar.getText().isEmpty()) {
      performSearch();
    }

    

  }
  /**
   * serach for a user
   */
  private void performSearch() {
    String input = searchBar.getText().toLowerCase().trim();

    boolean isSearchEmpty = input.isEmpty();
    // if true search all "?", if false search from input
    userQuery = isSearchEmpty
        ? "SELECT name, email, password, user_id FROM chanuka1.users;"
        : "SELECT name, email, password, user_id FROM chanuka1.users WHERE users.name LIKE '%" + input + "%';";

    try {
      Connection connection = db.getDBConnection();
      Statement statement = connection.createStatement();
      ResultSet queryOutput = statement.executeQuery(userQuery);

      users.clear();
      theVbox.getChildren().clear();
      listOfUsers.clear();
      while (queryOutput.next()) {
        String name = queryOutput.getString("name");
        String email = queryOutput.getString("email");
        String password = queryOutput.getString("password");
        String primaryKey = queryOutput.getString("user_id");
        listOfUsers.add(new String[] { name, email, password, primaryKey });
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("CardsDeleteModifyUser.fxml"));
        Parent card = fxmlLoader.load();
        CardsModifyDeleteUserController cardController = fxmlLoader.getController();
        cardController.setInformation(name, email, password, primaryKey);
        theVbox.getChildren().add(card);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException p) {
      p.printStackTrace();
    }
  }

  /**
   * Switch to the add user panel.

   * @param e is the event trigger.
   * @throws IOException
   */
  public void toTheAdd(MouseEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Admin_Panel_Add.fxml"));
    Scene scene = deleteUser.getScene();
    adminAnchor.getChildren().add(root);
  }

}
