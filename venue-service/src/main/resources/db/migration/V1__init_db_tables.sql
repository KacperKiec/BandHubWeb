CREATE TABLE IF NOT EXISTS venue (
    id SERIAL PRIMARY KEY,
    owner_id INTEGER NOT NULL,
    name varchar(40) NOT NULL,
    capacity INTEGER NOT NULL,
    description varchar(150),
    street VARCHAR(50) NOT NULL,
    number VARCHAR(10) NOT NULL,
    city VARCHAR(50) NOT NULL,
    postal_code VARCHAR(15) NOT NULL,
    country VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS offer (
    id SERIAL PRIMARY KEY,
    venue_id INTEGER NOT NULL,
    price_per_hour DECIMAL(10, 2) NOT NULL,
    date date NOT NULL,
    start_time time NOT NULL,
    end_time time NOT NULL,
    status varchar(20) CHECK (status IN ('ACTIVE', 'CLAIMED', 'CANCELED', 'ENDED')) NOT NULL,
    FOREIGN KEY (venue_id) REFERENCES venue(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS booking (
    id SERIAL PRIMARY KEY,
    tenant_id INTEGER NOT NULL,
    offer_id INTEGER NOT NULL,
    start_time time NOT NULL,
    end_time time NOT NULL,
    booking_date date NOT NULL,
    FOREIGN KEY (offer_id) REFERENCES offer(id) ON DELETE CASCADE
);