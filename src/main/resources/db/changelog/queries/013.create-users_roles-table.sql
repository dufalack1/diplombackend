create table users_roles (
    id bigserial primary key,
    credentials_id bigint references credentials(id) on delete cascade,
    roles_id bigint references roles(id) on delete cascade
);