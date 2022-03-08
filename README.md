# Cucumber-Java Skeleton

This is the simplest possible build script setup for Cucumber using Java.
There is nothing fancy like a webapp or browser testing. All this does is to show
you how to install and run Cucumber!
 

# API Automation Test BDD framework
API Automation Test is RestAssured based Cucumber framework to perform API testing. This project is useful as an example of API Testing with RestAssured and Java playing nicely together.

## Getting Started

Git:

    git clone https://github.com/diegograssato/cucumber-java-skeleton.git
    cd cucumber-java-skeleton

Subversion:

    svn checkout https://github.com/diegograssato/cucumber-java-skeleton/trunk cucumber-java-skeleton
    cd cucumber-java-skeleton

Or [download a zip](https://github.com/diegograssato/cucumber-java-skeleton/archive/main.zip) file.

## Use Maven

Open a command window and run:

    ./mvnw test

With environment:

    ./mvnw clean test -DENVIRONMENT=dev

Filter with tags:

    ./mvnw clean test -Dcucumber.filter.tags="@myTag"
    ./mvnw clean test -Dcucumber.filter.tags=""@azure and not @ignore"

This runs Cucumber features using Cucumber's JUnit 5 Platform Engine. The `Suite`
annotation on the `TestRunner` class tells JUnit to kick off Cucumber.

## Validating Json

For creating JSON schema of an API response:

Go to https://jsonschema.net/home

Paste the API response in the left side window and click on Submit button.

Your JSON schema file will be displayed on the right side window.

Copy and paste the JSON schema in the src/test/resources/contracts/<api-json-schema-name>.json file and rerun your test.

Validate your test using method `validationSchema("<api-json-schema-name>.json")`

### Reports used
- `Spark HTML Report`
- `Cucumber PDF Report`
- `Cucumber HTML Report`
