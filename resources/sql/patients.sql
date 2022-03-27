-- :name all-patients :? :*
-- :doc Get all patients
SELECT * FROM patients
WHERE is_deleted = false
ORDER BY id;

-- :name patient-by-id :? :1
-- :doc Get one patient by id
SELECT * FROM patients
WHERE id = :id AND is_deleted = false;

-- :name delete-patient-by-id :! :n
-- :doc Delete one patient by id
UPDATE patients
SET is_deleted = true
WHERE id = :id AND is_deleted = false;

-- :name update-patient-by-id :! :n
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

-- :name insert-patient :! :n
-- :doc Insert new patient
INSERT INTO patients (first_name, last_name, middle_name, sex, birth_date, address, cmi_number)
VALUES (:first_name, :last_name, :middle_name, :sex, :birth_date, :address, :cmi_number)
