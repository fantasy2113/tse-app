CREATE TABLE IF NOT EXISTS users
(
    id bigserial NOT NULL UNIQUE,
    username character varying(60) NOT NULL UNIQUE,
    user_role character varying(60) NOT NULL,
    password character varying(60) NOT NULL,
    active boolean NOT NULL,
    last_login timestamp without time zone NOT NULL,
    created timestamp without time zone NOT NULL,
    modified timestamp without time zone NOT NULL
);