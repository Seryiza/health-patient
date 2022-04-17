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

dev-frontend:
	npx shadow-cljs watch app

test:
	lein test

compile-jar:
	lein uberjar

start-jar:
	java -jar target/health-patient.jar

migrate:
	lein migratus migrate

ci: migrate test

docker-ci: docker-setup docker-test

docker-setup: env-prepare docker-build

docker-build:
	docker-compose build

docker-test:
	docker-compose run app make ci

docker-db:
	docker-compose up -d db
