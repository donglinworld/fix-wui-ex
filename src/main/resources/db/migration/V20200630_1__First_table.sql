DROP TABLE IF EXISTS FirstTable;
CREATE TABLE FirstTable (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    job  TEXT DEFAULT NULL
);