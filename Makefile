.PHONY: test

prepare-and-run-jar: install migrate frontend compile-jar start-jar

env-prepare:
	cp .env.example .env

install:
	lein deps

start: migrate
	lein repl

frontend:
	lein cljsbuild once

auto-frontend:
	lein cljsbuild auto

test:
	lein test

compile-jar:
	lein uberjar

start-jar:
	java -jar target/health-patient.jar

migrate:
	lein migratus migrate

docker-ci: docker-setup docker-test

docker-setup: env-prepare docker-build

docker-build:
	docker-compose build

docker-test:
	docker-compose run app make migrate
	docker-compose run app make test

docker-db:
	docker-compose up -d db
