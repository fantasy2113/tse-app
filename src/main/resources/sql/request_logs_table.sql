CREATE TABLE public.request_logs
(
    id bigserial NOT NULL UNIQUE,
    licence_id bigint NOT NULL,
    request_type character varying(60) COLLATE pg_catalog."default" NOT NULL,
    request_date timestamp without time zone NOT NULL,
    CONSTRAINT request_logs_pkey PRIMARY KEY (id,licence_id)
);