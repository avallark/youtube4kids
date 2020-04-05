drop table if exists users;

create table users (
user_id INTEGER NOT NULL AUTO_INCREMENT,
email_id char(100) not null unique,
password varchar(60) not null,
user_name varchar(50) not null unique,
full_name varchar(100) not null,
status varchar(1) default 'A',
user_role varchar(1) default 'C',
user_title varchar(20),
company_id integer not null,
phone1 varchar(15),
phone2 varchar(15),
last_updated_by integer,
last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by integer,
creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (user_id)
)
CHARACTER SET utf8 COLLATE utf8_general_ci;

-- Legend Status : A - Active , I - Inactive
-- user_role : A - Admin, U - User
-- All Users must take approval of admins

drop table if exists company;

create table company (
company_id integer not null auto_increment,
company_name varchar(30) not null,
registered_date date not null,
Paid_status varchar(1) not null,
last_paid_date date,
status varchar(1) default 'A',
last_updated_by integer,
last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by integer,
creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (company_id)
)
CHARACTER SET utf8 COLLATE utf8_general_ci;

drop table if exists user_company;
create table user_company (
user_id integer not null,
company_id integer not null,
last_updated_by integer,
last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by integer,
creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
CHARACTER SET utf8 COLLATE utf8_general_ci;


-- Multiple entries per user depending on the number of companies/contexts he is a part of.
-- Same email id can be used to register multiple companies.

-- users
insert into users (email_id, password, user_name, full_name, status, user_role, user_title, company_id, phone1, created_by, last_updated_by)
values (?, md5('password'), 'username', '?', 'A', '?',"Parent", 1, '?', 1, 1);
insert into users (email_id, password, user_name, full_name, status, user_role, user_title, company_id, phone1, created_by, last_updated_by)
values (?, md5('password'), 'username', '?', 'A', '?',"Parent", 1, '?', 1, 1);
insert into users (email_id, password, user_name, full_name, status, user_role, user_title, company_id, phone1, created_by, last_updated_by)
values (?, md5('password'), 'username', '?', 'U', '?',"Child", 1, '?', 1, 1);



-- company

insert into company (company_name, registered_date, paid_status, last_paid_date, created_by, last_updated_by) values ("?", now(), "P", now(), 1,1);

-- user_company

insert into user_company (user_id, company_id, created_by, last_updated_by) values (1,1,1,1);
insert into user_company (user_id, company_id, created_by, last_updated_by) values (2,1,1,1);
insert into user_company (user_id, company_id, created_by, last_updated_by) values (3,1,1,1);


-- nov 28 2019
drop table if exists notices;

create table notices (
notice_id integer not null auto_increment,
notice_subject varchar(60) not null,
notice text CHARACTER SET utf8,
notice_type varchar(1) default "N",
created_by integer,
creation_date timestamp default current_timestamp,
primary key (notice_id)
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- legend notice-type
-- N Notice, A Announcement, R Action Required

drop table if exists notify_records;

create table notify_records (
user_id integer,
group_flag varchar(1) default "N",
message varchar(255),
phone varchar(20),
notify_method varchar(20),
notify_type varchar(20),
results text,
creation_date timestamp default current_timestamp
)
CHARACTER SET utf8 COLLATE utf8_general_ci;

drop table if exists url_details;
create table url_details(
       url_id integer not null auto_increment,
       video_id varchar(20),
       video_title varchar(255),
       video_description text,
       video_thumbnail varchar(255),
       primary key (url_id)
)
CHARACTER SET utf8 COLLATE utf8_general_ci;

drop table if exists url_approval_requests;
create table url_approval_requests (
       url_id integer not null auto_increment ,
       user_id integer not null,
       approval_token varchar(32),
       approval_comments varchar(255),
       status varchar(1) default "P", -- P - pending, R - rejected, A - approved
       primary key (url_id)
)
CHARACTER SET utf8 COLLATE utf8_general_ci;
