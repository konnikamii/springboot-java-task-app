# Springboot Java Task Manager App

<p align="center" style="display: flex; justify-content: center; align-items: center;">
  <a href="https://fastapi.tiangolo.com/" rel="noopener noreferrer" target="_blank">
    <img src="https://images-cdn.openxcell.com/wp-content/uploads/2024/07/25070933/springboot-inner.svg" width="200" alt="Springboot Logo">
  </a>
</p>

<table align="center" > 
  <tr> 
    <td>
      <a href="https://www.postgresql.org/" rel="noopener noreferrer" target="_blank">
        <img src="https://www.unixmen.com/wp-content/uploads/2017/07/postgresql-logo.png" height="50" alt="PostgreSQL Logo">
      </a>
    </td>
    <td>
      <a href="https://github.com/mailhog" rel="noopener noreferrer" target="_blank">
        <img src="https://avatars.githubusercontent.com/u/10258541?s=200&v=4" height="50" alt="Mailhog Logo">
      </a>
    </td>
    <td>
      <a href="https://www.docker.com/" rel="noopener noreferrer" target="_blank">
        <img src="https://www.logo.wine/a/logo/Docker_(software)/Docker_(software)-Logo.wine.svg" height="100" alt="Docker Logo">
      </a>
    </td> 
  </tr>
</table>

## Description

This repository contains the backend for a task management platform application.

### Technologies Used

- **Backend**: Springboot written in Java 
- **Database**: PostgreSQL

### Additinal features include:

- **Mailhog** - email testing tool;
- **Backend Unit Testing**;
- **Backend Integration Testing**;  
- **Dockerized**: Easy setup and deployment

---

## Prerequisites

Ensure you have the following installed on your machine:
 
- [JDK](https://www.oracle.com/java/technologies/downloads/) (version **23.0.2** or higher) (if running locally)
- [Maven](https://pypi.org/project/pip/) (version **3.9.9** or higher) (if running locally) (optional)
- [Docker](https://www.docker.com/) (latest version)
- [Docker Compose](https://docs.docker.com/compose/) (latest version)

---

## Getting Started

#### 1. Make a folder where you will store the code:

```bash
mkdir springboot-java-task-app
```

#### 2. Clone the repository in the folder of your choice:

```bash
git clone https://github.com/konnikamii/springboot-java-task-app.git .
```
 

## Backend Setup
 
#### 1. Copy the example environment file and configure it:

```bash 
cp .\src\main\resources\.env.example .\src\main\resources\.env
```

#### 2. Clean and package the project:

You could simply use `mvn` if you have it already installed.

```bash
.\mvnw clean package -DskipTests                     # remove the flag if you are connected to the DB
``` 

#### 3. Run the project: (if running locally)

```bash
java -jar target/task-manager-0.0.1-SNAPSHOT.jar
```
   
## Docker Setup
 
#### 1. Build and start the Docker containers:

```bash
docker compose up --build
```

### Access the application:

By default:

- The **frontend** will be available at [http://localhost:3000](http://localhost:3000)
- The **backend** will be available at [http://localhost:8080](http://localhost:8080)

Try creating an account and logging in. There db is prepopulated with a few users and tasks.

---

### Additional Information:

- **Mailhog**:  
  Mailhog is included in the Docker setup to catch outgoing emails.  
  Access it at [http://localhost:8025](http://localhost:8025).

- **Database**:  
  The Docker setup includes a **PostgreSQL** database. Configure the connection in the `.env` file.

By default, the copied `.env` files should work when you run `docker compose up`.  
However, if any errors occur, ensure the correct **hostnames**, **ports**, and **credentials** are specified for **PostgreSQL** and **Mailhog**.  
Also, check the frontend and backend **hostnames** and **ports**.

The DB recreates all tables every time you restart the application. If you want to disable this behavior, change the `jpa:hibernate:ddl-auto: create-drop` to `update` and restart.
Note, you may need to make changes to the `DatabaseConfig` class to match your setup.

---

### Running Locally Without Docker:

You need the following:

- **PostgreSQL**:  
  Install **PostgreSQL** with its **GUI pgAdmin4** (optional) and create a database matching the name in your `.env` file.

    - Default port: `5432`
    - Ensure the `DATABASE_PORT` from the `.env` file and **PostgreSQL** are on the same port.
    - You can use the default `postgres` user and set a new password.
    - Ensure the host environment variable matches your local DB hostname (e.g., `DATABASE_HOSTNAME=127.0.0.1` or `DATABASE_HOSTNAME=localhost`).

- **Mailhog**:  
  Install and configure Mailhog to run on the following ports (or user Docker):

    - SMTP: `1025`
    - HTTP: `8025`

  [Here is a helpful guide for Windows users](https://runcloud.io/blog/mailhog-email-testing).


If Mailhog isn't configured or you don't want it in your setup you can just skip it. The backend will ignore it if there is no connection and will not throw an exception.

---

### Testing

There are also unit/integration tests included in the backend directory for some of the routes.
In order to run them make sure you can connect to the DB first, i.e. all `.env` variables are set correctly and that you have a test DB set up, then execute the following command:

```bash
mvn clean test
```
 
You should see that all tests are successful. If not, something with the setup is incorrect.
 
#### Helpfull commands for PostgreSQL

**Windows:**

```bash
pg_ctl.exe register -N "PostgreSQL" -U "NT AUTHORITY\NetworkService" -D "C:\Program Files\PostgreSQL\[version]\data" -w
                                                                  # creates a service to start on boot
pg_ctl status -D "C:\Program Files\PostgreSQL\[version]\data"     # checks the PostgreSQL process status
pg_ctl restart  -D "C:\Program Files\PostgreSQL\[version]\data"   # restart the PostgreSQL process

psql -p <port> -d <database-name> -U <user>                       # login as user in postgres database

\l+                                                               # size of DBs
\du                                                               # show all users
SELECT * FROM pg_user;                                            # show all users
CREATE DATABASE <database-name> TEMPLATE template0;               # create DB from clean template
DROP DATABASE IF EXISTS <database-name>;                          # remove DB
```

**Linux:**

```bash
pg_ctl status -D /var/lib/postgresql/[version]/main               # checks the PostgreSQL process status
pg_ctl restart -D /var/lib/postgresql/[version]/main              # restart the PostgreSQL process
pg_ctl start -D /var/lib/postgresql/[version]/main                # start the PostgreSQL process
pg_ctl stop -D /var/lib/postgresql/[version]/main                 # stop the PostgreSQL process
```
#### Helpfull commands for Docker

```bash
docker compose up                                                 # builds images and starts the containers (dafaults to: ./docker-compose.yaml ./.env)
docker compose down                                               # removes containers
docker compose config                                             # troubleshoots the setup

docker compose up --build                                         # forces image rebuilds
docker compose --project-name "my-app" up                         # flag for setting project name (if not specified)
docker compose -p "my-app" up                                     # shorthand for project name
docker compose -f <filename.yaml> up                              # runs a particular 'docker-compose.yaml' file
  
docker ps                                                         # lists all containers
docker logs <container_name_or_id>                                # check logs of container
docker stats                                                      # tracks active container resource utilization

docker exec -it <container_name_or_id> /bin/sh                    # enter container using shell
docker exec -it <container_name_or_id> bash                       # enter container using bash (if installed)
```