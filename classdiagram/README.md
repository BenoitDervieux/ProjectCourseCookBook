# The class diagram 

This is a basic draft of the class diagram that will be used to develop the application. 

THe following entities were identified

1. User
2. Admin user
3. Message
4. Favorites
5. Weekly
6. Recipe
7. Ingredient 
9. Shopping list

Other than that initially three more entities was added to control the application: MainApp, BrowsApp and LoginApp. Finally a data base connector was also added to communicate with the data base.

# Relationships 

- Admin is a user 
- User will have a list of weekly meal plans and a of favorite meals 
- The favorites will have a list of recipes 
- Recipe will have a list of ingredients
- A week will have a list of recipes
- Each week will have a shopping list that will help buy the ingredients 
- Shopping list will also have a list of ingredients 
- BrowserApp will hold all the recipes
- MainApp will have the BrowserApp
- MainApp will also hold the LoginApp

