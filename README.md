# How to use:

## On a local machine:

* Runs ALL tests with clean old tests results: `mvn clean test`
* Runs ALL tests without clean old tests results: `mvn test`
* **Cleans old tests results**: `mvn clean`
* **Tests**(*without clean old tests results*):
    * **API**:
        * Runs all API tests: `mvn -Dtest=io.thrive.fs.api.tests.*Test test`
        * [Positive registration scenario with a referral code](io.thrive.fs.api.tests.ApiRegistrationTest.registrationScenarioWithReferCodeTest) `mvn -Dtest=io.thrive.fs.api.tests.ApiRegistrationTest#registrationWithReferCodeScenarioTest test`
        * [Positive registration scenario without a referral code](io.thrive.fs.api.tests.ApiRegistrationTest.registrationScenarioWithoutReferCodeTest) `mvn -Dtest=io.thrive.fs.api.tests.ApiRegistrationTest#registrationWithoutReferCodeScenarioTest test`
    * **UI**:
        * Runs all UI tests: `mvn -Dtest=io.thrive.fs.ui.tests.HappyFlowRegisteringNewUserTest test`
* Generate allure project:
    * `mvn allure:report` - generate Allure report
    * [view the report](target/site/allure-maven-plugin/index.html)
    * `mvn allure:serve` - generate Allure report and opens it in the default browser

## CI/CD:

Each commit initiates the launch of tests, packs the folder with reports into an archive and transfers it to artifacts

To view the test report, you must:

1. Download and unzip the artifact [reports.zip](https://ci.appveyor.com/api/buildjobs/9cq9h5iauk4ij8fy/artifacts/reports.zip).

2. View a report from an unzipped artifact.

    1. When using it for the first time
        1. In the firefox browser, enter 'about:config' in the address bar
        2. After taking the risk and clicking ***'show all'***, set ***security.fileuri.strict_origin_policy*** to ***false***
    2. After these settings, you can open ***index.html*** using the firefox browser