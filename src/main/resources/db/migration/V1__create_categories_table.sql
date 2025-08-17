CREATE TABLE IF NOT EXISTS categories (
  id              BIGSERIAL PRIMARY KEY,
  title           VARCHAR(100) NOT NULL,
  created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
