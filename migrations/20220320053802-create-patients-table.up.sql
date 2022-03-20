CREATE TABLE patients (
  id SERIAL PRIMARY KEY,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  middle_name TEXT NULL DEFAULT NULL,
  sex human_sexes NOT NULL,
  birth_date DATE NOT NULL,
  address TEXT NULL DEFAULT NULL,
  cmi_number VARCHAR(16) NOT NULL
);

--;;

COMMENT ON COLUMN patients.cmi_number IS 'Compulsory Medical Insurance (OMS) number';
