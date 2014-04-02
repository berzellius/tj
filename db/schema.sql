CREATE TABLE cat_contract
(
  id bigint NOT NULL,
  name character varying(255),
  value character varying(255),
  CONSTRAINT cat_contract_pkey PRIMARY KEY (id)
);

CREATE TABLE cat_contract_status
(
  id bigint NOT NULL,
  value character varying(255),
  CONSTRAINT cat_contract_pkey PRIMARY KEY (id)
);

CREATE TABLE postgres.contract
(
  id bigint NOT NULL,
  c_memo character varying(255),
  c_number character varying(10) NOT NULL,
  version integer,
  cc_id bigint,
  ccs_id bigint,
  CONSTRAINT contract_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fsg0uqj1ci4iep45pefolv80g FOREIGN KEY (ccs_id)
      REFERENCES postgres.cat_contract_status (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_jjh6x1ltqv0q98n0axh6iis3p FOREIGN KEY (cc_id)
      REFERENCES postgres.cat_contract (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE locale
(
  entity character varying(31) NOT NULL,
  id bigint NOT NULL,
  entity_id bigint NOT NULL,
  locale character varying(255) NOT NULL,
  name character varying(255),
  value character varying(255),
  ccl_id bigint,
  CONSTRAINT locale_pkey PRIMARY KEY (id),
  CONSTRAINT fk_nfw26oi133f2jk2360ggmt3sw FOREIGN KEY (ccl_id)
      REFERENCES cat_contract (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE contract_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE cat_contract_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE cat_contract_status_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE locale_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;



