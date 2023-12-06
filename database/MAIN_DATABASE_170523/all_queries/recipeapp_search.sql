use recipeapp;

SELECT recipes.recipe_id,
GROUP_CONCAT(DISTINCT recipes.name SEPARATOR ', ') AS recipe_names,
GROUP_CONCAT(DISTINCT tags.name SEPARATOR ', ') AS tag_names, 
GROUP_CONCAT(DISTINCT ingredients.name SEPARATOR ', ') AS ingredient_names 
FROM recipeapp.recipes 
JOIN recipeapp.tags ON recipes.recipe_id = tags.recipe_id 
JOIN recipeapp.ingredients ON recipes.recipe_id = ingredients.recipe_id 
GROUP BY recipes.recipe_id;