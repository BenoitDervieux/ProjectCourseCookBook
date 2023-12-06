use chanuka1;
SET SQL_SAFE_UPDATES = 0;
DELETE FROM tags;
DELETE FROM recipe_tags;


INSERT INTO tags (name) VALUES ('Salad');
INSERT INTO tags (name) VALUES ('Healthy');
INSERT INTO recipe_tags VALUES ('1', '1'); 
INSERT INTO recipe_tags VALUES ('2', '1');


INSERT INTO tags (name) VALUES ('Meat');
INSERT INTO tags (name) VALUES ('Vegetables');
INSERT INTO recipe_tags VALUES ('3', '2'); 
INSERT INTO recipe_tags VALUES ('4', '2'); 


INSERT INTO tags (name) VALUES ('Salad');
INSERT INTO tags (name) VALUES ('Pasta');
INSERT INTO recipe_tags VALUES ('5', '3'); 
INSERT INTO recipe_tags VALUES ('6', '3');


INSERT INTO tags (name) VALUES ('Italian');
INSERT INTO tags (name) VALUES ('Baked');
INSERT INTO recipe_tags VALUES ('7', '4'); 
INSERT INTO recipe_tags VALUES ('8', '4');


INSERT INTO tags (name) VALUES ('Baked');
INSERT INTO tags (name) VALUES ('Fish');
INSERT INTO recipe_tags VALUES ('9', '5'); 
INSERT INTO recipe_tags VALUES ('10', '5'); 

 
INSERT INTO tags (name) VALUES ('Chicken');
INSERT INTO tags (name) VALUES ('Mexican');
INSERT INTO recipe_tags VALUES ('11', '6'); 
INSERT INTO recipe_tags VALUES ('12', '6'); 


INSERT INTO tags (name) VALUES ('Burger');
INSERT INTO tags (name) VALUES ('Beef');
INSERT INTO recipe_tags VALUES ('13', '7'); 
INSERT INTO recipe_tags VALUES ('14', '7');  


INSERT INTO tags (name) VALUES ('Cheese');
INSERT INTO tags (name) VALUES ('Pesto');
INSERT INTO recipe_tags VALUES ('15', '8'); 
INSERT INTO recipe_tags VALUES ('16', '8');  


INSERT INTO tags (name) VALUES ('Seafood');
INSERT INTO tags (name) VALUES ('Pasta');
INSERT INTO recipe_tags VALUES ('17', '9'); 
INSERT INTO recipe_tags VALUES ('18', '9'); 


INSERT INTO tags (name) VALUES ('Vegetables');
INSERT INTO tags (name) VALUES ('Healthy');
INSERT INTO recipe_tags VALUES ('19', '10'); 
INSERT INTO recipe_tags VALUES ('20', '10'); 


INSERT INTO tags (name) VALUES ('Beef');
INSERT INTO tags (name) VALUES ('Cream');
INSERT INTO recipe_tags VALUES ('21', '11'); 
INSERT INTO recipe_tags VALUES ('22', '11'); 


INSERT INTO tags (name) VALUES ('Chicken');
INSERT INTO tags (name) VALUES ('Green');
INSERT INTO recipe_tags VALUES ('23', '12'); 
INSERT INTO recipe_tags VALUES ('24', '12'); 


INSERT INTO tags (name) VALUES ('Pizza');
INSERT INTO tags (name) VALUES ('Cheese');
INSERT INTO recipe_tags VALUES ('25', '13'); 
INSERT INTO recipe_tags VALUES ('26', '13'); 


INSERT INTO tags (name) VALUES ('Fish');
INSERT INTO tags (name) VALUES ('Grilled');
INSERT INTO recipe_tags VALUES ('27', '14'); 
INSERT INTO recipe_tags VALUES ('28', '14');  


INSERT INTO tags (name) VALUES ('Chicken');
INSERT INTO tags (name) VALUES ('Pasta');
INSERT INTO recipe_tags VALUES ('29', '15'); 
INSERT INTO recipe_tags VALUES ('30', '15'); 


INSERT INTO tags (name) VALUES ('Beef');
INSERT INTO tags (name) VALUES ('Hearty');
INSERT INTO recipe_tags VALUES ('31', '16'); 
INSERT INTO recipe_tags VALUES ('32', '16');  


INSERT INTO tags (name) VALUES ('Vegetables');
INSERT INTO tags (name) VALUES ('Healthy');
INSERT INTO recipe_tags VALUES ('33', '17'); 
INSERT INTO recipe_tags VALUES ('34', '17');  


INSERT INTO tags (name) VALUES ('Pasta');
INSERT INTO tags (name) VALUES ('Cream');
INSERT INTO recipe_tags VALUES ('35', '18'); 
INSERT INTO recipe_tags VALUES ('36', '18');   


INSERT INTO tags (name) VALUES ('Lentil');
INSERT INTO tags (name) VALUES ('Healthy');
INSERT INTO recipe_tags VALUES ('37', '19'); 
INSERT INTO recipe_tags VALUES ('38', '19');  


INSERT INTO tags (name) VALUES ('Pasta');
INSERT INTO tags (name) VALUES ('Healthy');
INSERT INTO recipe_tags VALUES ('39', '20'); 
INSERT INTO recipe_tags VALUES ('40', '20');  


INSERT INTO tags (name) VALUES ('Chicken');
INSERT INTO tags (name) VALUES ('Cheese');
INSERT INTO recipe_tags VALUES ('41', '21'); 
INSERT INTO recipe_tags VALUES ('42', '21');  


INSERT INTO tags (name) VALUES ('Beef');
INSERT INTO tags (name) VALUES ('Mexican');
INSERT INTO recipe_tags VALUES ('43', '22'); 
INSERT INTO recipe_tags VALUES ('44', '22');  

INSERT INTO tags (name) VALUES ('Mushroom');
INSERT INTO tags (name) VALUES ('Italian');
INSERT INTO recipe_tags VALUES ('45', '23'); 
INSERT INTO recipe_tags VALUES ('46', '23');  


INSERT INTO tags (name) VALUES ('Fish');
INSERT INTO tags (name) VALUES ('Fast Food');
INSERT INTO recipe_tags VALUES ('47', '24'); 
INSERT INTO recipe_tags VALUES ('48', '24');  


INSERT INTO tags (name) VALUES ('Salad');
INSERT INTO tags (name) VALUES ('Healthy');
INSERT INTO recipe_tags VALUES ('49', '25'); 
INSERT INTO recipe_tags VALUES ('50', '25');  


INSERT INTO tags (name) VALUES ('Chicken');
INSERT INTO tags (name) VALUES ('Mexican');
INSERT INTO recipe_tags VALUES ('51', '26'); 
INSERT INTO recipe_tags VALUES ('52', '26');  

INSERT INTO tags (name) VALUES ('Pork');
INSERT INTO tags (name) VALUES ('Rice');
INSERT INTO recipe_tags VALUES ('53', '27'); 
INSERT INTO recipe_tags VALUES ('54', '27');  


INSERT INTO tags (name) VALUES ('Beef');
INSERT INTO tags (name) VALUES ('Vegetables');
INSERT INTO recipe_tags VALUES ('55', '28'); 
INSERT INTO recipe_tags VALUES ('56', '28'); 


INSERT INTO tags (name) VALUES ('Seafood');
INSERT INTO tags (name) VALUES ('Italian');
INSERT INTO recipe_tags VALUES ('57', '29'); 
INSERT INTO recipe_tags VALUES ('58', '29');  


INSERT INTO tags (name) VALUES ('Healthy');
INSERT INTO tags (name) VALUES ('Salad');
INSERT INTO recipe_tags VALUES ('59', '30'); 
INSERT INTO recipe_tags VALUES ('60', '30');  

select * from tags;

select * from tags;