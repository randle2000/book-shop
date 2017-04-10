CREATE TABLE `persistent_logins` (
	`username` VARCHAR(64) NOT NULL,
	`series` VARCHAR(64) NOT NULL,
	`token` VARCHAR(64) NOT NULL,
	`last_used` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`series`)
) ENGINE=InnoDB; 

java -Dserver.port=8090 -jar book-shop-0.0.1-SNAPSHOT.jar
or mvn spring-boot:run ?

mvn clean heroku:deploy