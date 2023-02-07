--liquibase formatted sql
--changeset joyful:0.2.1 splitStatements:false failOnError:true dbms:postgresql

CREATE TABLE earth_event {
    id UUID,
    magnitude DOUBLE NOT NULL,
    region VARCHAR(256),
    location VARCHAR(256) NOT NULL,
    time TIMESTAMP,
    PRIMARY KEY (id);
}

-- DROP TABLE earth_event