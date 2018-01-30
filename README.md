# SLN's Book Shop

### If you have cloned this project from git	
- Edit application-local.properties for DB URL, username and password
- Crate tables manually or set spring.jpa.hibernate.ddl-auto=create
- Rename mail.properties-dummy to mail.properties and update password

### Profiles
- There are 2 profiles in this app: local and heroku
	Profiles determine which application.properties files will be used
- Profile examples: http://www.baeldung.com/spring-profiles
- spring.profiles.default can be used to set default profile
	- If spring.profiles.active is set (which is being done in Heroku's Maven plugin), then its value determines which profiles are active.
	- But if spring.profiles.active isn’t set, then Spring looks to spring.profiles.default
	- When spring.profiles.active is set, it doesn’t matter what spring.profiles.default is set to; the profiles set in spring.profiles.active take precedence.

### SQL for remember-me persistent logins
```
CREATE TABLE `persistent_logins` (
	`username` VARCHAR(64) NOT NULL,
	`series` VARCHAR(64) NOT NULL,
	`token` VARCHAR(64) NOT NULL,
	`last_used` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`series`)
) ENGINE=InnoDB;
``` 

### Compile, run, deploy
- To compile (skipTests is necessary to prevent it connecting to DB, because you're not using any profile here):
`mvn clean package -DskipTests`
	
- To run locally:  
`java -Dserver.port=8090 -Dspring.profiles.active=local -jar book-shop-0.0.1-SNAPSHOT.jar`  
OR  
`mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=local"`
	- setting `spring.profiles.active=local` will make Spring use `application-local.properties`
	- when it is being run on Heroku, heroku-maven-plugin in pom.xml uses this instead: `-Dspring.profiles.active=heroku`
	- alternatively, you can set OS System Environment variable `SPRING_PROFILES_ACTIVE=local`
	for more info see:  
	https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html  
	https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config

- To run in Eclipse:  
	- Run -> Run Configurations... -> Spring Boot App -> <book-shop> -> Spring Boot tab -> Profile = local
	- OR set system property spring.profiles.active=local

- To deploy:  
`mvn clean heroku:deploy -DskipTests`

- To see logs:  
`heroku logs --app APP-NAME --num NUMER_OF_LINES`

### UML diagram
![alt text](https://raw.githubusercontent.com/randle2000/book-shop/master/UML.png "My UML")