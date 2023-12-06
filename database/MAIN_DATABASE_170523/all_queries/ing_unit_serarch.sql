use chanuka1;

SELECT r.name AS rname, ri.amount as amount, u.name AS unit, ing.name AS ingredient
FROM recipes AS r
JOIN recipe_ingredients AS ri ON r.recipe_id = ri.recipe_id
JOIN units AS u ON ri.unit_id = u.unit_id
JOIN ingredients AS ing ON ri.ingredient_id = ing.ingredient_id
WHERE r.name LIKE "%beef%";

