FROM clojure:lein-2.9.8

RUN apt-get update
RUN apt-get install -y make

WORKDIR /app
