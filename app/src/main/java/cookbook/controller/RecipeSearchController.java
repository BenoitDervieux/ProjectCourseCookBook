package cookbook.controller;

import cookbook.model.RecipeSearchModel;
import cookbook.DBUtils;
import cookbook.DatabaseConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecipeSearchController implements Initializable {

  @FXML
  private TableView<RecipeSearchModel> recipeTableView;
  @FXML
  private TableColumn<RecipeSearchModel, Integer> recipeIdTableColumn;
  @FXML
  private TableColumn<RecipeSearchModel, String> recipeNameTableColumn;
  @FXML
  private TableColumn<RecipeSearchModel, String> recipeSummaryTableColumn;
  @FXML
  private TableColumn<RecipeSearchModel, String> recipeDescriptionTableColumn;
  @FXML
  private TextField recipeTextField;

  ObservableList<RecipeSearchModel> recipeSearchModelObservableList = FXCollections.observableArrayList();


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    DatabaseConnection connectNow = new DatabaseConnection();
    Connection connectDB = connectNow.getDBConnection();

    //Sql query execute in the backend
    String productViewQuery = ("SELECT r.recipe_id, r.name AS recipe_names, GROUP_CONCAT(DISTINCT t.name SEPARATOR ', ') AS tag_names, GROUP_CONCAT(DISTINCT ing.name SEPARATOR ', ') AS ingredient_names "
                              +"FROM recipes r LEFT JOIN recipe_tags rt ON r.recipe_id = rt.recipe_id"
                              +"LEFT JOIN tags t ON rt.tag_id = t.tag_id"
                              +"LEFT JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id"
                              +"LEFT JOIN ingredients ing ON ri.ingredient_id = ing.ingredient_id"
                              +"GROUP BY r.recipe_id, r.name;");

                              //("SELECT r.recipe_id, r.name, t.name AS tag_name, i.name AS ingredient_name"
                              //    +"FROM recipes r"
                              //    +"LEFT JOIN tags t ON r.recipe_id = t.recipe_id"
                              //    +"LEFT JOIN ingredients i ON r.recipe_id = i.recipe_id"
                              //    +"WHERE r.name LIKE '%search_query%' OR t.name LIKE '%search_query%' OR i.name LIKE '%search_query%';")*/

                              //("SELECT recipes.recipe_id, recipes.name AS recipe_name, tags.name AS tag_name, ingredients.name AS ingredient_name
                              //+"FROM recipeapp.recipes"
                              //+"JOIN recipeapp.tags ON recipes.recipe_id = tags.recipe_id "
                              //+"JOIN recipeapp.ingredients ON recipes.recipe_id = ingredients.recipe_id;")*/

    try {
      Statement statement = connectDB.createStatement();
      ResultSet queryOutput = statement.executeQuery(productViewQuery);

      while (queryOutput.next()){
        
        Integer queryRecipe_id = queryOutput.getInt("recipe_id");
        String queryName = queryOutput.getString("recipe_names");
        String querySummary = queryOutput.getString("tag_names");
        String queryDescription = queryOutput.getString("ingredient_names");

        //Populate the Observable List
        recipeSearchModelObservableList.add(new RecipeSearchModel(queryRecipe_id, queryName,
            querySummary, queryDescription));
      }
      //Property Value Factory Corresponds to the new RecipeSearchModel fields
      //The table column is the one annotated above
      recipeIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("recipe_id"));
      recipeNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
      recipeSummaryTableColumn.setCellValueFactory(new PropertyValueFactory<>("summary"));
      recipeDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

      recipeTableView.setItems(recipeSearchModelObservableList);

      FilteredList<RecipeSearchModel> filteredData = new FilteredList<>(recipeSearchModelObservableList, b -> true);

      recipeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(recipeSearchModel -> {

          // If no search value then display all the recipes or whatever recipe it currently has, no changes.
          if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
            return true;
          }

          String searchRecipe = newValue.toLowerCase();

          if (recipeSearchModel.getName().toLowerCase().indexOf(searchRecipe) > -1) {
            return true; // Means we found a match in RecipeName
          } else if (recipeSearchModel.getSummary().toLowerCase().indexOf(searchRecipe) > -1){
            return true; // Means ingredient we found a match
          } else if (recipeSearchModel.getDescription().toString().indexOf(searchRecipe) > -1) {
            return true; // to be changed for tags implementation but now for recipe id
          } else
            return false; // add an error message no match found

        });
      });

      SortedList<RecipeSearchModel> sortedData = new SortedList <>(filteredData);

      // Bind sorted result with Table View will be changed later
      sortedData.comparatorProperty().bind(recipeTableView.comparatorProperty());

      // filtered and sorted data table view
      recipeTableView.setItems(sortedData);




    } catch(SQLException e) {
      Logger.getLogger(RecipeSearchController.class.getName()).log(Level.SEVERE, null, e);
      e.printStackTrace();
    }


  }
}