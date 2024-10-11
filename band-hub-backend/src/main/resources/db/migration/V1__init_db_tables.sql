CREATE TABLE musician (
    id int(8) AUTO_INCREMENT PRIMARY KEY,
    name varchar(30) NOT NULL,
    email varchar(30) NOT NULL,
    password varchar(20) NOT NULL,
    age int (2),
    city varchar(20),
    sex ENUM('MALE', 'FEMALE') NOT NULL DEFAULT 'MALE',
    bio varchar(200)
);

CREATE TABLE band (
    id int(8) AUTO_INCREMENT PRIMARY KEY,
    name varchar(30) NOT NULL,
    founder_id int(8) NOT NULL,
    max_members int(3),
    is_looking_members bit,
    city varchar(20),
    bio varchar(400),
    CONSTRAINT FOREIGN KEY (founder_id) REFERENCES musician(id)
);

CREATE TABLE musician_genre (
    musician_id int(8),
    genre ENUM('ROCK', 'METAL', 'GRUNGE', 'POP', 'HIPHOP', 'CLASSIC', 'JAZZ', 'INDIE'),
    PRIMARY KEY (musician_id, genre),
    CONSTRAINT FOREIGN KEY (musician_id) REFERENCES musician(id)
);

CREATE TABLE band_genre (
    band_id int(8),
    genre ENUM('ROCK', 'METAL', 'GRUNGE', 'POP', 'HIPHOP', 'CLASSIC', 'JAZZ', 'INDIE'),
    PRIMARY KEY (band_id, genre),
    CONSTRAINT FOREIGN KEY (band_id) REFERENCES band(id)
);

CREATE TABLE musician_instrument (
    musician_id int(8),
    instrument ENUM('ACOUSTIC_GUITAR', 'ELECTRIC_GUITAR', 'BASS', 'DRUMS', 'KEYBOARD', 'VOICE'),
    PRIMARY KEY (musician_id, instrument),
    CONSTRAINT FOREIGN KEY (musician_id) REFERENCES musician(id)
);

CREATE TABLE band_requested_instrument (
     band_id int(8),
     instrument ENUM('ACOUSTIC_GUITAR', 'ELECTRIC_GUITAR', 'BASS', 'DRUMS', 'KEYBOARD', 'VOICE'),
     PRIMARY KEY (band_id, instrument),
     CONSTRAINT FOREIGN KEY (band_id) REFERENCES band(id)
);

CREATE TABLE band_current_instrument (
      band_id int(8),
      instrument ENUM('ACOUSTIC_GUITAR', 'ELECTRIC_GUITAR', 'BASS', 'DRUMS', 'KEYBOARD', 'VOICE'),
      PRIMARY KEY (band_id, instrument),
      CONSTRAINT FOREIGN KEY (band_id) REFERENCES band(id)
);

CREATE TABLE musician_band (
    musician_id int(8),
    band_id int(8),
    enter_date DATE,
    PRIMARY KEY (musician_id, band_id),
    CONSTRAINT FOREIGN KEY (musician_id) REFERENCES musician(id),
    CONSTRAINT FOREIGN KEY (band_id) REFERENCES band(id)
)
