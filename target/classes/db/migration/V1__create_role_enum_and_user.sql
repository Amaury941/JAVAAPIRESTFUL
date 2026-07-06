CREATE TYPE role AS ENUM ('ADMIN', 'EDITOR', 'VIEWER');

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role role NOT NULL DEFAULT 'VIEWER',
    created_at TIMESTAMP NOT NULL DEFAULT now()
);