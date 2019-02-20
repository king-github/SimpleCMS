# Simple Content Management System

Look into wiki to see screenshots.   

## Used modules and frameworks:
- java 8
- spring boot 2 (currently 2.0.8)
- JPA (on Hibernate) with spring-boot-data
- spring security users with roles and authorities
- Jade (PUG) not much popular but very elegant template engine

## Compile and run:

**Run with maven:**

Running with profile 'dev' you don't need outer database 
(will be used H2). For other profiles '*dev2*'(mySQL), '*dev3*' 
(PostgreSQL) you need configure db connection in 
application-devX.properties. The database is created 
every time the app is started. You can change it in properties file.

Data for pre-filling db is in FixturesDev class. You can sign in to 
panel with username *admin* and password *pass*.    
```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Compile with maven to jar:**
```
mvn package spring-boot:repackage
```

**Run from command line:**
```
java -Dspring.profiles.active=dev  -jar target/simple-cms-0.0.1-SNAPSHOT.jar
```
**Run tests:**
```
mvn -e test
```
- Front of CMS: [localhost:8080/](http://localhost:8080/)
- Panel of CMS: [localhost:8080/panel](http://localhost:8080/panel)

**Profiles:**
- dev  - storage in h2 database - config in application-dev.properties
- dev2 - storage in mySQL(MariaDB) - config in application-dev2.properties
- dev3 - storage in postgreSQL - config in application-dev3.properties

## To do:
- Increase test coverage
- Add password change form
- Add images for articles
- Add full csrf support (Jade not supports out of the box csrf)
- Refactor some class


