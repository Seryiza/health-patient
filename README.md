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

Don't forget update `DATABASE_JDBC_URL` env variable if necessary

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

## Технические хотелки
- Пройтись свежим взглядом, добавить док-строки, если необходимо
- Добавить html escaping во все места вывода
- Добавить более красивое форматирование дат
- Вывод "нет пациентов", если в списке нет пациентов
- Подумать о замене своей функции `html/render` на `ring.util.response/response` или middleware для рендера hiccup
- Подумать про более изящное оформление селекторов в cljs
    - Оставить строками? Перенести на структуру данных?
- Добавить обработку кейса с дублированием номера ОМС
- Добавить отображение ошибок формы при создании/редактирования пациента (напр., сообщение о пользователе с таким номером ОМС)
- Добавить пагинацию для списка пациентов
- Заиспользовать clojure.spec?
    - Подумать про генерацию тестовых данных через spec
    - Подумать про замену библиотеки `struct` на clojure.spec
- Добавить кеширование через Redis
- Заменить подчёркивания на минусы в структуре пациента
- Написать макрос для более удобного связывания типа `[селектор-со-множеством-нод] (действие-для-каждой-ноды)` в cljs
- Подумать про рефакторинг роутинга
    - Сделать роут создания пациента как `/patients/create`? `/patients-forms/create`? 
- Подумать про функции для генерации роутов (url)
- Почитать и сделать более удобный cljs workflow с приятным repl
- Добавить деплой через k8s
- Оптимизировать `make ci` (сейчас дважды происходит скачивание пакетов из-за независимых запусков `docker-compose run`)
- Добавить аутентификацию/авторизацию
- Добавить милые 404 / 500
- Добавить linting в CI
- Добавить отправку ошибок куда-нибудь (в Sentry, напр.)
- Заиспользовать [testcontainers](https://github.com/javahippie/clj-test-containers) для более продвинутого запуска тестов?
- Написать фронтенда макрос "выполни функции, если валидация по схеме прошла успешно"
