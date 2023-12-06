use recipeapp;
SET SQL_SAFE_UPDATES = 0;
#DELETE FROM tags;

INSERT INTO tags (recipe_id, name) VALUES ('1', 'Salad');
INSERT INTO tags (recipe_id, name) VALUES ('1', 'Healthy');

INSERT INTO tags (recipe_id, name) VALUES ('2', 'Meat');
INSERT INTO tags (recipe_id, name) VALUES ('2', 'Vegetables');

INSERT INTO tags (recipe_id, name) VALUES ('3', 'Salad');
INSERT INTO tags (recipe_id, name) VALUES ('3', 'Pasta');

INSERT INTO tags (recipe_id, name) VALUES ('4', 'Italian');
INSERT INTO tags (recipe_id, name) VALUES ('4', 'Baked');

INSERT INTO tags (recipe_id, name) VALUES ('5', 'Baked');
INSERT INTO tags (recipe_id, name) VALUES ('5', 'Fish');

INSERT INTO tags (recipe_id, name) VALUES ('6', 'Chicken');
INSERT INTO tags (recipe_id, name) VALUES ('6', 'Mexican');

INSERT INTO tags (recipe_id, name) VALUES ('7', 'Burger');
INSERT INTO tags (recipe_id, name) VALUES ('7', 'Beef');

INSERT INTO tags (recipe_id, name) VALUES ('8', 'Cheese');
INSERT INTO tags (recipe_id, name) VALUES ('8', 'Pesto');

INSERT INTO tags (recipe_id, name) VALUES ('9', 'Seafood');
INSERT INTO tags (recipe_id, name) VALUES ('9', 'Pasta');

INSERT INTO tags (recipe_id, name) VALUES ('10', 'Vegetables');
INSERT INTO tags (recipe_id, name) VALUES ('10', 'Healthy');

INSERT INTO tags (recipe_id, name) VALUES ('11', 'Beef');
INSERT INTO tags (recipe_id, name) VALUES ('11', 'Cream');

INSERT INTO tags (recipe_id, name) VALUES ('12', 'Chicken');
INSERT INTO tags (recipe_id, name) VALUES ('12', 'Green');

INSERT INTO tags (recipe_id, name) VALUES ('13', 'Pizza');
INSERT INTO tags (recipe_id, name) VALUES ('13', 'Cheese');

INSERT INTO tags (recipe_id, name) VALUES ('14', 'Fish');
INSERT INTO tags (recipe_id, name) VALUES ('14', 'Grilled');

INSERT INTO tags (recipe_id, name) VALUES ('15', 'Chicken');
INSERT INTO tags (recipe_id, name) VALUES ('15', 'Pasta');

INSERT INTO tags (recipe_id, name) VALUES ('16', 'Beef');
INSERT INTO tags (recipe_id, name) VALUES ('16', 'Hearty');

INSERT INTO tags (recipe_id, name) VALUES ('17', 'Vegetables');
INSERT INTO tags (recipe_id, name) VALUES ('17', 'Healthy');

INSERT INTO tags (recipe_id, name) VALUES ('18', 'Pasta');
INSERT INTO tags (recipe_id, name) VALUES ('18', 'Cream');

INSERT INTO tags (recipe_id, name) VALUES ('19', 'Lentil');
INSERT INTO tags (recipe_id, name) VALUES ('19', 'Healthy');

INSERT INTO tags (recipe_id, name) VALUES ('20', 'Pasta');
INSERT INTO tags (recipe_id, name) VALUES ('20', 'Healthy');

INSERT INTO tags (recipe_id, name) VALUES ('21', 'Chicken');
INSERT INTO tags (recipe_id, name) VALUES ('21', 'Cheese');

INSERT INTO tags (recipe_id, name) VALUES ('22', 'Beef');
INSERT INTO tags (recipe_id, name) VALUES ('22', 'Mexican');

INSERT INTO tags (recipe_id, name) VALUES ('23', 'Mushroom');
INSERT INTO tags (recipe_id, name) VALUES ('23', 'Italian');

INSERT INTO tags (recipe_id, name) VALUES ('24', 'Fish');
INSERT INTO tags (recipe_id, name) VALUES ('24', 'Fast Food');

INSERT INTO tags (recipe_id, name) VALUES ('25', 'Salad');
INSERT INTO tags (recipe_id, name) VALUES ('25', 'Healthy');

INSERT INTO tags (recipe_id, name) VALUES ('26', 'Chicken');
INSERT INTO tags (recipe_id, name) VALUES ('26', 'Mexican');

INSERT INTO tags (recipe_id, name) VALUES ('27', 'Pork');
INSERT INTO tags (recipe_id, name) VALUES ('27', 'Rice');

INSERT INTO tags (recipe_id, name) VALUES ('28', 'Beef');
INSERT INTO tags (recipe_id, name) VALUES ('28', 'Vegetables');

INSERT INTO tags (recipe_id, name) VALUES ('29', 'Seafood');
INSERT INTO tags (recipe_id, name) VALUES ('29', 'Italian');

INSERT INTO tags (recipe_id, name) VALUES ('30', 'Healthy');
INSERT INTO tags (recipe_id, name) VALUES ('30', 'Salad');


select * from tags;
