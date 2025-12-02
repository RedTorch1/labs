-- create_tables.sql
-- users
CREATE TABLE IF NOT EXISTS app_user (
  id SERIAL PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(256) NOT NULL,
  created_at TIMESTAMP DEFAULT now()
);

-- functions
CREATE TABLE IF NOT EXISTS func (
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  name VARCHAR(200) NOT NULL,
  expression TEXT,
  created_at TIMESTAMP DEFAULT now()
);

-- points
CREATE TABLE IF NOT EXISTS point (
  id BIGSERIAL PRIMARY KEY,
  function_id INT NOT NULL REFERENCES func(id) ON DELETE CASCADE,
  x_value DOUBLE PRECISION NOT NULL,
  y_value DOUBLE PRECISION NOT NULL,
  UNIQUE (function_id, x_value)
);

CREATE INDEX IF NOT EXISTS idx_point_function ON point(function_id);
CREATE INDEX IF NOT EXISTS idx_point_function_x ON point(function_id, x_value);
