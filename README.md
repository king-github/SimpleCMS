# Simple Content Management System

## Used modules and frameworks:
- java 8
- spring boot 2 (currently 2.0.8)
- JPA (on Hibernate) with spring-boot-data
- spring security users with roles and authorities
- Jade (PUG) not much popular but very elegant view engine

## Compile and run:

**Run with maven:**

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
- dev - h2 database - config in application-dev.properties
- dev2 - mySQL(MariaDB) - config in application-dev2.properties

## To do:
- Increase test coverage
- Add password change form
- Add images for articles
- Add full csrf support (Jade not supports out of the box csrf)
- Refactor some class
