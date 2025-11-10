CREATE TABLE IF NOT EXISTS point (
  id BIGSERIAL PRIMARY KEY,
  function_id INT NOT NULL REFERENCES func(id) ON DELETE CASCADE,
  x_value DOUBLE PRECISION NOT NULL,
  y_value DOUBLE PRECISION NOT NULL,
  UNIQUE (function_id, x_value)
);

CREATE INDEX IF NOT EXISTS idx_point_function ON point(function_id);
CREATE INDEX IF NOT EXISTS idx_point_function_x ON point(function_id, x_value);