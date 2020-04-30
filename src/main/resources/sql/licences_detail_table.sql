CREATE TABLE IF NOT EXISTS licences_detail
(
    id bigserial NOT NULL UNIQUE,
    licence_id bigint NOT NULL,
    branch_number character varying(120) NOT NULL,
    till_external_id character varying(120) NOT NULL,
    date_registered timestamp without time zone NOT NULL,
    modified timestamp without time zone NOT NULL,
    is_active boolean NOT NULL,
    PRIMARY KEY (licence_id, branch_number, till_external_id)
);
