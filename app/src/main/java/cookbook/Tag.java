package cookbook;

public class Tag { 
    private int tagId;
    private int recipeId;
    private String name;
    private int userID;

    public Tag(int tagId, int recipeId, String name) {
        this.name = name;
        this.recipeId = recipeId;
        this.tagId = tagId;
    }

    public Tag(int tagId,  String name, int userID) {
        this.name = name;
        this.tagId = tagId;
        this.userID = userID;
    }

    public Tag(int tagId, String name) {
        this.name = name;
        this.tagId = tagId;
    }


    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    
}
