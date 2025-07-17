CREATE TABLE offer_availability (
    id SERIAL PRIMARY KEY,
    offer_id INTEGER NOT NULL,
    start_time time NOT NULL,
    end_time time NOT NULL
    FOREIGN KEY (offer_id) REFERENCES offer(id) ON DELETE CASCADE
);
