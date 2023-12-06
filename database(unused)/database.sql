create schema recipeapp;

use recipeapp;

CREATE TABLE users (
  user_id				INT NOT NULL AUTO_INCREMENT,
  name					VARCHAR(255) NOT NULL,
  email					VARCHAR(255) NOT NULL UNIQUE,
  password				VARCHAR(255) NOT NULL,
  PRIMARY KEY (user_id)
);

CREATE TABLE recipes (
  recipe_id				INT NOT NULL AUTO_INCREMENT,
  name					VARCHAR(255) NOT NULL,
  summary				TEXT,
  description			TEXT,
  user_id				INT NOT NULL,
  PRIMARY KEY (recipe_id)
);

CREATE TABLE messages (
msg_iD					INT NOT NULL AUTO_INCREMENT,
recipe_id				INT NOT NULL,
user_id					INT NOT NULL,
message					TEXT,

PRIMARY KEY (msg_id),
FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id),
FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE weekly (
weekly_id				INT NOT NULL,
user_id					INT NOT NULL,
recipe_id				INT NOT NULL,

PRIMARY KEY (weekly_id),
FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id),
FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE favourites (
favs_id					INT NOT NULL,
user_id					INT NOT NULL,
recipe_id				INT NOT NULL,

PRIMARY KEY (favs_id),
FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id),
FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE ingredients (
  ingredients_id		INT NOT NULL AUTO_INCREMENT,
  name					VARCHAR(255) NOT NULL,
  recipe_id				INT NOT NULL,
  amount				FLOAT,
  PRIMARY KEY (ingredients_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id)
);

CREATE TABLE tags (
  tag_id				INT NOT NULL AUTO_INCREMENT,
  user_id				INT,
  recipe_id				INT NOT NULL,
  
  name					VARCHAR(255) NOT NULL,
  PRIMARY KEY (tag_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id)
);

CREATE TABLE shopping (
shop_id					INT NOT NULL AUTO_INCREMENT,
user_id					INT NOT NULL,
weekly_id				INT,

PRIMARY KEY (shop_id),
FOREIGN KEY (user_id) REFERENCES users(user_id),
FOREIGN KEY (weekly_id) REFERENCES weekly(weekly_id)
);

