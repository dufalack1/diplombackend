create table invitations (
    id bigserial primary key,
    email varchar(50) not null,
    generated_code text not null,
    expiration_time date not null
);