use chanuka1;

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

