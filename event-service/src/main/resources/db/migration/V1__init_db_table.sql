CREATE TABLE event (
    id SERIAL PRIMARY KEY,
    organizer_id INTEGER NOT NULL,
    venue_id INTEGER NOT NULL,
    name varchar(50) NOT NULL,
    date date NOT NULL,
    start_time time NOT NULL,
    end_time time NOT NULL,
    description varchar(255),
    ticket_price DECIMAL(10,2)
);