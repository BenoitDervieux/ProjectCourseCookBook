#DROP SCHEMA chanuka1;
CREATE SCHEMA chanuka1;
USE chanuka1;

CREATE TABLE users (
  user_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  is_admin boolean default false,
  PRIMARY KEY (user_id)
);

CREATE TABLE recipes (
  recipe_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  summary TEXT,
  description TEXT,
  portion INT,
  PRIMARY KEY (recipe_id)
);

CREATE TABLE comments (
  comment_id INT NOT NULL AUTO_INCREMENT,
  recipe_id INT NOT NULL,
  user_id INT NOT NULL,
  comment TEXT,
  PRIMARY KEY (comment_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE instructions (
  instruction_id INT NOT NULL AUTO_INCREMENT,
  recipe_id INT NOT NULL,
  step_number INT NOT NULL,
  description TEXT NOT NULL,
  PRIMARY KEY (instruction_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id)
);

CREATE TABLE ingredients (
  ingredient_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (ingredient_id)
);

CREATE TABLE units (
  unit_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (unit_id)
);

CREATE TABLE recipe_ingredients (
  ingredient_id INT NOT NULL,
  recipe_id INT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  unit_id INT,
  PRIMARY KEY (ingredient_id, recipe_id),
  FOREIGN KEY (ingredient_id) REFERENCES ingredients(ingredient_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id),
  FOREIGN KEY (unit_id) REFERENCES units(unit_id)
);

CREATE TABLE tags (
  tag_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (tag_id)
);

CREATE TABLE recipe_tags (
  tag_id INT NOT NULL,
  recipe_id INT NOT NULL,
  PRIMARY KEY (tag_id, recipe_id),
  FOREIGN KEY (tag_id) REFERENCES tags(tag_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id)
);

CREATE TABLE messages (
  msg_id INT NOT NULL AUTO_INCREMENT,
  recipe_id INT NOT NULL,
  sender_id INT NOT NULL,
  receiver_id INT NOT NULL,
  message TEXT NOT NULL,
  PRIMARY KEY (msg_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id),
  FOREIGN KEY (sender_id) REFERENCES users(user_id),
  FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);

CREATE TABLE days (
  day_id INT NOT NULL AUTO_INCREMENT,
  day_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (day_id)
);

CREATE TABLE daily_recipes (
  daily_recipe_id INT NOT NULL AUTO_INCREMENT,
  day_id INT NOT NULL,
  recipe_id INT NOT NULL,
  portions INT,
  PRIMARY KEY (daily_recipe_id),
  FOREIGN KEY (day_id) REFERENCES days(day_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id) ON DELETE CASCADE
);

CREATE TABLE weekly (
  weekly_id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  week_no INT,
  PRIMARY KEY (weekly_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE daily_recipes_of_week (
  daily_recipes_of_week_id INT NOT NULL AUTO_INCREMENT,
  daily_recipe_id INT NOT NULL,
  weekly_id INT NOT NULL,
  PRIMARY KEY (daily_recipes_of_week_id),
  FOREIGN KEY (daily_recipe_id) REFERENCES daily_recipes(daily_recipe_id) ON DELETE CASCADE,
  FOREIGN KEY (weekly_id) REFERENCES weekly(weekly_id) ON DELETE CASCADE
);


CREATE TABLE favourites (
  favs_id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  recipe_id INT NOT NULL,
  PRIMARY KEY (favs_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE shopping (
  shop_id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  weekly_id INT,
  ingredient_id INT NOT NULL,
  amount TEXT NOT NULL,
  PRIMARY KEY (shop_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (weekly_id) REFERENCES weekly(weekly_id),
  FOREIGN KEY (ingredient_id) REFERENCES ingredients(ingredient_id)
);

CREATE TABLE ratings (
  rating_id INT NOT NULL AUTO_INCREMENT,
  recipe_id INT NOT NULL,
  user_id INT NOT NULL,
  rating INT NOT NULL,
  PRIMARY KEY (rating_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

create table user_tags (

tag_id int not null auto_increment,
name varchar(50),
user_id int not null,

primary key (tag_id),
foreign key (user_id) references users(user_id)
);

create table user_recipe_tags (

tag_id int not null,
name varchar(50),
recipe_id int not null,

primary key (tag_id, recipe_id),
foreign key (recipe_id) references recipes(recipe_id),
foreign key (tag_id) references user_tags(tag_id)
);

