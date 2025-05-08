create table users_nodes (
    id bigserial primary key,
    users_id bigint references users(id),
    nodes_id bigint references nodes(id)
);