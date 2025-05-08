create table credentials (
    id bigserial primary key,
    email varchar(66) not null,
    password varchar not null
);