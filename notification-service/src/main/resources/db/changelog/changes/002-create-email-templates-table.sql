-- liquibase formatted sql

-- changeset notification-service:002
CREATE TABLE EMAIL_TEMPLATE
(
    id UUID PRIMARY KEY,
    template_code VARCHAR(100) UNIQUE,
    subject VARCHAR(500),
    body TEXT
);