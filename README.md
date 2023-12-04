# Service-Registry

## How to build and run

Pre-requisites:
- Java 17
- Maven

From the root directory of the project run:
```console
mvn clean package
java -jar ./target/Service-Registry-1.0-SNAPSHOT.jar --port 8001 # default port is 8000
```