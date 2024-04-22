drop database expenseTracker_DB;
drop user expenseTracker;
create user expenseTracker with password 'password';
create user expenseTracker_DB with template=template0 owner=expenseTracker;
\connect expenseTracker_DB;
alter default privileges grant all on tables to expenseTracker;
alter default privileges grant all on sequences to expenseTracker;

create table et_users(
user_id integer primary key not null,
first_name varchar(30) not null,
last_name varchar(30) not null,
email varchar(30) not null,
password text not null);