-- :name all-patients :? :*
-- :doc Get all patients
SELECT * FROM patients ORDER BY id;

-- :name patient-by-id :? :1
-- :doc Get one patient by id
SELECT * FROM patients WHERE id = :id;
