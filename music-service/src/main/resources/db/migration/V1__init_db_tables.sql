CREATE TABLE musician (
    id SERIAL PRIMARY KEY,
    is_looking_band boolean NOT NULL ,
    bio varchar(255)
);

CREATE TABLE band (
    id SERIAL PRIMARY KEY,
    name varchar(30) NOT NULL,
    is_looking_members boolean NOT NULL,
    city varchar(20),
    bio varchar(400)
);

CREATE TABLE genre (
    id SERIAL PRIMARY KEY,
    name varchar(40) NOT NULL
);

CREATE TABLE instrument (
    id SERIAL PRIMARY KEY,
    name varchar(40) NOT NULL
);

CREATE TABLE musician_instrument (
    musician_id INTEGER,
    instrument_id INTEGER,
    PRIMARY KEY (musician_id, instrument_id),
    FOREIGN KEY (musician_id) REFERENCES musician(id) ON DELETE CASCADE,
    FOREIGN KEY (instrument_id) REFERENCES instrument(id) ON DELETE CASCADE
);

CREATE TABLE musician_genre (
    musician_id INTEGER,
    genre_id INTEGER,
    PRIMARY KEY (musician_id, genre_id),
    FOREIGN KEY (musician_id) REFERENCES musician(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE
);

CREATE TABLE band_genre (
    band_id INTEGER,
    genre_id INTEGER,
    PRIMARY KEY (band_id, genre_id),
    FOREIGN KEY (band_id) REFERENCES band(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE
);

CREATE TABLE band_musician (
    band_id INTEGER,
    musician_id INTEGER,
    join_date DATE,
    is_founder boolean NOT NULL,
    PRIMARY KEY (musician_id, band_id),
    FOREIGN KEY (musician_id) REFERENCES musician(id) ON DELETE CASCADE,
    FOREIGN KEY (band_id) REFERENCES band(id) ON DELETE CASCADE
);