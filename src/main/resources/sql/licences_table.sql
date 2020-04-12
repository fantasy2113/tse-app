CREATE TABLE public.licences
(
    id bigserial NOT NULL UNIQUE,
    licence_number character varying(120) COLLATE pg_catalog."default" NOT NULL,
    tse_type character varying(120) COLLATE pg_catalog."default" NOT NULL,
    number_of_tse integer NOT NULL,
    is_active  boolean                                            NOT NULL,
    created    timestamp without time zone                        NOT NULL,
    modified   timestamp without time zone                        NOT NULL,
    CONSTRAINT licences_pkey PRIMARY KEY (licence_number, tse_type),
    CONSTRAINT licence_number_key UNIQUE (id)
);