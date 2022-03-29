# Health Patient: Clojure CRUD

[![Master CI](https://github.com/Seryiza/health-patient/actions/workflows/master-ci.yml/badge.svg)](https://github.com/Seryiza/health-patient/actions/workflows/master-ci.yml)

Simple web CRUD to manipulate patients. Application uses Clojure, ClojureScript (not for SPA), PostgreSQL and some cool libraries!

## How to setup and run
The project requires [Leiningen](https://leiningen.org/) (2.0.0+).

### Server REPL
```sh
# Setup env variables:
$ export HTTP_PORT=8080
$ export DATABASE_JDBC_URL=jdbc:postgresql://127.0.0.1:5432/health_patient?user=postgres&password=pass&stringtype=unspecified

# Start repl:
$ make install
$ make start

# Start server:
user=> (dev)            # Enter to development namespace
health-repl=> (start)   # Start systems (db connection, http server...)
health-repl=> (refresh) # Reload changed namespaces and restart systems
```

#### Docker database
If you want you can use PostgreSQL from Docker. It's easy:

```sh
$ make docker-build
$ make docker-db    # Database will run in background
```

Don't forget update `DATABASE_JDBC_URL` env variable

### ClojureScript
```sh
$ make auto-frontend
```

### Neovim
If you use [Neovim](https://neovim.io/) and [Conjure](https://github.com/Olical/conjure) you can configurate your init.lua like this:

```lua
vim.g['conjure#client#clojure#nrepl#refresh#after'] = 'health-repl/restart'
```

And restart changed namespaces by default mapping `<localleader>rr`. See [Conjure doc](https://github.com/Olical/conjure/blob/master/doc/conjure-client-clojure-nrepl.txt). It's very cool!
