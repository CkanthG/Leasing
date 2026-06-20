-- liquibase formatted sql

-- changeset notification-service:001
CREATE TABLE NOTIFICATION
(
    id UUID PRIMARY KEY,
    event_id VARCHAR(100) NOT NULL,
    lease_id UUID,
    user_id BIGINT,
    email VARCHAR(255) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    subject VARCHAR(500) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    retry_count INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    sent_at TIMESTAMP,
    failure_reason TEXT
);