DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'band_name_unique'
    ) THEN
ALTER TABLE band ADD CONSTRAINT band_name_unique UNIQUE (name);
END IF;
END $$;
