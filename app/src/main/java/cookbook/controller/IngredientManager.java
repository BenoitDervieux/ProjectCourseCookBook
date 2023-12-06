package cookbook.controller;

import java.util.ArrayList;
import java.util.List;

import cookbook.Recipe;

public class IngredientManager {

    private String name;
    private Recipe recipe;

    public IngredientManager(String name, Recipe recipe) {
        this.name = name;
        this.recipe = recipe; 
    }
    public Recipe getRecipe() {
        return recipe;
    }
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    
    
}
