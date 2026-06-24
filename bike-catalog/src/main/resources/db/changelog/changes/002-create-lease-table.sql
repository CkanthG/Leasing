-- liquibase formatted sql

-- changeset lease:002
CREATE TABLE LEASE(
    id UUID PRIMARY KEY,
    bike_catalog_id BIGINT,
    lease_id UUID,
    event_id UUID,
    user_id BIGINT,
    email VARCHAR(255),
    event_type VARCHAR(255),
    subject VARCHAR(255),
    created_at TIMESTAMP,

    CONSTRAINT fk_lease
            FOREIGN KEY (bike_catalog_id)
            REFERENCES bike_catalog(id)
);