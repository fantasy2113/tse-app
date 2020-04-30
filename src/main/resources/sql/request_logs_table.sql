CREATE TABLE IF NOT EXISTS request_logs
(
    id bigserial NOT NULL UNIQUE,
    licence_detail_id bigint NOT NULL,
    request_type character varying(60) NOT NULL,
    request_date timestamp without time zone NOT NULL,
    PRIMARY KEY (licence_detail_id)
);