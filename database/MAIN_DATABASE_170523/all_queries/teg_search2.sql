use chanuka1;

SELECT r.name AS rname 
FROM chanuka1.recipes AS r 
JOIN chanuka1.recipe_tags AS rt ON r.recipe_id = rt.recipe_id 
JOIN chanuka1.tags AS t ON rt.tag_id = t.tag_id 
WHERE t.name LIKE '%healt%';