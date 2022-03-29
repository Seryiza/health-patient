test:
	lein test

jar:
	lein uberjar

start-jar:
	java -jar target/health-patient.jar

migration:
	lein migratus migrate
