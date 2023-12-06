package cookbook;

public class User {
    private String username;
    private String email;
    private int userId;
    private String Image;
    private boolean admin;
    public User() {

    }
    
    public User(int userId, String username, String email, int admin) {
        this.username = username;
        this.email = email;
        this.userId = userId;
        if (admin == 1) {
            this.admin = true;
        } else {
            this.admin = false;
        }
        String name = "u" + userId;
        this.Image = "src/main/resources/user_images/" + name + ".jpg";
    }

    public boolean isAdmin() {
        return admin;
    }
    public String getUsername() {
        return username;
    }
    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    
}
