create table join_requests (
    id bigserial primary key,
    is_invited boolean not null default false,
    users_id bigint references users(id),
    nodes_id bigint references nodes(id)
);