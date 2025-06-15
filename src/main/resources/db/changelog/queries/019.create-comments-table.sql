create table comments (
    id bigserial primary key,
    "message" text not null,
    publication_time timestamp not null default now(),
    users_id bigint references users(id) on delete cascade,
    nodes_id bigint references nodes(id) on delete cascade
);