FROM jelastic/maven:3.8.6-openjdk-20.ea-b24
MAINTAINER Marcello Lopes (marcellolopesdev@gmail.com)
COPY pom.xml /usr/local/service/pom.xml
COPY .env /usr/local/service/.env
COPY src /usr/local/service/src
WORKDIR /usr/local/service
RUN mvn package
COPY .env /usr/local/service/target/appassembler/bin/.env
CMD ["/usr/local/service/target/appassembler/bin/Bagabot"]