-- liquibase formatted sql

-- changeset lease:03
ALTER TABLE LEASE ADD COLUMN lease_tenure BIGINT;
