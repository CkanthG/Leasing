-- liquibase formatted sql

-- changeset bike-catalog:001
CREATE TABLE BIKE_CATALOG(
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(255),
    model VARCHAR(255),
    variant VARCHAR(255),
    engine_cc VARCHAR(255),
    price DOUBLE PRECISION,
    lease_amount DOUBLE PRECISION,
    lease_tenure BIGINT,
    mileage DOUBLE PRECISION,
    images bytea,
    availability_status BOOLEAN,
    insurance_details VARCHAR(255)
);