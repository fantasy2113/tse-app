CREATE TABLE IF NOT EXISTS licences
(
    id bigserial NOT NULL UNIQUE,
    licence_number character varying(120) NOT NULL,
    tse_type character varying(120) NOT NULL,
    number_of_tse integer NOT NULL,
    is_active boolean NOT NULL,
    created timestamp without time zone NOT NULL,
    modified timestamp without time zone NOT NULL,
    PRIMARY KEY (licence_number, tse_type)
);
