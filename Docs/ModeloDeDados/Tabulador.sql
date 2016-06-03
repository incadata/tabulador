/*
Created: 03/06/2016
Modified: 03/06/2016
Model: Tabulador - Modelo Físico
Database: PostgreSQL 9.2
*/


-- Create tables section -------------------------------------------------

-- Table tipo_campo

CREATE TABLE "tipo_campo"(
 "id_tipo_campo" Smallint NOT NULL,
 "ds_tipo_campo" Bigint
)
WITH (OIDS=FALSE)
;

-- Add keys for table tipo_campo

ALTER TABLE "tipo_campo" ADD CONSTRAINT "pk_tipo_campo" PRIMARY KEY ("id_tipo_campo")
;

-- Table cidade

CREATE TABLE "cidade"(
 "id_cidade" Bigint NOT NULL,
 "nm_cidade" Character(256) NOT NULL,
 "fg_capital_estado" Boolean,
 "fg_capital_rs" Boolean,
 "cd_ibge" Integer NOT NULL,
 "id_regional_saude" Bigint NOT NULL
)
WITH (OIDS=FALSE)
;

-- Add keys for table cidade

ALTER TABLE "cidade" ADD CONSTRAINT "pk_cidade" PRIMARY KEY ("id_cidade")
;

-- Table regional_saude

CREATE TABLE "regional_saude"(
 "id_regional_saude" Bigint NOT NULL,
 "nm_regional_saude" Character varying(150) NOT NULL
)
WITH (OIDS=FALSE)
;

-- Add keys for table regional_saude

ALTER TABLE "regional_saude" ADD CONSTRAINT "pk_regional_saude" PRIMARY KEY ("id_regional_saude")
;

-- Table estado

CREATE TABLE "estado"(
 "id_estado" Integer NOT NULL,
 "nm_estado" Bigint NOT NULL,
 "sg_estado" Character varying(2) NOT NULL,
 "id_regional_saude" Bigint NOT NULL,
 "id_cidade" Bigint
)
WITH (OIDS=FALSE)
;

-- Add keys for table estado

ALTER TABLE "estado" ADD CONSTRAINT "id_uf" PRIMARY KEY ("id_estado")
;

-- Table regiao

CREATE TABLE "regiao"(
 "id_regiao" Integer NOT NULL,
 "nm_regiao" Character varying(15),
 "sg_regiao" Character varying(6),
 "id_estado" Integer
)
WITH (OIDS=FALSE)
;

-- Add keys for table regiao

ALTER TABLE "regiao" ADD CONSTRAINT "Unique Identifier1" PRIMARY KEY ("id_regiao")
;

-- Table campo_config

CREATE TABLE "campo_config"(
 "id_campo_config" Integer NOT NULL,
 "nm_campo_config" Character varying(50) NOT NULL,
 "nm_label" Character varying(100) NOT NULL,
 "fg_filtro" Boolean,
 "nm_abreviado" Character varying(30),
 "id_tipo_campo" Smallint NOT NULL,
 "id_tabela_config" Integer NOT NULL,
 "id_tipo_filtro" Integer NOT NULL
)
WITH (OIDS=FALSE)
;

-- Add keys for table campo_config

ALTER TABLE "campo_config" ADD CONSTRAINT "pk_campo_config" PRIMARY KEY ("id_campo_config")
;

-- Table tabela_config

CREATE TABLE "tabela_config"(
 "id_tabela_config" Integer NOT NULL,
 "nm_tabela_config" Character varying(100) NOT NULL,
 "ds_titulo" Character varying(255),
 "fg_localidade" Boolean
)
WITH (OIDS=FALSE)
;

-- Add keys for table tabela_config

ALTER TABLE "tabela_config" ADD CONSTRAINT "pk_tabela_config" PRIMARY KEY ("id_tabela_config")
;

-- Table tipo_filtro

CREATE TABLE "tipo_filtro"(
 "id_tipo_filtro" Integer NOT NULL,
 "nm_tipo_filtro" Character varying(30)
)
WITH (OIDS=FALSE)
;

-- Add keys for table tipo_filtro

ALTER TABLE "tipo_filtro" ADD CONSTRAINT "pk_tipo_filtro" PRIMARY KEY ("id_tipo_filtro")
;

-- Table valor_campo_config

CREATE TABLE "valor_campo_config"(
 "id_valor_campo_config" Bigint NOT NULL,
 "cd_valor_campo_config" Integer,
 "ds_valor_campo_config" Character varying(50),
 "id_campo_config" Integer NOT NULL
)
WITH (OIDS=FALSE)
;

-- Add keys for table valor_campo_config

ALTER TABLE "valor_campo_config" ADD CONSTRAINT "pk_valor_campo_config" PRIMARY KEY ("id_valor_campo_config")
;

-- Create relationships section ------------------------------------------------- 

ALTER TABLE "estado" ADD CONSTRAINT "regiao_saude_estado" FOREIGN KEY ("id_regional_saude") REFERENCES "regional_saude" ("id_regional_saude") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "estado" ADD CONSTRAINT "fk_cidade_uf" FOREIGN KEY ("id_cidade") REFERENCES "cidade" ("id_cidade") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "regiao" ADD CONSTRAINT "fk_regiao_uf" FOREIGN KEY ("id_estado") REFERENCES "estado" ("id_estado") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "cidade" ADD CONSTRAINT "fk_regional_saude" FOREIGN KEY ("id_regional_saude") REFERENCES "regional_saude" ("id_regional_saude") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "campo_config" ADD CONSTRAINT "fk_campo_config_tp_campo" FOREIGN KEY ("id_tipo_campo") REFERENCES "tipo_campo" ("id_tipo_campo") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "campo_config" ADD CONSTRAINT "fk_campo_config_tabela" FOREIGN KEY ("id_tabela_config") REFERENCES "tabela_config" ("id_tabela_config") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "campo_config" ADD CONSTRAINT "fk_camp_config_tipo" FOREIGN KEY ("id_tipo_filtro") REFERENCES "tipo_filtro" ("id_tipo_filtro") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "valor_campo_config" ADD CONSTRAINT "fk_campo_config_valor_campo" FOREIGN KEY ("id_campo_config") REFERENCES "campo_config" ("id_campo_config") ON DELETE NO ACTION ON UPDATE NO ACTION
;





