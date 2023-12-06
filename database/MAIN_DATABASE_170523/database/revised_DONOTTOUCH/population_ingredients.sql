use chanuka1;

-- Ingredients
INSERT INTO ingredients (name) VALUES 
('romaine lettuce'),
('parmesan cheese'),
('croutons'),
('beef'),
('broccoli'),
('red bell pepper'),
('pasta'),
('pesto'),
('cherry tomatoes'),
('flour'),
('yeast'),
('olive oil'),
('chicken'),
('green bell pepper'),
('onion'),
('ground beef'),
('sour cream'),
('cheddar cheese'),
('shrimp'),
('linguine'),
('carrots'),
('spinach'),
('feta cheese'),
('tomato sauce'),
('mozzarella cheese'),
('salmon'),
('lemon'),
('dill'),
('heavy cream'),
('butter'),
('beef tenderloin'),
('button mushrooms'),
('spaghetti'),
('pancetta'),
('lentils'),
('carrot'),
('celery'),
('onion powder'),
('garlic powder'),
('bay leaves'),
('vegetable stock'),
('quinoa');

-- Units
INSERT INTO units (name) VALUES 
('cups'),
('tbsp'),
('tsp'),
('oz'),
('lb'),
('cloves'),
('pieces');

-- Recipe_Ingredients
-- Classic Caesar Salad
INSERT INTO recipe_ingredients (ingredient_id, recipe_id, amount, unit_id) VALUES 
(1, 1, 4, 1),
(2, 1, 0.5, 4),
(3, 1, 1, 7);

-- Beef Stir-Fry with Vegetables
INSERT INTO recipe_ingredients (ingredient_id, recipe_id, amount, unit_id) VALUES 
(4, 2, 1, 5),
(5, 2, 2, 7),
(6, 2, 1, 7);

-- Pesto Pasta Salad
INSERT INTO recipe_ingredients (ingredient_id, recipe_id, amount, unit_id) VALUES 
(7, 3, 8, 1),
(8, 3, 1, 4),
(9, 3, 1, 7);

-- Homemade Pizza
INSERT INTO recipe_ingredients (ingredient_id, recipe_id, amount, unit_id) VALUES 
(10, 4, 2, 1),
(11, 4, 0.25, 4),
(12, 4, 1, 4),
(13, 4, 1, 4),
(25, 4, 2, 7);

-- Baked Salmon with Lemon and Dill
INSERT INTO recipe_ingredients (ingredient_id, recipe_id, amount, unit_id) VALUES 
(26, 5, 1, 1),
(27, 5, 1, 4),
(28, 5, 2, 7);

-- Chicken Fajitas
INSERT INTO recipe_ingredients (ingredient_id, recipe_id, amount, unit_id) VALUES 
(12, 6, 1, 1),
(13, 6, 0.5, 4),
(14, 6, 1, 4),
(15, 6, 1, 4),
(6, 6, 1, 7);

-- Classic Beef Burger
INSERT INTO recipe_ingredients (ingredient_id, recipe_id, amount, unit_id) VALUES 
(16, 7, 1, 1),
(17, 7, 0.5, 4),
(18, 7, 1, 4),
(19, 7, 1, 4);
