create table users_categories (
    id bigserial primary key,
    users_id bigint references users(id) on delete cascade ,
    categories_id bigint references categories(id) on delete cascade
);