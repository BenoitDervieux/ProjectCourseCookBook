package cookbook;

import java.util.ArrayList;

public class Recipe {
    private int userId;
    private int recipeId;
    private int portion;
    private String name;
    private String summary;
    private String description;
    private ArrayList<String> ingredients;
    private ArrayList<Message> messages;
    private ArrayList<Tag> tags;
    private String image;
    private boolean favourite;
    private double rating;
    
    public Recipe() {

    }
    
    public Recipe(int recipeId, String name, String summary, String description, int portion, double rating) {
        this.recipeId = recipeId;
        this.name = name;
        this.portion = portion;
        this.summary = summary;
        this.description = description;
        this.rating = rating;

    }

    public boolean isFavourite() {
        if (favourite == true) {
            return true;
        } 
        return false;
    }
    
    public void setFavourite(boolean state) {
        this.favourite = state;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public int getPortion() {
        return portion;
    }

    public void setRecipeId(int recipeid) {
        this.recipeId = recipeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getRating() {
        return this.rating;
    }
    
 
}
