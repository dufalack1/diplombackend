create table sub_categories (
    id bigserial primary key,
    title varchar(100) not null,
    description text not null,
    short_description varchar(100) not null,
    image_url text not null,
    categories_id bigint references categories(id)
);