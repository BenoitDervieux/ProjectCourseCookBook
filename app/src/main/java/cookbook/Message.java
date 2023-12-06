package cookbook;

public class Message {
    private String text;
    private int messageId;
    private int userId;
    private int recipeId;

    public Message(int messageId, int recipeId, int userId, String text) {
        this.text = text;
        this.userId = userId;
        this.recipeId = recipeId;
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int msgId) {
        this.messageId = msgId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
