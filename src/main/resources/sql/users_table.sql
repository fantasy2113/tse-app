CREATE TABLE public.users
(
    id         bigserial                                          NOT NULL UNIQUE,
    username   character varying(60) COLLATE pg_catalog."default" NOT NULL UNIQUE,
    user_role   character varying(60) COLLATE pg_catalog."default" NOT NULL,
    password   character varying(60) COLLATE pg_catalog."default" NOT NULL,
    active  boolean                                            NOT NULL,
    last_login timestamp without time zone                        NOT NULL,
    created    timestamp without time zone                        NOT NULL,
    modified   timestamp without time zone                        NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id, username),
    CONSTRAINT users_username_key UNIQUE (username)
);