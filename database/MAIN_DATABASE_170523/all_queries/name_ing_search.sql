
use chanuka1;

SELECT DISTINCT r.name AS rname
FROM chanuka1.recipes AS r 
JOIN chanuka1.recipe_ingredients AS ri ON r.recipe_id = ri.recipe_id 
JOIN chanuka1.ingredients AS ing ON ri.ingredient_id = ing.ingredient_id 
WHERE r.name LIKE '%beef%' OR ing.name LIKE '%beef%';