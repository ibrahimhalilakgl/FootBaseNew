CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE user_roles (
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE teams (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(10) UNIQUE NOT NULL
);

CREATE TABLE players (
    id BIGSERIAL PRIMARY KEY,
    team_id BIGINT REFERENCES teams (id) ON DELETE SET NULL,
    full_name VARCHAR(150) NOT NULL,
    position VARCHAR(50),
    shirt_number INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE matches (
    id BIGSERIAL PRIMARY KEY,
    home_team_id BIGINT REFERENCES teams (id) ON DELETE SET NULL,
    away_team_id BIGINT REFERENCES teams (id) ON DELETE SET NULL,
    kickoff_at TIMESTAMPTZ NOT NULL,
    venue VARCHAR(150),
    status VARCHAR(40) NOT NULL DEFAULT 'SCHEDULED'
);

CREATE TABLE match_predictions (
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT REFERENCES matches (id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    home_score INTEGER NOT NULL,
    away_score INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(match_id, user_id)
);

CREATE TABLE player_ratings (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT REFERENCES players (id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    score INTEGER NOT NULL CHECK (score BETWEEN 1 AND 10),
    comment TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE match_comments (
    id BIGSERIAL PRIMARY KEY,
    match_id BIGINT REFERENCES matches (id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- seed roles
INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'), ('ROLE_MODERATOR'), ('ROLE_USER')
ON CONFLICT DO NOTHING;

