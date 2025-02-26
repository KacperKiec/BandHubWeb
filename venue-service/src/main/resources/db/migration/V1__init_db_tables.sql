CREATE TABLE venue (
    id SERIAL PRIMARY KEY,
    owner_id INTEGER NOT NULL,
    name varchar(40) NOT NULL,
    city varchar(30) NOT NULL,
    address varchar(50) NOT NULL,
    capacity INTEGER NOT NULL,
    description varchar(150)
);

CREATE TYPE offer_status AS ENUM ('ACTIVE', 'CLAIMED', 'CANCELED');

CREATE TABLE offer (
    id SERIAL PRIMARY KEY,
    venue_id INTEGER NOT NULL,
    price_per_hour DECIMAL(10, 2) NOT NULL,
    date date NOT NULL,
    start_time time NOT NULL,
    end_time time NOT NULL,
    status offer_status NOT NULL,
    FOREIGN KEY (venue_id) REFERENCES venue(id) ON DELETE CASCADE
);

CREATE TABLE booking (
    id SERIAL PRIMARY KEY,
    tenant_id INTEGER NOT NULL,
    offer_id INTEGER NOT NULL,
    booking_date date NOT NULL,
    FOREIGN KEY (offer_id) REFERENCES offer(id) ON DELETE CASCADE
);