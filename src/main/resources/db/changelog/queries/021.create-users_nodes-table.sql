create table users_nodes (
    id bigserial primary key,
    users_id bigint references users(id) on delete cascade,
    nodes_id bigint references nodes(id) on delete cascade
);