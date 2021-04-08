-- user group
drop table if exists user_group cascade;
create table user_group
(
    id     serial,
    "name" varchar(20) not null,

    constraint user_group_pk primary key (id)
);

insert into user_group (name)
values ('owner');
insert into user_group (name)
values ('cashier');
insert into user_group (name)
values ('seller');
insert into user_group (name)
values ('customer');

-- user
drop table if exists "user" cascade;
create table "user"
(
    username      varchar(50),
    password_hash char(128) not null,
    user_group    integer,
    creation_time timestamp default now(),

    constraint user_pk primary key (username),
    constraint user_group_fk foreign key (user_group) references user_group (id)
);

insert into "user"
values ('machinemaster',
        '5bb8c38da81179ee2b6b56e211673dc6b00e391d8e1caa0e61dce0752a35649c97591f06f61374150d2c420263d3a089448c9735799195af7c755479e890e28c',
        1);
insert into "user"
values ('stockmaster',
        '2fb3acc5a878cd8a3b75522c334decd680291968b3e28e7a445308f107a76584ac1c0f785906ee43522f005f25dbcc72576b4736449b71078167f993692c7c6c',
        3);
insert into "user"
values ('cashmaster',
        '32d65aed58eac10aaa9b7d7ef75d06d850264001f0480a735330486e07ce838a8ea0624abf6a4d2fcbe016a804e19438f787eae0d43ba1226ee3bfd9a05904b8',
        2);
insert into "user"
values ('simon',
        'a4eea955b659e097b873f71fd60197d3b66337f94848ea1deedb711abc77ce84fc6e0aeaf084b0c68620bcf4431b9c80ceccad546d8d2fd7b3a7508ffa475cf9',
        4);

-- product category
drop table if exists product_category cascade;
create table product_category
(
    id     serial,
    "name" varchar(50),

    constraint product_category_pk primary key (id)
);

insert into product_category (name)
values ('drinks');
insert into product_category (name)
values ('chocolates');
insert into product_category (name)
values ('chips');
insert into product_category (name)
values ('candies');


-- product
drop table if exists product cascade;
create table product
(
    id       serial,
    "name"   varchar(50) not null,
    price    money       not null,
    quantity integer     not null,
    category serial,

    constraint product_category_fk foreign key (category) references product_category (id),
    constraint product_quantity_ck check (quantity >= 0 and quantity <= 15),
    constraint product_price_ck check (price >= 0::money)
);

insert into product (name, price, quantity, category)
values ('Mineral Water', 3.5, 7, 1);

insert into product (name, price, quantity, category)
values ('Sprite', 2.5, 7, 1);

insert into product (name, price, quantity, category)
values ('Coca cola', 2.5, 7, 1);

insert into product (name, price, quantity, category)
values ('Pepsi', 2.5, 7, 1);

insert into product (name, price, quantity, category)
values ('Juice', 3.5, 7, 1);

insert into product (name, price, quantity, category)
values ('M&M', 1.5, 7, 2);

insert into product (name, price, quantity, category)
values ('Mars', 2, 7, 2);

insert into product (name, price, quantity, category)
values ('Bounty', 1.5, 7, 2);

insert into product (name, price, quantity, category)
values ('Snickers', 1.5, 7, 2);

insert into product (name, price, quantity, category)
values ('Smiths', 3, 7, 3);

insert into product (name, price, quantity, category)
values ('Pringles', 3, 7, 3);

insert into product (name, price, quantity, category)
values ('Kettle', 2.5, 7, 3);

insert into product (name, price, quantity, category)
values ('Thins', 2, 7, 3);

insert into product (name, price, quantity, category)
values ('Mentos', 1, 7, 4);

insert into product (name, price, quantity, category)
values ('Sour Patch', 3, 7, 4);

insert into product (name, price, quantity, category)
values ('Skittles', 2, 7, 4);

insert into product (name, price, quantity, category)
values ('White Rabbit', 5, 0, 4);

-- cash
drop table if exists cash;
create table cash
(
    value    money,
    quantity integer,

    constraint cash_pk primary key (value),
    constraint cash_ck check ( quantity >= 0)
);

insert into cash
values (0.01, 50);
insert into cash
values (0.10, 50);
insert into cash
values (0.20, 50);
insert into cash
values (0.50, 50);
insert into cash
values (1.00, 50);
insert into cash
values (2.00, 50);
insert into cash
values (5.00, 50);
insert into cash
values (10.00, 50);
insert into cash
values (20.00, 50);
insert into cash
values (50.00, 50);

-- card
drop table if exists card;
create table card
(
    cardholder  varchar(50),
    card_number varchar(5),

    constraint card_pk primary key (card_number)
)
