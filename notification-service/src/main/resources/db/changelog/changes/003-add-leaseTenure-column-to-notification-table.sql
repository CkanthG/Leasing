-- liquibase formatted sql

-- changeset notification-service:003
ALTER TABLE NOTIFICATION ADD COLUMN lease_tenure BIGINT;