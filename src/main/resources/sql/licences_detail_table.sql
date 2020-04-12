CREATE TABLE public.licences_detail
(
    id bigserial NOT NULL UNIQUE,
    licence_id bigint NOT NULL,
    branch_number integer NOT NULL,
    till_external_id integer NOT NULL,
    date_registered timestamp without time zone NOT NULL,
    modified   timestamp without time zone                        NOT NULL,
    is_active  boolean                                            NOT NULL,
    CONSTRAINT licences_detail_pkey PRIMARY KEY (licence_id, branch_number, till_external_id),
    CONSTRAINT licence_detail_number_key UNIQUE (id)
);