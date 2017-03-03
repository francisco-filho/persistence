DROP TABLE IF EXISTS officers;
CREATE TABLE officers (
  id         serial,
  rank       text NOT NULL,
  first_name text NOT NULL,
  last_name  text NOT NULL,
  PRIMARY KEY (id)
);