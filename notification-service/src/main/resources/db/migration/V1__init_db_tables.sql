CREATE TABLE IF NOT EXISTS notifications (
    id SERIAL PRIMARY KEY,
    recipient_id INT NOT NULL,
    type VARCHAR(100) NOT NULL,
    title VARCHAR(50) NOT NULL,
    content VARCHAR(255) NOT NULL,
    metadata JSONB NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);
