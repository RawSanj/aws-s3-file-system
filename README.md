# aws-s3-file-system
### AWS S3 as File System Application - Spring Cloud AWS - Spring  Boot - Thymeleaf

This is simple Spring Boot Application to upload/delete/search files in AWS S3 bucket. 
It uses Thymeleaf view tech and Bootstrap for responsive UI.

It also provides a REST api for creating Single Page Application using frameworks like Angular or React.

### Tech

**aws-s3-file-system** uses a number of open source projects:

* [Spring Boot] - An opinionated framework for building production-ready Spring applications. It favors convention over configuration and is designed to get you up and running as quickly as possible.
* [Spring Cloud AWS] - Spring Cloud for Amazon Web Services, eases the integration with hosted Amazon Web Services. It offers a convenient way to interact with AWS provided services using well-known Spring idioms and APIs.
* [Thymeleaf] - Thymeleaf is a modern server-side Java template engine for both web and standalone environments.
* [Bootswatch] - A Boostrap theme for great UI boilerplate for modern web apps.

### Installation and Configuration


```sh
$ git clone https://github.com/RawSanj/aws-s3-file-system.git

$ cd aws-s3-file-system
```

Configuration:
  - Create a S3 bucket in Amazon Web Services.
  - Create an IAM user with Programmatic access (i.e. enable access key ID and secret access key for the AWS API) and attach only AWS AmazonS3FullAccess policy.
  - Add the above noted AWS Access key, Secret key and S3 Bucket name in `/src/main/resources/application.properties` file.


Run the application:
```sh
$ mvn clean package

$ mvn spring-boot:run
```

### Tools

The following tools are used to create this project :

* Spring Tool Suite
* Maven
* Google Chrome
* Git

License
----

Apache License 2.0

Copyright (c) 2017 Sanjay Rawat

[//]: #

   [Spring Boot]: <https://projects.spring.io/spring-boot/>
   [Spring Cloud AWS]:<https://cloud.spring.io/spring-cloud-aws/>
   [Thymeleaf]: <http://www.thymeleaf.org/>
   [Bootswatch]: <https://bootswatch.com/paper/>
