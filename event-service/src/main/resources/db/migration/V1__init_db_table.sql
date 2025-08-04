CREATE TABLE event (
    id SERIAL PRIMARY KEY,
    name varchar(255) NOT NULL,
    band_id INTEGER NOT NULL,
    venue_id INTEGER NOT NULL,
    booking_id INTEGER,
    date date NOT NULL,
    start_time time NOT NULL,
    end_time time NOT NULL,
    description varchar(255) NOT NULL,
    ticket_price DECIMAL(10,2) NOT NULL
);