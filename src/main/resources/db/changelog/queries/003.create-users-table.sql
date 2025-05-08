create table users (
    id bigserial primary key,
    birth_date date not null,
    displayed_name varchar(25) not null,
    avatar_url text not null,
    phone_number varchar(15) default null,
    credentials_id bigint references credentials(id)
);