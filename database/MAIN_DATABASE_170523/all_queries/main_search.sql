use chanuka1;

SELECT
  r.recipe_id,
  r.name AS recipe_names,
  GROUP_CONCAT(DISTINCT t.name SEPARATOR ', ') AS tag_names,
  GROUP_CONCAT(DISTINCT ing.name SEPARATOR ', ') AS ingredient_names
FROM recipes r
  LEFT JOIN recipe_tags rt ON r.recipe_id = rt.recipe_id
  LEFT JOIN tags t ON rt.tag_id = t.tag_id
  LEFT JOIN recipe_ingredients ri ON r.recipe_id = ri.recipe_id
  LEFT JOIN ingredients ing ON ri.ingredient_id = ing.ingredient_id
GROUP BY r.recipe_id, r.name;
