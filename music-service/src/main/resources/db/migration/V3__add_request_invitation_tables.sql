CREATE TABLE IF NOT EXISTS band_invitations (
    id SERIAL PRIMARY KEY,
    band_id INTEGER NOT NULL,
    invited_musician_id INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATE NOT NULL,
    CONSTRAINT fk_band FOREIGN KEY (band_id) REFERENCES band(id) ON DELETE CASCADE ,
    CONSTRAINT fk_invited FOREIGN KEY (invited_musician_id) REFERENCES musician(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS band_join_requests (
    id SERIAL PRIMARY KEY,
    band_id INTEGER NOT NULL,
    applicant_id INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATE NOT NULL,
    CONSTRAINT fk_band FOREIGN KEY (band_id) REFERENCES band(id) ON DELETE CASCADE ,
    CONSTRAINT fk_applicant FOREIGN KEY (applicant_id) REFERENCES musician(id) ON DELETE CASCADE
);