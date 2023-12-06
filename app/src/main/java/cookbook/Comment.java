package cookbook;

public class Comment {
    private String comment;
    private int recipeId;
    private int userId;
    private int commentID;
    private String userName;

    public Comment(String comment, int recipeId, int userID, int commentID, String userName) {
        this.comment = comment;
        this.recipeId = recipeId;
        this.userId = userID;
        this.commentID = commentID;
        this.userName = userName;
    }

    public void setComment(String com) {
        this.comment = com;
    }

    public String getComment() {
        return comment;
    }

    public void setRecipeId(int recID) {
        this.recipeId = recID;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setCommentID(int comID) {
        this.commentID = comID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
