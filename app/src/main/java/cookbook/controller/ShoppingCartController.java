package cookbook.controller;

import cookbook.User;
import cookbook.DBUtils;
import cookbook.DatabaseConnection;
import cookbook.Ingredient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShoppingCartController {

  private final DatabaseConnection db;

  private User user;

  private AnchorPane cardAnchor;

  private DBUtils dbU = DBUtils.getInstance();

  private List<String[]> listIngredients = new ArrayList<>();

  public ShoppingCartController() {
    this.db = new DatabaseConnection();
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setCardAnchor(AnchorPane cardAnchor) {
    this.cardAnchor = cardAnchor;
  }

  @FXML
  private VBox theVbox;
  @FXML
  private Label weekLabel;
  @FXML
  private ChoiceBox<String> weekBox;

  public void initialize() {

    //Get current week and user id for query
    Calendar calendar = Calendar.getInstance();
    int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
    int userID = user.getUserId();

    //Setters for Label and week
    weekLabel.setText(Integer.toString(currentWeek));
    weekBox.setValue("Week " + currentWeek);

    //add weeks to weekbox, where user has recipe
    for (int i: dbU.getWeeks(userID)) {
      String week = "Week " +  i;
      weekBox.getItems().add(week);
    }

    populateDatabase(userID, currentWeek);
    setCards(userID, currentWeek);

    //weekBox show ingredients based on week
    weekBox.setOnAction(event -> {
      String selectedWeek = weekBox.getValue();
      String weekNumber = selectedWeek.substring(selectedWeek.lastIndexOf(" ") + 1);
      int week = Integer.parseInt(weekNumber);

      weekLabel.setText(Integer.toString(week));

      setCards(userID, week);
    });
  }
  
  //Populates database accordingly, checks for new recipes before actions.
  private void populateDatabase(int userID, int week) {
    // If any new recipes added to the weekly, re-add ingredients and amount
    ArrayList<Integer> newRecipeIds = dbU.checkNewRecipes(week, userID);
    if (!newRecipeIds.isEmpty()) {
      System.out.println(newRecipeIds);
      
      dbU.populateShoppingTable(week, userID);
      dbU.deleteShopIngredients(userID);
      dbU.populateShopIngredients(userID, week);
    } else {
      //dbU.deleteShopIngredients(userID);
      //dbU.populateShopIngredients(userID, week);
    }
    setCards(userID, week);
  }

  // sets the ingredient cards with info from database
  private void setCards(int userID, int week) {
    // Clear the existing cards
    theVbox.getChildren().clear();

    if (user != null) {
     
     
      try {
        ArrayList<Ingredient> ing = dbU.getShopping(week, userID);
        for(var i : ing){
          
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("BuyingCartCard.fxml"));
        Parent card = loader.load();
        CartIngredientsController cardControl = loader.getController();
        cardControl.setInfo(i.getName(), i.getId(), i.getAmount()+"", userID, i.getShopID());
        theVbox.getChildren().add(card);
        }

      } catch (IOException e) {
        e.printStackTrace();
      }

    } else {
      System.out.println("User is null");
    }
  }

}
