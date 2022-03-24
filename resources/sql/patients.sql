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
