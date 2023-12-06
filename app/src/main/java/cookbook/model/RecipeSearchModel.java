package cookbook.model;

public class RecipeSearchModel {
  Integer recipe_id;
  String name;
  String summary;
  String description;

  public RecipeSearchModel(Integer recipe_id, String name, String summary, String description) {
    this.recipe_id = recipe_id;
    this.name = name;
    this.summary = summary;
    this.description = description;
  }

  public Integer getRecipe_id() {
    return recipe_id;
  }

  public String getName() {
    return name;
  }

  public String getSummary() {
    return summary;
  }

  public String getDescription() {
    return description;
  }

  public void setRecipe_id(Integer recipe_id) {
    this.recipe_id = recipe_id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
