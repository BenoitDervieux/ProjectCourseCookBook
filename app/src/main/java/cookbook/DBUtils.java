package cookbook;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class DBUtils {

    DatabaseConnection databaseConnection = new DatabaseConnection();
    private Connection connection = databaseConnection.getDBConnection();
    private ArrayList<User> users;
    private ArrayList<Recipe> recipes;
    private static volatile DBUtils instance;
    private User user;

    private DBUtils() {
        try {
            users = new ArrayList<>();
            PreparedStatement getUsers = connection.prepareStatement("SELECT * FROM users");
            ResultSet userSet = getUsers.executeQuery();
            while (userSet.next()) {
                User u = new User(userSet.getInt(1), userSet.getString(2), userSet.getString(3), userSet.getInt(5));
                users.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Could not get user !!");
        }

    }

    public static DBUtils getInstance() {
        DBUtils result = instance;
        if (result == null) {
            synchronized (DBUtils.class) {
                if (result == null) {
                    result = new DBUtils();
                    instance = result;
                }
            }

        }

        return result;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setAllRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public User getOneUser(int UserId) {

        try {
            PreparedStatement getUser = connection.prepareStatement("SELECT * FROM users WHERE user_id=?");
            getUser.setInt(1, UserId);
            ResultSet user = getUser.executeQuery();
            User newUser = null;
            while (user.next()) {
                newUser = new User(user.getInt(1), user.getString(2), user.getString(3), user.getInt(5));
            }
            return newUser;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Checks the user login and gives error messages if it is wrong.
     */
    public void checkLogin(String username, String password) {
        try {
            // Hashing the password for better security
            String hashedPassword = getMd5(password);
            PreparedStatement names = connection.prepareStatement("SELECT name, email FROM users");
            ResultSet nameSet = names.executeQuery();
            String validName = "";
            // Checks if user exists.
            while (nameSet.next()) {
                if (nameSet.getString(1).equals(username)) {
                    validName = username;
                } else if (nameSet.getString(2).equals(username)) {
                    validName = username;
                }
            }
            // Checks if users password matches with the username if it is not empty.
            if (!validName.isBlank()) {
                PreparedStatement passwords = connection
                        .prepareStatement("SELECT password FROM users WHERE name=? OR email=?");
                passwords.setString(1, validName);
                passwords.setString(2, validName);
                ResultSet passwordSet = passwords.executeQuery();
                while (passwordSet.next()) {
                    if (!passwordSet.getString(1).equals(hashedPassword)) {
                        throw new IllegalArgumentException("You entered wrong credentials!");
                    }
                }
            } else {
                throw new IllegalArgumentException("You entered wrong credentials!");
            }
        } catch (SQLException e) {
            System.out.println("An error has occurred: " + e.getMessage());
        }
    }

    /**
     * Checks the registration so that it is valid.
     */
    public void checkRegistration(TextField email, TextField username, TextField password) {
        try {
            if (password.getText().length() < 6) {
                throw new IllegalArgumentException("Password needs to be longer than 6 characters!");
            }
            // Hashing password for better security.
            String hashedPassword = getMd5(password.getText());
            PreparedStatement checkEmail = connection.prepareStatement("SELECT email FROM users");
            ResultSet emailSet = checkEmail.executeQuery();
            // Checks if email already exists.
            while (emailSet.next()) {
                if (emailSet.getString(1).equals(email.getText())) {
                    email.setBorder(new Border(
                            new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                                    new BorderWidths(2))));
                    throw new IllegalArgumentException("Email is already registered!");
                }
            }
            // Checks if username already exists
            PreparedStatement checkName = connection.prepareStatement("SELECT name FROM users");
            ResultSet nameSet = checkName.executeQuery();
            while (nameSet.next()) {
                if (nameSet.getString(1).equals(username.getText())) {
                    username.setBorder(new Border(
                            new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                                    new BorderWidths(2))));
                    throw new IllegalArgumentException("username is already taken!");
                }
            }
            // If everything is correct, then add the user to the database.
            PreparedStatement insertUser = connection
                    .prepareStatement("INSERT INTO users(name, email, password) VALUES (?, ?, ?)");
            insertUser.setString(1, username.getText());
            insertUser.setString(2, email.getText());
            insertUser.setString(3, hashedPassword);
            insertUser.executeUpdate();

        } catch (SQLException e) {
            System.out.println("An error has occured: " + e.getMessage());
        }
    }

    /**
     * Password hashing for better security.
     * 
     * @param input a String to be hashed
     * @return the hash of the string.
     */
    public String getMd5(String input) {
        try {

            MessageDigest msgDst = MessageDigest.getInstance("MD5");
            byte[] msgArr = msgDst.digest(input.getBytes());
            BigInteger bi = new BigInteger(1, msgArr);
            String hshtxt = bi.toString(16);
            while (hshtxt.length() < 32) {
                hshtxt = "0" + hshtxt;
            }
            return hshtxt;

        } catch (NoSuchAlgorithmException e) {
            System.out.println("An error has occured: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the logged in user.
     * 
     * @param username is the unique username of the user.
     * @return a user object that is logged in.
     */
    public User getUser(String username) {
        try {
            PreparedStatement user = connection.prepareStatement("SELECT * FROM users WHERE name=?");
            user.setString(1, username);
            ResultSet userSet = user.executeQuery();
            User newUser = new User();
            while (userSet.next()) {
                newUser = new User(userSet.getInt(1), userSet.getString(2), userSet.getString(3), userSet.getInt(5));
            }
            return newUser;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A method that gets all the recipes from the database.
     * 
     * @return an arraylist of recipes
     */
    public ArrayList<Recipe> getAllRecipes(int userID) {
        try {
            ArrayList<Recipe> recipesList = new ArrayList<>();
            // Gets all recipes.
            PreparedStatement recipeDetails = connection.prepareStatement("SELECT * FROM recipes");
            ResultSet rs = recipeDetails.executeQuery();
            while (rs.next()) {
                // gets the tags and messages for the recipes.
                ArrayList<Message> messageList = getRecipeMessages(rs.getInt(1));
                ArrayList<Tag> tagList = getAllTagsByID(rs.getInt(1), userID);
                Recipe recipe = new Recipe(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5), rs.getFloat(6));
                recipe.setUserId(rs.getInt(7));
                recipe.setFavourite(checkIsFavorite(getUser().getUserId(),recipe.getRecipeId()));
                recipe.setMessages(messageList);
                recipe.setTags(tagList);
                recipe.setImage("src/main/resources/images/Pot au feu.jpg");
                recipesList.add(recipe);
            }
            return recipesList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets all recipes that are tagged with only the predefined tag
     * 
     * @param tag the tag used for searching
     * @return the recipe in an arraylist
     */
    public ArrayList<Recipe> getAllTaggedRecipes(String tag) {

        try {

            ArrayList<Recipe> recipesList = new ArrayList<>();
            // Gets all recipes witht the tag.
            PreparedStatement recipeDetails = connection.prepareStatement(
                    "SELECT DISTINCT r.* FROM chanuka1.recipes AS r JOIN chanuka1.recipe_tags AS rt ON r.recipe_id = rt.recipe_id JOIN chanuka1.tags AS t ON rt.tag_id = t.tag_id WHERE t.name LIKE '%"
                            + tag + "%';");
            ResultSet rs = recipeDetails.executeQuery();
            while (rs.next()) {
                // gets the tags and messages for the recipes.
                ArrayList<Message> messageList = getRecipeMessages(rs.getInt(1));
                ArrayList<Tag> tagList = getPredefinedRecipeTags(rs.getInt(1));
                Recipe recipe = new Recipe(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5), rs.getFloat(6));
                recipe.setUserId(rs.getInt(7));
                recipe.setFavourite(checkIsFavorite(getUser().getUserId(),recipe.getRecipeId()));
                recipe.setMessages(messageList);
                recipe.setTags(tagList);
                recipe.setImage("src/main/resources/images/Pot au feu.jpg");
                recipesList.add(recipe);
            }
            return recipesList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<Recipe> getRecipeByIngredient(String ingredient) {

        try {

            ArrayList<Recipe> recipesList = new ArrayList<>();
            // Gets all recipes with the tag.
            PreparedStatement recipeDetails = connection.prepareStatement(
                    "SELECT DISTINCT r.* FROM chanuka1.recipes r JOIN chanuka1.recipe_ingredients ri ON ri.recipe_id = r.recipe_id JOIN chanuka1.ingredients i ON ri.ingredient_id = i.ingredient_id WHERE i.name LIKE '%"
                            + ingredient + "%';");
            ResultSet rs = recipeDetails.executeQuery();
            while (rs.next()) {
                // gets the tags and messages for the recipes.
                ArrayList<Message> messageList = getRecipeMessages(rs.getInt(1));
                ArrayList<Tag> tagList = getPredefinedRecipeTags(rs.getInt(1));
                Recipe recipe = new Recipe(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5), rs.getFloat(6));
                recipe.setUserId(rs.getInt(7));
                recipe.setFavourite(checkIsFavorite(getUser().getUserId(),recipe.getRecipeId()));
                recipe.setMessages(messageList);
                recipe.setTags(tagList);
                recipe.setImage("src/main/resources/images/Pot au feu.jpg");
                recipesList.add(recipe);
            }
            return recipesList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * This method gets one recipe from the database, baes on the recipeid.
     * 
     * @param recipeId is the recipe id of the recipe.
     * @return a recipe
     */
    public Recipe getOneRecipe(int recipeId, int userID) {
        try {
            // Gets one recipe by recipeId
            PreparedStatement recipeDetails = connection.prepareStatement("SELECT * FROM recipes WHERE recipe_id=?");
            recipeDetails.setInt(1, recipeId);
            ResultSet rs = recipeDetails.executeQuery();
            Recipe recipe = new Recipe();
            while (rs.next()) {
                ArrayList<Message> messageList = getRecipeMessages(rs.getInt(1));
                ArrayList<Tag> tagList = getAllTagsByID(rs.getInt(1), userID);
                recipe = new Recipe(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5),
                        rs.getFloat(6));
                recipe.setUserId(rs.getInt(7));
                recipe.setFavourite(checkIsFavorite(getUser().getUserId(),recipe.getRecipeId()));
                // Sets messages and tags to the recipe.
                recipe.setMessages(messageList);
                recipe.setTags(tagList);
                recipe.setImage("src/main/resources/images/Pot au feu.jpg");
                recipe.setImage("src/main/resources/images/Pot au feu.jpg");
            }
            return recipe;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A method that gets all the users recipes from the weekly table in the
     * database.
     * 
     * @return an arraylist of recipes
     */
    public ArrayList<Recipe> getWeeklyRecipes(int weekno, int userId, String day) {
        try {
            ArrayList<Recipe> recipesList = new ArrayList<>();
            // Gets all the weekly recipes based on the week and userId.
            String query = ("SELECT r.* FROM recipes r JOIN ( SELECT dr.recipe_id FROM daily_recipes_of_week"
                    + " drw JOIN weekly w ON drw.weekly_id = w.weekly_id  JOIN daily_recipes dr ON drw.daily_recipe_id"
                    + " = dr.daily_recipe_id WHERE w.user_id = ? AND w.week_no = ? AND dr.day_id = (SELECT day_id FROM"
                    + " days WHERE day_name = ?)) sub ON r.recipe_id = sub.recipe_id;");
            PreparedStatement recipeDetails = connection.prepareStatement(query);
            recipeDetails.setInt(1, userId);
            recipeDetails.setInt(2, weekno);
            recipeDetails.setString(3, day);
            ResultSet rs = recipeDetails.executeQuery();
            while (rs.next()) {
                ArrayList<Message> messageList = getRecipeMessages(rs.getInt(1));
                ArrayList<Tag> tagList = getAllTagsByID(rs.getInt(1), userId);
                Recipe recipe = new Recipe(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5), rs.getFloat(6));
                recipe.setUserId(rs.getInt(7));
                recipe.setFavourite(checkIsFavorite(getUser().getUserId(),recipe.getRecipeId()));
                // Gets and sets the tags and messages for the recipes.
                recipe.setMessages(messageList);
                recipe.setTags(tagList);
                recipesList.add(recipe);
            }
            return recipesList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A method that gets all the users recipes from the favourites table in the
     * database.
     * 
     * @return an arraylist of recipes
     */
    public ArrayList<Recipe> getFavouriteRecipes(int userId) {
        try {
            ArrayList<Recipe> recipesList = new ArrayList<>();
            // Gets all the favourite recipes based on the userId.
            PreparedStatement recipeDetails = connection.prepareStatement(
                    "SELECT * FROM recipes LEFT JOIN favourites ON recipes.recipe_id=favourites.recipe_id WHERE favourites.user_id=?;");
            recipeDetails.setInt(1, userId);
            ResultSet rs = recipeDetails.executeQuery();
            while (rs.next()) {
                ArrayList<Message> messageList = getRecipeMessages(rs.getInt(1));
                ArrayList<Tag> tagList = getAllTagsByID(rs.getInt(1), userId);
                Recipe recipe = new Recipe(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5), rs.getFloat(6));
                recipe.setUserId(rs.getInt(7));
                recipe.setFavourite(checkIsFavorite(getUser().getUserId(),recipe.getRecipeId()));
                // Gets and sets the tags and messages for the recipes.
                recipe.setMessages(messageList);
                recipe.setTags(tagList);
                recipe.setImage("src/main/resources/images/Pot au feu.jpg");
                recipesList.add(recipe);
            }
            return recipesList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A method that gets all the messages for a specific recipe.
     * 
     * @return an arraylist of messages.
     */
    public ArrayList<Message> getRecipeMessages(int recipeId) {
        PreparedStatement recipeDetails;
        try {
            ArrayList<Message> messages = new ArrayList<>();
            // Gets all the messages for the recipe.
            recipeDetails = connection.prepareStatement("SELECT * FROM messages WHERE recipe_id=?");
            recipeDetails.setInt(1, recipeId);
            ResultSet rs = recipeDetails.executeQuery();
            while (rs.next()) {
                // Creates a new message.
                Message message = new Message(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4));
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        }

    }

    /**
     * to check whether a recipe is favorite of a user.
     *
     * @param userID   user's user ID
     * @param recipeId the recipe's recipe_id
     * @return whether favorite or not
     */
    public boolean checkIsFavorite(int userID, int recipeId) {
        PreparedStatement recipeDetails;
        try {
            recipeDetails = connection.prepareStatement(
                    "SELECT EXISTS ( SELECT 1 FROM favourites WHERE user_id = ? AND recipe_id = ?) AS is_favourite;");
            recipeDetails.setInt(1, userID);
            recipeDetails.setInt(2, recipeId);
            ResultSet rs = recipeDetails.executeQuery();
            rs.next();
            int status = rs.getInt(1);

            if (status == 1) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * sets the tags for a user or recipe
     * requires user_id and recipe_id
     */
    public void setTag(int recipe_id, String tagname) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO tags (name) VALUES (?);");
            stmt.setString(1, tagname);
            stmt.executeUpdate();
            PreparedStatement getTagId = connection.prepareStatement("Select tag_id FROM tags WHERE name=?");
            getTagId.setString(1, tagname);
            ResultSet tagId = getTagId.executeQuery();
            int id = 0;
            while (tagId.next()) {
                id = tagId.getInt(1);
            }
            PreparedStatement setRecipeTag = connection
                    .prepareStatement("INSERT INTO recipe_tags(tag_id, recipe_id) VALUES (?, ?)");
            setRecipeTag.setInt(1, id);
            setRecipeTag.setInt(2, recipe_id);
        } catch (Exception e) {
            System.out.println("Error in adding the tag " + tagname + " : " + e.getMessage());
        }
    }

    public ArrayList<Tag> getAllTagsByID(int recipeID, int userID) {
        ArrayList<Tag> tags = new ArrayList<>();
        tags.addAll(getPredefinedRecipeTags(recipeID));
        tags.addAll(getUserRecipeTags(recipeID, userID));

        return tags;
    }

    public ArrayList<Tag> getAllTagsByPhrase(String phrase) {
        ArrayList<Tag> tags = new ArrayList<>();
        tags.addAll(getPredefinedTags(phrase));
        tags.addAll(getUserTags(phrase));

        return tags;
    }

    /**
     * This method is used when a user creates a recipe. Thus, a tag added while creating a recipe 
     * is considered a predefined tag but by a user.
     *
     * @param name name of the tag
     * @param userID Id of the user responsible for adding the tag
     * @return ID of the new tag
     */
    public int addUserPredefinedTag(String name, int userID){
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO tags (name, user_id) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setInt(2, userID);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            
            if ( generatedKeys.next()) {
              int genKey = generatedKeys.getInt(1);
              return genKey;
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }


    /**
     * gets the relevant tag based on the tag id.
     *
     * @param tagID the relevant tag id
     * @return the tag
     */
    public Tag getPredefinedTagByID(int tagID){
        Tag tag;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags where tag_id = ?");
            statement.setInt(1, tagID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                // Creates a new tag.
                tag = new Tag(rs.getInt(1),rs.getString(2), rs.getInt(3));
                // gets only one tag so having it in the loop
                return tag;
            }
           
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }


    /**
     * A method that gets all the tags for a specific recipe.
     * 
     * @return an arraylist of tags.
     */
    public ArrayList<Tag> getPredefinedRecipeTags(int recipeId) {
        PreparedStatement recipeDetails;
        try {
            ArrayList<Tag> tags = new ArrayList<>();
            // Gets all the tags for the recipe.
            recipeDetails = connection.prepareStatement("SELECT tags.tag_id, name, recipe_id, tags.user_id FROM tags LEFT JOIN recipe_tags ON tags.tag_id=recipe_tags.tag_id WHERE recipe_id=?");
            recipeDetails.setInt(1, recipeId);
            ResultSet rs = recipeDetails.executeQuery();
            while (rs.next()) {
                // Creates a new tag.
                Tag tag = new Tag(rs.getInt(1), rs.getInt(3), rs.getString(2));
                tag.setUserID(rs.getInt(4));
                tags.add(tag);
            }
            return tags;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        }
    }

    /**
     * Get's the predefined tags.
     *
     * @param phrase the string phrase to match the tags.
     * @return the matching list of tags.
     */
    public ArrayList<Tag> getPredefinedTags(String phrase) {
        ArrayList<Tag> tags = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT * FROM tags WHERE name Like ? ");
            statement.setString(1, "%" + phrase + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                // these are predefined tags
                tags.add(new Tag(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;
    }

    public ArrayList<Tag> getPredefinedTags() {
        ArrayList<Tag> tags = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT * FROM tags");
            // statement.setString(1,"%"+ phrase + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                // these are predefined tags
                tags.add(new Tag(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;
    }


    public void addPredefinedTagToRecipe(int tagID, int recipeID) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO recipe_tags(tag_id, recipe_id) VALUES (?, ?)");
            statement.setInt(1, tagID);
            statement.setInt(2, recipeID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check whether the tag is predefined.
     *
     * @param name name of the tag.
     * @return whether it is a predefined tag
     */
    public int getPredefinedTagID(String name) {
        PreparedStatement checkifExists;
        try {
            checkifExists = connection.prepareStatement("SELECT tag_id FROM tags WHERE name = ?");
            checkifExists.setString(1, name);
            ResultSet check = checkifExists.executeQuery();
            if (check.next()) {
                return check.getInt(1);
            }
            // Checks if it already exists in the favourite.
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * A method that gets all the tags for a specific recipe.
     * 
     * @return an arraylist of tags.
     */
    public ArrayList<Tag> getUserRecipeTags(int recipeId, int userID) {
        PreparedStatement recipeDetails;
        try {
            ArrayList<Tag> tags = new ArrayList<>();
            // Gets all the tags for the recipe.
            recipeDetails = connection.prepareStatement(
                    "SELECT user_tags.tag_id, user_tags.name, user_recipe_tags.recipe_id FROM user_tags LEFT JOIN user_recipe_tags ON user_tags.tag_id=user_recipe_tags.tag_id WHERE recipe_id=? AND user_tags.user_id = ?");
            recipeDetails.setInt(1, recipeId);
            recipeDetails.setInt(2, userID);
            ResultSet rs = recipeDetails.executeQuery();
            while (rs.next()) {
                // Creates a new tag.
                Tag tag = new Tag(rs.getInt(1), rs.getInt(3), rs.getString(2));
                tag.setUserID(userID);
                tags.add(tag);
            }
            return tags;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        }
    }

    /**
     * Get's the user tags.
     *
     * @param phrase the string phrase to match the tags.
     * @return the matching list of tags.
     */
    public ArrayList<Tag> getUserTags(String phrase) {
        ArrayList<Tag> tags = new ArrayList<>();
        try {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT DISTINCT * FROM user_tags WHERE name Like ? ");
            statement.setString(1, "%" + phrase + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                // these are user tags
                tags.add(new Tag(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;
    }

    /*
     * This will add a custom tag to a recipe.
     */
    public int addUserTagToRecipe(int userID, String tagName, int recipeID) {
        boolean isAvailable = false;
        int tagIndex = -1;
        try {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT tag_id FROM user_tags WHERE user_id = ? AND name = ?");
            statement.setInt(1, userID);
            statement.setString(2, tagName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tagIndex = rs.getInt(1);
                isAvailable = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // add if the tag is not available;
        if (!isAvailable) {
            tagIndex = addUserTag(userID, tagName);
        }

        try {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO user_recipe_tags(tag_id, recipe_id) VALUES (?, ?)");
            statement.setInt(1, tagIndex);
            statement.setInt(2, recipeID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tagIndex;
    }

    /**
     * Add a user created tag.
     * 
     * @param userID specific user.
     * @param name   name of the tag.
     * @return generated id of the tag or returns -1 if there is an issue.
     */
    public int addUserTag(int userID, String name) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO user_tags(user_id, name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userID);
            statement.setString(2, name);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int genKey = generatedKeys.getInt(1);
                return genKey;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ArrayList<Tag> updateRecipeTags(int userID, Recipe recipe, ArrayList<String> newTags){
        ArrayList<Tag> tagsArr = new ArrayList<>();
        try {

            PreparedStatement removeUserTags = connection.prepareStatement("DELETE FROM user_recipe_tags WHERE recipe_id = ? AND tag_id IN ( SELECT tag_id FROM user_tags WHERE user_id = ?)");

            if(userID == recipe.getUserId()){
                PreparedStatement removePredefTags = connection.prepareStatement("DELETE FROM recipe_tags WHERE recipe_id = ? ");

                removePredefTags.setInt(1,recipe.getRecipeId());
                removePredefTags.executeUpdate();

                removeUserTags.setInt(1,recipe.getRecipeId());
                removeUserTags.setInt(2,userID);
                removeUserTags.executeUpdate();
            
                for (String tag : newTags) {
                    int predefinedTagID = getPredefinedTagID(tag);

                    if(predefinedTagID != -1){
                        addPredefinedTagToRecipe(predefinedTagID,recipe.getRecipeId());
                        Tag tg = getPredefinedTagByID(predefinedTagID);
                        System.out.println("pre"+tg.getName());
                        tagsArr.add(tg);
                    } else {  
                        int userTagID = addUserTagToRecipe(userID, tag, recipe.getRecipeId());
                        Tag tg = new Tag(userTagID, tag, userID);
                        System.out.println(tg.getName());
                        tagsArr.add(tg);
                        
                    }   
                }
            } else {
                removeUserTags.setInt(1,recipe.getRecipeId());
                removeUserTags.setInt(2,userID);
                removeUserTags.executeUpdate();
                for (String tag : newTags) {
                   
                    int userTagID = addUserTagToRecipe(userID, tag, recipe.getRecipeId());
                    Tag tg = new Tag(userTagID, tag, userID);
                    System.out.println(tg.getName());
                    tagsArr.add(tg);
                                    
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return tagsArr;
    }

    /**
     * A method that adds a recipe to the favourites table of the user.
     * 
     */
    public void addtoFavourite(Recipe recipe, int userId) {
        try {
            PreparedStatement checkifExists = connection
                    .prepareStatement("SELECT user_id, recipe_id FROM favourites WHERE user_id=?");
            checkifExists.setInt(1, userId);
            ResultSet check = checkifExists.executeQuery();
            // Checks if it already exists in the favourite.
            while (check.next()) {
                if (recipe.getUserId() == check.getInt(1) && recipe.getRecipeId() == check.getInt(2)) {
                    throw new IllegalArgumentException("The recipe is already in your favourites!");
                }
            }
            PreparedStatement addFavourite = connection
                    .prepareStatement("INSERT INTO favourites(user_id, recipe_id) VALUES (?, ?)");
            addFavourite.setInt(1, userId);
            addFavourite.setInt(2, recipe.getRecipeId());
            addFavourite.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that adds a recipe to the weekly table of the user.
     * 
     */
    public void addtoWeekly(Recipe recipe, int userId, int week, String day) {

        try {
            PreparedStatement insertDay = connection.prepareStatement("SELECT day_id FROM days WHERE day_name=?");
            insertDay.setString(1, day);
            int dayId = 0;
            ResultSet days = insertDay.executeQuery();
            while (days.next()) {
                dayId = days.getInt(1);
            }

            PreparedStatement dailyRecipe = connection
                    .prepareStatement("INSERT INTO daily_recipes(day_id, recipe_id, portions) VALUES (?, ?, ?)");
            dailyRecipe.setInt(1, dayId);
            dailyRecipe.setInt(2, recipe.getRecipeId());
            dailyRecipe.setInt(3, recipe.getPortion());
            dailyRecipe.executeUpdate();

            int weekId = getWeekId(userId, week);

            if (weekId == -1) {
                PreparedStatement addWeekly = connection.prepareStatement(
                        "INSERT INTO weekly(user_id, week_no) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                addWeekly.setInt(1, userId);
                addWeekly.setInt(2, week);
                addWeekly.executeUpdate();
                try (ResultSet generatedKeys = addWeekly.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        weekId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating recipe failed, no ID obtained.");
                    }
                }
            }


            PreparedStatement getDailyId = connection.prepareStatement(
                    "SELECT daily_recipe_id FROM daily_recipes WHERE day_id=? AND recipe_id=? AND portions=?");
            getDailyId.setInt(1, dayId);
            getDailyId.setInt(2, recipe.getRecipeId());
            getDailyId.setInt(3, recipe.getPortion());
            int dailyId = 0;
            ResultSet daily = getDailyId.executeQuery();
            while (daily.next()) {
                dailyId = daily.getInt(1);
            }

            PreparedStatement dailyRecipeWeek = connection
                    .prepareStatement("INSERT INTO daily_recipes_of_week(daily_recipe_id, weekly_id) VALUES (?, ?)");
            dailyRecipeWeek.setInt(1, dailyId);
            dailyRecipeWeek.setInt(2, weekId);
            dailyRecipeWeek.executeUpdate();

            ArrayList<Ingredient> ingredients = getIngredients(recipe.getRecipeId());
            // add to shopping card
            for(var i : ingredients){
                addToShopping(userId, weekId, i.getId(), i.getAmount(), week);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the weekly id of a week og user.
     *
     * @param userID Specific user.
     * @param week   the week number.
     * @return week_id or -1 if it is not available
     */
    public int getWeekId(int userID, int week) {
        int weekId = -1;
        try {
            PreparedStatement getWeekId = connection
                    .prepareStatement("SELECT weekly_id FROM weekly WHERE user_id=? AND week_no=?");
            getWeekId.setInt(1, userID);
            getWeekId.setInt(2, week);

            ResultSet weeks = getWeekId.executeQuery();
            while (weeks.next()) {
                weekId = weeks.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weekId;
    }

    /**
     * A method that removes a recipe from the favourites table of the user.
     * 
     */
    public void removeFromFavourite(Recipe recipe, int userId) {
        try {
            PreparedStatement removeFavourite = connection
                    .prepareStatement("DELETE FROM favourites WHERE user_id=? AND recipe_id=?");
            removeFavourite.setInt(1, userId);
            removeFavourite.setInt(2, recipe.getRecipeId());
            removeFavourite.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that removes a recipe from the weekly table of the user.
     * 
     */
    public void removeFromWeekly(Recipe recipe, int userId, int week, String day) {
        try {
            PreparedStatement insertDay = connection.prepareStatement("SELECT day_id FROM days WHERE day_name=?");
            insertDay.setString(1, day);
            int dayId = 0;
            ResultSet days = insertDay.executeQuery();
            while (days.next()) {
                dayId = days.getInt(1);
            }

            PreparedStatement getWeekId = connection
                    .prepareStatement("SELECT weekly_id FROM weekly WHERE user_id=? AND week_no=?");
            getWeekId.setInt(1, userId);
            getWeekId.setInt(2, week);
            int weekId = 0;
            ResultSet weeks = getWeekId.executeQuery();
            while (weeks.next()) {
                weekId = weeks.getInt(1);
            }

            PreparedStatement getDailyId = connection.prepareStatement(
                    "SELECT daily_recipe_id FROM daily_recipes WHERE day_id=? AND recipe_id=? AND portions=?");
            getDailyId.setInt(1, dayId);
            getDailyId.setInt(2, recipe.getRecipeId());
            getDailyId.setInt(3, recipe.getPortion());
            int dailyId = 0;
            ResultSet daily = getDailyId.executeQuery();
            while (daily.next()) {
                dailyId = daily.getInt(1);
            }

            // Delete the record from daily_recipes_of_week first
            PreparedStatement dailyRecipeWeek = connection
                    .prepareStatement("DELETE FROM daily_recipes_of_week WHERE daily_recipe_id=? AND weekly_id=?");
            dailyRecipeWeek.setInt(1, dailyId);
            dailyRecipeWeek.setInt(2, weekId);
            dailyRecipeWeek.executeUpdate();

            // Delete the record from daily_recipes table
            PreparedStatement dailyRecipe = connection
                    .prepareStatement("DELETE FROM daily_recipes WHERE day_id=? AND recipe_id=? AND portions=?");
            dailyRecipe.setInt(1, dayId);
            dailyRecipe.setInt(2, recipe.getRecipeId());
            dailyRecipe.setInt(3, recipe.getPortion());
            dailyRecipe.executeUpdate();

            // Check if the week is empty and delete it if it is
            PreparedStatement checkWeek = connection
                    .prepareStatement("SELECT COUNT(*) FROM daily_recipes_of_week WHERE weekly_id=?");
            checkWeek.setInt(1, weekId);
            ResultSet count = checkWeek.executeQuery();
            count.next();
            int recipeCount = count.getInt(1);
            if (recipeCount == 0) {
                PreparedStatement deleteWeek = connection.prepareStatement("DELETE FROM weekly WHERE weekly_id=?");
                deleteWeek.setInt(1, weekId);
                deleteWeek.executeUpdate();
            }

            // reduce or delete from cart
            if(weekId > 0){
                ArrayList<Ingredient> ing = getIngredients(recipe.getRecipeId());
                for(var i : ing){
                    reduceIngredientAmount(userId, weekId, i.getId(), i.getAmount());
                }
               
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the weeks that the user has created.
     * 
     * @param Userid is the userid of the logged in user.
     * @return an arraylist of integers as week numbers.
     */
    public ArrayList<Integer> getWeeks(int Userid) {
        ArrayList<Integer> weekNumbers = new ArrayList<>();
        try {
            PreparedStatement getWeeks = connection
                    .prepareStatement("SELECT DISTINCT week_no FROM weekly WHERE user_id=? ORDER BY week_no ASC");
            getWeeks.setInt(1, Userid);
            ResultSet weeks = getWeeks.executeQuery();
            while (weeks.next()) {
                weekNumbers.add(weeks.getInt(1));
            }
            return weekNumbers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addRecipe(String name, String description, ArrayList<String> instructions,
            ArrayList<Ingredient> ingredients, ArrayList<String> tags, int portion, int userID) {

        int generatedRecipeID = -1;

        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO recipes (name, description, portion, user_id) VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, portion);
            stmt.setInt(4, userID);
            stmt.executeUpdate();
            // get the generated key
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedRecipeID = generatedKeys.getInt(1);
                    System.out.println("Inserted recipe id: " + generatedRecipeID);
                } else {
                    throw new SQLException("Creating recipe failed, no ID obtained.");
                }
            }

            for (int i = 0; i < instructions.size(); i++) {
                PreparedStatement setInstruction = connection.prepareStatement(
                        "INSERT INTO instructions (recipe_id, step_number, description) VALUES (?, ?, ?)");
                setInstruction.setInt(1, generatedRecipeID);
                setInstruction.setInt(2, i);
                setInstruction.setString(3, instructions.get(i));
                setInstruction.executeUpdate();
            }

            for (Ingredient ing : ingredients) {
                PreparedStatement setIngredient = connection
                        .prepareStatement("INSERT INTO ingredients (name) VALUES (?)");
                setIngredient.setString(1, ing.getName());
                setIngredient.executeUpdate();
                PreparedStatement setUnit = connection.prepareStatement("INSERT INTO units (name) VALUES (?)");
                setUnit.setString(1, ing.getUnit());
                setUnit.executeUpdate();
                PreparedStatement getId = connection.prepareStatement(
                        "SELECT ingredient_id, unit_id FROM ingredients, units WHERE units.name=? AND ingredients.name=?");
                getId.setString(1, ing.getUnit());
                getId.setString(2, ing.getName());
                ResultSet Ids = getId.executeQuery();
                int unitId = 0;
                int ingId = 0;
                while (Ids.next()) {
                    ingId = Ids.getInt(1);
                    unitId = Ids.getInt(2);
                }
                PreparedStatement setAll = connection.prepareStatement(
                        "INSERT INTO recipe_ingredients (ingredient_id, recipe_id, amount, unit_id) VALUES (?, ?, ?, ?)");
                setAll.setInt(1, ingId);
                setAll.setInt(2, generatedRecipeID);
                setAll.setDouble(3, ing.getAmount());
                setAll.setInt(4, unitId);
                setAll.executeUpdate();
            }

            for (String tag : tags) {
                int predefinedTagID = getPredefinedTagID(tag);
                if (predefinedTagID != -1) {
                    addPredefinedTagToRecipe(predefinedTagID, generatedRecipeID);
                } else {
                    // create a new predefined tag for the user
                   int creationID = addUserPredefinedTag(tag, userID);
                   
                   if(creationID != -1){
                       System.out.println("created ID"+creationID+"\nRecipe"+generatedRecipeID);
                       addPredefinedTagToRecipe(creationID,generatedRecipeID);
                   }
                   
                }

            }

        } catch (Exception e) {
            System.out.println("Error in adding the recipe " + name + " : " + e.getMessage());
        }

        return generatedRecipeID;
    }

    /**
     * returns ingredients of a specific recipe.
     *
     * @param recipeID the id of the specific recipe
     * @return array list of ingredients.
     */
    public ArrayList<Ingredient> getIngredients(int recipeID) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String query = ("SELECT i.name AS ingredient_name, ri.amount, u.name AS unit_name, i.ingredient_id FROM recipe_ingredients ri JOIN ingredients i ON ri.ingredient_id = i.ingredient_id LEFT JOIN units u ON ri.unit_id = u.unit_id WHERE ri.recipe_id = ?;");

        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setInt(1, recipeID);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                ingredients.add(new Ingredient(results.getString(1), results.getInt(2), results.getString(3),results.getInt(4)))
                ;
            }

        } catch (SQLException e) {
            System.out.println("An error has occured: " + e.getMessage());
        }

        return ingredients;
    }

    public ArrayList<String> getAllUnits() {
        ArrayList<String> unitList = new ArrayList<>();
        String query = "SELECT distinct name FROM units";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet unitSet = statement.executeQuery();
            while (unitSet.next()) {
                unitList.add(unitSet.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("An error has occured: " + e.getMessage());
        }
        return unitList;

    }

    /**
     * returns the list of instructions of a recipe.
     *
     * @param recipeID id of the specific recipe.
     * @return list of instructions.
     */
    public ArrayList<String> getInstructions(int recipeID) {
        ArrayList<String> instructions = new ArrayList<>();
        String query = ("SELECT instructions.step_number, instructions.description FROM instructions JOIN recipes ON recipes.recipe_id = instructions.recipe_id WHERE recipes.recipe_id = ?;");

        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setInt(1, recipeID);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                instructions.add(results.getString(2));
            }

        } catch (SQLException e) {
            System.out.println("An error has occured: " + e.getMessage());
        }

        return instructions;
    }

    public void addComment(String comment, int recipeId, int userID) {

        try {
            PreparedStatement stmt = connection
                    .prepareStatement("INSERT INTO comments (recipe_id, user_id, comment) VALUES (?, ?, ?);");
            stmt.setInt(1, recipeId);
            stmt.setInt(2, userID);
            stmt.setString(3, comment);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in adding the comment: " + e.getMessage());
        }

    }

    public ArrayList<Comment> getComments(int recipeID) {
        ArrayList<Comment> comments = new ArrayList<>();
        String query = ("SELECT comments.comment, comments.recipe_id, comments.user_id, comments.comment_id, users.name FROM comments JOIN recipes ON recipes.recipe_id = comments.recipe_id JOIN users ON users.user_id = comments.user_id WHERE recipes.recipe_id = ?;");

        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setInt(1, recipeID);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Comment comment = new Comment(results.getString(1), results.getInt(2), results.getInt(3),
                        results.getInt(4), results.getString(5));
                comments.add(comment);

            }

        } catch (SQLException e) {
            System.out.println("An error has occured: " + e.getMessage());
        }

        return comments;
    }

    public void deleteComment(int commentId, int recipeId, int userId) {
        try {
            PreparedStatement dltComment = connection
                    .prepareStatement("DELETE FROM comments WHERE comment_id=? AND recipe_id=? AND user_id = ?");
            dltComment.setInt(1, commentId);
            dltComment.setInt(2, recipeId);
            dltComment.setInt(3, userId);
            dltComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateComment(int commentId, int recipeId, int userId, String text) {
        try {
            PreparedStatement editComment = connection.prepareStatement(
                    "UPDATE comments SET comment = ? WHERE comment_id = ? AND recipe_id = ? AND user_id = ?");
            editComment.setString(1, text);
            editComment.setInt(2, commentId);
            editComment.setInt(3, recipeId);
            editComment.setInt(4, userId);
            editComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Comment getOneComment(int userId, int recipeId, String text, String userName) {
        try {
            PreparedStatement getComment = connection
                    .prepareStatement("SELECT * FROM comments WHERE user_id=? AND recipe_id=? AND comment=?");
            getComment.setInt(1, userId);
            getComment.setInt(2, recipeId);
            getComment.setString(3, text);
            ResultSet comment = getComment.executeQuery();
            Comment theComment = null;
            while (comment.next()) {
                theComment = new Comment(comment.getString(4), comment.getInt(2), comment.getInt(3), comment.getInt(1),
                        userName);
            }
            return theComment;

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // populate shopping table
    public void populateShoppingTable(int weekno, int userId) {
        try {
            //Delete current shopping list for user
            String delQuery = "DELETE FROM chanuka1.shopping WHERE user_id = ? "
                            + "AND weekly_id IN (SELECT weekly_id "
                            + "FROM chanuka1.weekly WHERE user_id = ? )";
            PreparedStatement delStatement = connection.prepareStatement(delQuery);
            delStatement.setInt(1, userId);
            delStatement.setInt(2, userId);
            //delStatement.setInt(3, weekno);
            delStatement.executeUpdate();

            // Insert query into shopping table
            String insertQuery = "INSERT INTO chanuka1.shopping (user_id, weekly_id, week_no, ingredient_id, i_amount) "
                                + "SELECT ?, wi.weekly_id, wi.week_no, ri.ingredient_id, ri.amount "
                                + "FROM chanuka1.recipes r "
                                + "JOIN chanuka1.recipe_ingredients ri ON ri.recipe_id = r.recipe_id "
                                + "JOIN chanuka1.ingredients i ON ri.ingredient_id = i.ingredient_id "
                                + "JOIN chanuka1.daily_recipes dr ON dr.recipe_id = ri.recipe_id "
                                + "JOIN chanuka1.daily_recipes_of_week drow ON drow.daily_recipe_id = dr.daily_recipe_id "
                                + "JOIN chanuka1.weekly wi ON wi.weekly_id = drow.weekly_id "
                                + "JOIN chanuka1.users u ON u.user_id = wi.user_id "
                                + "WHERE u.user_id = ? "; //AND wi.week_no = ?
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, userId);
            insertStatement.setInt(2, userId);
            //insertStatement.setInt(3, weekno);
            insertStatement.executeUpdate();         

        } catch (SQLException e) {
            e.printStackTrace();
           
        }
    }

    // populate shop ingredients table
    public void populateShopIngredients(int userId, int week) {
        try {
            /* // Delete existing entries for the user
            String deleteQuery = "DELETE FROM shop_ingredients WHERE user_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, userId);
            deleteStatement.executeUpdate(); */

            String shopQuery = "INSERT INTO shop_ingredients (ingredient_id, i_amount, user_id, week_no) "
                             + "SELECT ingredient_id, SUM(i_amount), user_id, week_no "
                             + "FROM shopping "
                             + "GROUP BY ingredient_id, user_id, week_no";
            PreparedStatement shopIngredientStatement = connection.prepareStatement(shopQuery);
            //shopIngredientStatement.setInt(1, week);
            shopIngredientStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteShopIngredients(int userId) {
        try {
            // Delete existing entries for the user
            String deleteQuery = "DELETE FROM shop_ingredients WHERE user_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, userId);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Checks if any new recipes in weekly
    public ArrayList<Integer> checkNewRecipes(int weekno, int userId) {
        ArrayList<Integer> newRecipeIds = new ArrayList<>();

        try {
            String query = "SELECT wi.weekly_id " +
                    "FROM chanuka1.weekly wi " +
                    "LEFT JOIN chanuka1.daily_recipes_of_week drow ON wi.weekly_id = drow.weekly_id " +
                    "LEFT JOIN chanuka1.daily_recipes dr ON drow.daily_recipe_id = dr.daily_recipe_id " +
                    "LEFT JOIN chanuka1.recipe_ingredients ri ON dr.recipe_id = ri.recipe_id " +
                    "WHERE wi.user_id = ? AND wi.week_no = ? AND ri.recipe_id IS NOT NULL " +
                    "AND wi.weekly_id NOT IN (" +
                    "SELECT s.weekly_id FROM chanuka1.shopping s WHERE s.user_id = ?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, weekno);
            statement.setInt(3, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int weeklyId = resultSet.getInt("weekly_id");
                newRecipeIds.add(weeklyId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newRecipeIds;
    }

    public void sendRecipe(Recipe recipe, User sender, ArrayList<User> recievers, String message) {

        String query = "INSERT INTO messages(recipe_id, sender_id, receiver_id, message) "
                + "VALUES (?, ?, ?, ?)";
        try {
            for (User user : recievers) {
                PreparedStatement checkMessage = connection.prepareStatement("SELECT * FROM messages WHERE sender_id=? AND receiver_id=? AND message=?");
                checkMessage.setInt(1, sender.getUserId());
                checkMessage.setInt(2, user.getUserId());
                checkMessage.setString(3, message);
                ResultSet doubleCheck = checkMessage.executeQuery();
                Message newMessage = null;
                while (doubleCheck.next()) {
                    newMessage = new Message(doubleCheck.getInt(1), doubleCheck.getInt(2), user.getUserId(), message);
                }
                if (newMessage == null) {
                    PreparedStatement sendRecipes = connection.prepareStatement(query);
                    sendRecipes.setInt(1, recipe.getRecipeId());
                    sendRecipes.setInt(2, sender.getUserId());
                    sendRecipes.setInt(3, user.getUserId());
                    sendRecipes.setString(4, message);
                    sendRecipes.executeUpdate();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<RecipeMessage> getUserMessages(int reciever) {
        try {
            ArrayList<RecipeMessage> messages = new ArrayList<>();
            PreparedStatement recipeMessages = connection
                    .prepareStatement("SELECT * FROM messages WHERE receiver_id=?");
            recipeMessages.setInt(1, reciever);
            ResultSet recipemessage = recipeMessages.executeQuery();

            while (recipemessage.next()) {
                Recipe recipe = getOneRecipe(recipemessage.getInt(2), reciever);
                RecipeMessage message = new RecipeMessage(recipemessage.getInt(3), recipemessage.getInt(4),
                        recipemessage.getString(5), recipe);
                messages.add(message);
            }

            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void removeUserMessage(User receiver, User sender, Recipe recipe, String message) {
        try {
            PreparedStatement removeMessage = connection.prepareStatement(
                    "DELETE FROM messages WHERE receiver_id=? AND sender_id=? AND recipe_id=? AND message=?");
            removeMessage.setInt(1, receiver.getUserId());
            removeMessage.setInt(2, sender.getUserId());
            removeMessage.setInt(3, recipe.getRecipeId());
            removeMessage.setString(4, message);
            removeMessage.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyUser(TextField nameField, TextField emailField, TextField passwordField, String primaryKey) {
        try {
            // If everything is correct, then add the user to the database.
            PreparedStatement updateUser = connection
                    .prepareStatement("UPDATE chanuka1.users SET name = ?, email = ?, password = ? WHERE user_id = ?");
            updateUser.setString(1, nameField.getText());
            updateUser.setString(2, emailField.getText());
            updateUser.setString(3, passwordField.getText());
            updateUser.setString(4, primaryKey);
            updateUser.executeUpdate();

        } catch (Exception e) {
            System.out.println("An error has occured: " + e.getMessage());
        }
    }

    public void modifyUser2(String nameT, String emailT, String passwordT, String primaryKey) {
        try {
            // If everything is correct, then add the user to the database.
            PreparedStatement updateUser = connection
                    .prepareStatement("UPDATE chanuka1.users SET name = ?, email = ?, password = ? WHERE user_id = ?");
            updateUser.setString(1, nameT);
            updateUser.setString(2, emailT);
            updateUser.setString(3, passwordT);
            updateUser.setString(4, primaryKey);
            updateUser.executeUpdate();
        } catch (Exception e) {
            System.out.println("An error has occured: " + e.getMessage());
        }
    }

    public ArrayList<HelpHeading> getHelpData(){
        try {
            ArrayList<HelpHeading> headings = new ArrayList<>();
            PreparedStatement help = connection.prepareStatement("SELECT * FROM helptitle");
            ResultSet helpHeadings = help.executeQuery();
            while (helpHeadings.next()) {
                HelpHeading temp  = new HelpHeading();
                temp.setHeadingId(helpHeadings.getInt(1));
                temp.setHeading(helpHeadings.getString(2));
                headings.add(temp);
            }

            for(var h:  headings){
                ArrayList<HelpInstruction> instructions = getHelpStepsByID(h.getHeadingId());
                h.setSetOfInstructions(instructions);
            }
           
            return headings;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<HelpInstruction> getHelpStepsByID(int id){

        try {
            ArrayList<HelpInstruction> instructionsArr = new ArrayList<>();
            PreparedStatement help = connection.prepareStatement("SELECT * FROM helpdesc WHERE help_id = ?");
            help.setInt(1, id);
            ResultSet instructions = help.executeQuery();
            while (instructions.next()) {
                HelpInstruction temp  = new HelpInstruction();
                temp.setStep(instructions.getString(3));
                temp.setImageID(instructions.getString(4));
                instructionsArr.add(temp);
            }          
            return instructionsArr;
        }   catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

     /**
     * A method that gets all the users recipes from the weekly table in the database.
     * @return an arraylist of recipes
     */
    public ArrayList<String> getWeeklyRecipeOfIng(int weekno, int userId, String ingredientName) {
        try {
            ArrayList<String> recipesList = new ArrayList<>();
            // Gets all the weekly recipes based on the week, userId and ingredient.
            String query = ("SELECT DISTINCT r.name FROM chanuka1.recipes r JOIN chanuka1.recipe_ingredients ri ON ri.recipe_id = r.recipe_id "
                            + "JOIN chanuka1.ingredients i ON ri.ingredient_id = i.ingredient_id "
                            + "JOIN chanuka1.daily_recipes dr ON dr.recipe_id = ri.recipe_id "
                            + "JOIN chanuka1.daily_recipes_of_week drow ON drow.daily_recipe_id = dr.daily_recipe_id "
                            + "JOIN chanuka1.weekly wi ON wi.weekly_id = drow.weekly_id "
                            + "JOIN chanuka1.users u ON u.user_id = wi.user_id "
                            // + "JOIN chanuka1.shopping sc ON ri.ingredient_id = sc.ingredient_id AND
                            // sc.weekly_id = wi.weekly_id "
                            + "WHERE u.user_id = ? AND wi.week_no = ? AND i.name = ?;");
            PreparedStatement recipeDetails = connection.prepareStatement(query);
            recipeDetails.setInt(1, userId);
            recipeDetails.setInt(2, weekno);
            recipeDetails.setString(3, ingredientName);
            ResultSet recIng = recipeDetails.executeQuery();
            while (recIng.next()) {
							String recipeName = recIng.getString("name");
							recipesList.add(recipeName);
					}
            return recipesList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getHelpTitle(String Title) {
        try {
          PreparedStatement title = connection.prepareStatement("SELECT title FROM chanuka1.helptitle WHERE title=?");
          title.setString(1, Title);
          ResultSet userSet = title.executeQuery();
          String theTitle = "";
            while (userSet.next()) {
                theTitle = userSet.getString(1);
            }
          return theTitle;
        } catch (SQLException e) {
          e.printStackTrace();
          return null;
        }

    }
    
    public void setRating(Recipe recipe, int rating, User user) {
        try {
            // check if user has rated yet
            PreparedStatement checkIfRated = connection
                    .prepareStatement("select * from ratings where user_id = ? and recipe_id = ?");
            checkIfRated.setInt(1, user.getUserId());
            checkIfRated.setInt(2, recipe.getRecipeId());
            ResultSet checked = checkIfRated.executeQuery();
            System.out.println();

            // if not rated then add the rating
            if (checked.next()) {
                PreparedStatement addRating = connection
                        .prepareStatement("update ratings set rating = ? where user_id = ? and recipe_id = ?");
                
                addRating.setInt(1, rating);
                addRating.setInt(2, user.getUserId());
                addRating.setInt(3, recipe.getRecipeId());
                addRating.executeUpdate();
            } else {
                // if already rated then delete and add the new rating
                PreparedStatement addRating = connection
                        .prepareStatement("insert into ratings (user_id, recipe_id, rating) values (?, ?, ?)");
                addRating.setInt(1, user.getUserId());
                addRating.setInt(2, recipe.getRecipeId());
                addRating.setInt(3, rating);
                addRating.executeUpdate();
            }

            // update recipe rating on average

            PreparedStatement avgRating = connection
                    .prepareStatement("SELECT AVG(rating) AS ar FROM ratings WHERE recipe_id = ?");
            avgRating.setInt(1, recipe.getRecipeId()); // Set the recipe ID parameter
            ResultSet resultSet = avgRating.executeQuery();

            if (resultSet.next()) {
                int avg = resultSet.getInt("ar");
                PreparedStatement updateRating = connection
                        .prepareStatement("UPDATE recipes SET rating = ? WHERE recipe_id = ?");
                updateRating.setInt(1, avg);
                updateRating.setInt(2, recipe.getRecipeId());
                updateRating.executeUpdate();
            } else {
                System.out.println("An error has occured in setting the rating for the recipe");
            }

        } catch (Exception e) {
            System.out.println("An error has occured in setting the rating for the recipe: " + e.getMessage());
        }
    }

    public void getIngredientsAndAmounts(int userID,int weekNo){
        try {
            String query = """
                SELECT i.name, SUM(ri.amount) AS total_amount
                FROM weekly w
                JOIN users u ON u.user_id = w.user_id
                JOIN daily_recipes_of_week drw ON drw.weekly_id = w.weekly_id
                JOIN daily_recipes dr ON dr.daily_recipe_id = drw.daily_recipe_id
                JOIN recipe_ingredients ri ON ri.recipe_id = dr.recipe_id
                JOIN ingredients i ON i.ingredient_id = ri.ingredient_id
                WHERE w.week_no = ?
                  AND u.user_id = ?
                GROUP BY i.name;
                    """;
            PreparedStatement update = connection.prepareStatement(query);
            update.setInt(1, weekNo);
            update.setInt(2, userID);

            ResultSet ing = update.executeQuery();
              while (ing.next()) {
                 System.out.println("name" + ing.getString(1) + " amount " + ing.getInt(2) );
              }
            
          } catch (SQLException e) {
            e.printStackTrace();
           
          }
  
    }


    /**
     * 
     * Add recipe to shopping cart.
     *
     * @param userId user id
     * @param weeklyId week id
     * @param ingredientId ingredient id
     * @param amount amount
     * @param weekNo week no
     * 
     */
    public void addToShopping(int userId, int weeklyId, int ingredientId, double amount, int weekNo) {
        try {
            // Check if the record already exists
            PreparedStatement checkExisting = connection.prepareStatement(
                    "SELECT i_amount FROM shopping WHERE user_id=? AND weekly_id=? AND ingredient_id=?");
            checkExisting.setInt(1, userId);
            checkExisting.setInt(2, weeklyId);
            checkExisting.setInt(3, ingredientId);
            ResultSet existing = checkExisting.executeQuery();
    
            if (existing.next()) {
                // Update the existing record by adding the new amount to the current amount
                double existingAmount = existing.getDouble("i_amount");
                double updatedAmount = existingAmount + amount;
    
                PreparedStatement updateShopping = connection.prepareStatement(
                        "UPDATE shopping SET i_amount=? WHERE user_id=? AND weekly_id=? AND ingredient_id=?");
                updateShopping.setDouble(1, updatedAmount);
                updateShopping.setInt(2, userId);
                updateShopping.setInt(3, weeklyId);
                updateShopping.setInt(4, ingredientId);
                updateShopping.executeUpdate();
            } else {
                // Insert a new record
                PreparedStatement insertShopping = connection.prepareStatement(
                        "INSERT INTO shopping (user_id, weekly_id, ingredient_id, i_amount, week_no) VALUES (?, ?, ?, ?, ?)");
                insertShopping.setInt(1, userId);
                insertShopping.setInt(2, weeklyId);
                insertShopping.setInt(3, ingredientId);
                insertShopping.setDouble(4, amount);
                insertShopping.setInt(5, weekNo);
                insertShopping.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * get details from cart.
     *
     * @param weekId week id
     * @param userId user id
     */
    public ArrayList<Ingredient> getShopping(int weekId, int userId){
      try{
            ArrayList<Ingredient> ingList = new ArrayList<>();
            // Gets all the weekly recipes based on the week, userId and ingredient.
            String query = ("""
                SELECT i.name, s.i_amount, i.ingredient_id, s.shop_id
                FROM shopping s
                JOIN weekly w ON w.weekly_id = s.weekly_id
                JOIN ingredients i ON i.ingredient_id = s.ingredient_id
                WHERE w.week_no = ? AND s.user_id = ?
                    """);
            PreparedStatement recipeDetails = connection.prepareStatement(query);
            recipeDetails.setInt(1, weekId);
            recipeDetails.setInt(2, userId);
           
            ResultSet recIng = recipeDetails.executeQuery();
            while (recIng.next()) {
                String ingName = recIng.getString(1);
                boolean exists = false;
                for(var i : ingList){
                    if(i.getName().toLowerCase().equals(ingName)){
                        i.setAmount(i.getAmount() + recIng.getDouble(2) );
                        exists = true;
                    }
                }
                if(!exists){
                    Double amount = recIng.getDouble(2);
                    int ingID = recIng.getInt(3);
                    int shopID = recIng.getInt(4);
                    ingList.add(new Ingredient(ingName, amount, ingID,shopID));
                }
                
                    }
            
            return ingList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public void reduceIngredientAmount(int userId, int weeklyId, int ingredientId, Double amountToReduce) {
        try {
            // Check current amount
            PreparedStatement checkAmount = connection.prepareStatement(
                "SELECT i_amount FROM shopping WHERE user_id = ? AND weekly_id = ? AND ingredient_id = ?"
            );
            checkAmount.setInt(1, userId);
            checkAmount.setInt(2, weeklyId);
            checkAmount.setInt(3, ingredientId);
            ResultSet result = checkAmount.executeQuery();
            
            if (result.next()) {
                Double currentAmount = result.getDouble("i_amount");
                Double newAmount = currentAmount - amountToReduce;
                
                if (newAmount > 0) {
                    // Reduce the amount
                    PreparedStatement updateAmount = connection.prepareStatement(
                        "UPDATE shopping SET i_amount = ? WHERE user_id = ? AND weekly_id = ? AND ingredient_id = ?"
                    );
                    updateAmount.setDouble(1, newAmount);
                    updateAmount.setInt(2, userId);
                    updateAmount.setInt(3, weeklyId);
                    updateAmount.setInt(4, ingredientId);
                    updateAmount.executeUpdate();
                } else {
                    // Delete the record
                    PreparedStatement deleteRecord = connection.prepareStatement(
                        "DELETE FROM shopping WHERE user_id = ? AND weekly_id = ? AND ingredient_id = ?"
                    );
                    deleteRecord.setInt(1, userId);
                    deleteRecord.setInt(2, weeklyId);
                    deleteRecord.setInt(3, ingredientId);
                    deleteRecord.executeUpdate();
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
      
    
}
