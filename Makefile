.PHONY: test

prepare-and-run-jar: install migrate frontend compile-jar start-jar
ci: docker-setup docker-test
docker-setup: env-prepare docker-build

env-prepare:
	cp .env.example .env

install:
	lein deps

frontend:
	lein cljsbuild once

test:
	lein test

compile-jar:
	lein uberjar

start-jar:
	java -jar target/health-patient.jar

migrate:
	lein migratus migrate

docker-build:
	docker-compose build

docker-test:
	docker-compose run app make migrate
	docker-compose run app make test
