use chanuka1;

SELECT recipes.recipe_id,
GROUP_CONCAT(DISTINCT recipes.name SEPARATOR ', ') AS recipe_names,
tags.tag AS tag
FROM chanuka1.recipes
JOIN chanuka1.recipe_tags ON recipes.recipe_id = recipe_tags.recipe_id
JOIN(SELECT recipe_tags.recipe_id, tags.name AS tag
	FROM chanuka1.recipe_tags JOIN chanuka1.tags ON recipe_tags.tag_id = tags.tag_id)
AS tags ON recipes.recipe_id = tags.recipe_id
GROUP BY recipes.recipe_id, tags.tag;