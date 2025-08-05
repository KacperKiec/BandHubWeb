CREATE TABLE user_events (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    event_id INTEGER NOT NULL,
    relation VARCHAR(25) NOT NULL,
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
);