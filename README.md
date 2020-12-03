## Overview
This is for uploading and downloading file to AWS S3 bucket.

## Getting Started - Prerequisites
* Git Latest
* Java 11
* Maven 3.6.0

## How to run Server Locally
To build, execute:

    ./mvn clean build

To run the application with the local (development) profile, execute:

    ./mvn spring-boot:run

To run the application with another profile, execute:

    ./mvn run -Dspring.profiles.active=<profile_name>

Remember to run tests before pushing your code changes

    ./mvn clean test
    
To Launch the application in debug mode (for Unix and Linux based machines) 
    
    sh bin/launch-application.sh    
