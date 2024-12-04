CREATE
database thuvien;
Use
thuvien
Create table users
(
    id                 int primary key auto_increment,
    fullname           varchar(100)          default '',
    phone_number       nvarchar(20) not null,
    address            varchar(200)          default '',
    password           varchar(100) not null default '',
    created_at         datetime,
    updated_at         datetime,
    is_active          tinyint               default 1,
    date_of_birth      date,
    facebook_acount_id int                   default 0,
    google_acount_id   int                   default 0

);
alter table users
    add column role_id int;


Create table roles
(
    id   int primary key,
    name varchar(20) not null
);

alter table users
    add foreign key (role_id) references roles (id);


create table tokens
(
    id              int primary key auto_increment,
    token           varchar(255) unique not null,
    token_type      varchar(50)         not null,
    expiration_date datetime,
    revoked         tinyint(1) not null,
    expired         tinyint(1) not null,
    user_id         int,
    foreign key (user_id) references users (id)
);
Create table social_accounts
(
    id         int primary key auto_increment,
    provider   varchar(20)  not null comment 'tên nhà social',
    provide_id varchar(50)  not null,
    email      varchar(150) not null comment 'Email tài khoản',
    name       varchar(100) not null comment 'tên người dùng',
    user_id    int,
    foreign key (user_id) references users (id)
);
create table categories
(
    id   int primary key auto_increment,
    name varchar(100) not null default '' comment 'sách sử',
);
Create table product
(
    id          int primary key auto_increment,
    name        varchar(100) comment 'tên sản phẩm',
    price       float not null check (price >= 0),
    thumbnail   varchar(300) default '',
    description longtext     default '',
    created_at  datetime,
    updated_at  datetime,
    Category_id int,
    Foreign key (Category_id) references categories (id)
);
Create table orders
(
    id           int primary key auto_increment,
    user_id      int,
    foreign key (user_id) references users (id),
    fullname     varchar(100) default '',
    email        varchar(100) default '',
    phone_number varchar(20)  not null,
    address      varchar(100) not null,
    note         varchar(100) default '',
    order_date   datetime     default current_timestamp,
    Status       varchar(20),
    total_money  float check (total_money >= 0)

);
alter table orders
    add column shipping_method nvarchar(100);
alter table orders
    add column shipping_address nvarchar(200);
alter table orders
    add column shipping_date date;
alter table orders
    add column tracking_number nvarchar(100);
alter table orders
    add column payment_method nvarchar(100);
--xoá 1đơn hàng => xoá mềm =>thêm actie
alter table orders
    add column active4 tinyint(1);
--trạng thái đơn hàng chỉ được phps nhận ' 1 số giá trị cụ thể'
alter table orders modify column status enum('pending','processing','shipped','delivered','cancelled') comment 'Trạng thái đơn hàng'

Create table order_detail
(
    id                 int primary key auto_increment,
    order_id           int,
    foreign key (order_id) references orders (id),
    product_id         int,
    foreign key (product_id) references product (id),
    price              float check (price >= 0),
    number_of_products int check (number_of_products > 0),
    total_money        float check (total_money >= 0),
    color              varchar(20) default ''


);

create table product_images(
       id                 int primary key auto_increment,
       product_id int,
        foreign key (product_id) references product (id),
constraint fk_product_images_product_id
foreign key (product_id) references product (id) on delete cascade 
);
alter table product_images
    add column image_url nvarchar(300);