create table categories (
    id bigserial primary key,
    title varchar not null,
    description text not null,
    short_description varchar not null,
    image_url text not null
);