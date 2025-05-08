create table users_roles (
    id bigserial primary key,
    credentials_id bigint references credentials(id),
    roles_id bigint references roles(id)
);