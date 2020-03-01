SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL SERIALIZABLE
-- drop table brands if exists
-- drop table categories if exists
-- drop table products if exists
-- drop table products_tags if exists
-- drop table tags if exists
create table brands (id bigint generated by default as identity, name varchar(255), version bigint, primary key (id))
create table categories (id bigint generated by default as identity, name varchar(255), version bigint, primary key (id))
create table products (id binary not null, created_at timestamp, description varchar(255), name varchar(255), version bigint, brand_id bigint, category_id bigint, primary key (id))
create table products_tags (product_id binary not null, tags_id bigint not null, primary key (product_id, tags_id))
create table tags (id bigint generated by default as identity, name varchar(255), version bigint, primary key (id))
alter table brands add constraint UKoce3937d2f4mpfqrycbr0l93m unique (name)
alter table categories add constraint UKt8o6pivur7nn124jehx7cygw5 unique (name)
alter table tags add constraint UKt48xdq560gs3gap9g7jg36kgc unique (name)
alter table products add constraint FKa3a4mpsfdf4d2y6r8ra3sc8mv foreign key (brand_id) references brands
alter table products add constraint FKog2rp4qthbtt2lfyhfo32lsw9 foreign key (category_id) references categories
alter table products_tags add constraint FK9f89byp75bd6wdttkrcngfu23 foreign key (tags_id) references tags
alter table products_tags add constraint FKt6jksc02nxg0qutvynpis3lyo foreign key (product_id) references products