package cookbook;

public class RecipeMessage {
    private int senderId;
    private int recieverId;
    private String message;
    private Recipe recipe;
    
    public RecipeMessage(int senderId, int recieverId, String message, Recipe recipe) {
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.message = message;
        this.recipe = recipe;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(int recieverId) {
        this.recieverId = recieverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    
}
