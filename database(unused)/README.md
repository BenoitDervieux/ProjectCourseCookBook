# The main entities for the database by Amol: (the next person can make changes to this) :)

The main entities:

1. Users
2. Favourites
3. Messages
4. Weekly List
5. Shopping List
6. Recipe
7. Tags
8. Ingredients


# Assumptions (11apr):

- All entities exist independently, and all have a unique ID along with the user ID that points to them so as to make an easier system to do queries (using join on user ID for example, as it is the main top down use of the app). This should be changed as some entities need to work with each other such as ingredients and shopping list. 

- EER is not a complete design (but i can upload it if you want to see) at all as i am unsure how it may reflect the class design so i did not want to waste time on creating one that will be scrapped anyway

- It does not entirely follow the class diagram as i assumed different instances of the classes may be stored within the parent object - i.e, weekly list holds the starred recipe ID etc, whose efficacy will be more evident as we start to programme the main file

- There is no favourites enitity as it can just be treated as a tag in the back end

# Assumptions (13apr): The previous design has been overwritten

- The only entities that hold information so far are users, messages and recipes. The rest are so far only pointing to the information, additional columns to hold information can be added later

- favourites and weekly are the same thing, except weekly contributes to the shopping entity