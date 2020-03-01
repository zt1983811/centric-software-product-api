INSERT INTO categories (`name`) VALUES ('beer'), ('wine'), ('cake');
INSERT INTO brands (`name`) VALUES ('nike'), ('adidas'), ('puma');
INSERT INTO tags (`name`) VALUES ('leo'), ('mouse'), ('dog');
INSERT INTO products (id, `name`, description, brand_id, category_id)
VALUES ('1a17ee531d54e0dd8255be821361f1aa', 'shoes', 'special shoses', 1 ,1),
('38ee0f2f4d8c8fac0881bb1ccf7e1698', 'tshirt', 'dirty tshirt', 2 ,1);
INSERT INTO products_tags (product_id, tags_id)
VALUES ('1a17ee531d54e0dd8255be821361f1aa', 1),
('38ee0f2f4d8c8fac0881bb1ccf7e1698', 3);

