-- :name all-patients :query :many
-- :doc Get all patients
SELECT * FROM patients
WHERE is_deleted = false
ORDER BY id;

-- :name patient-by-id :query :one
-- :doc Get one patient by id
SELECT * FROM patients
WHERE id = :id AND is_deleted = false;

-- :name delete-patient-by-id :execute :affected
-- :doc Delete one patient by id
UPDATE patients
SET is_deleted = true
WHERE id = :id AND is_deleted = false;

-- :name update-patient-by-id :execute :affected
-- :doc Update one patient by id
UPDATE patients
SET first_name = :first_name,
    last_name = :last_name,
    middle_name = :middle_name,
    sex = :sex,
    birth_date = :birth_date,
    address = :address,
    cmi_number = :cmi_number
WHERE id = :id AND is_deleted = false;

-- :name insert-patient :insert :one
-- :doc Insert new patient. Return ID of inserted patient
INSERT INTO patients (first_name, last_name, middle_name, sex, birth_date, address, cmi_number)
VALUES (:first_name, :last_name, :middle_name, :sex, :birth_date, :address, :cmi_number)
