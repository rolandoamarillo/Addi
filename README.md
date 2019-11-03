# Addi Application

This is an Android Application made as a test.

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com/rolandoamarillo/Addi.git
```

## Run test
After importing the project, left click the java folder inside /app/src/test folder and click on "Run All Tests" to run unit testing
If instrumentation tests are required, left click the java folder inside /app/src/androidTest and click on "Run All Tests"

## Workers
This project was done by:
* [Rolando Amarillo](http://github.com/rolandoamarillo)

## Mocks
There are two modes to run the mocks (DYNAMIC; FIXED). 
On Dynamic, all the remote information will be valid. To change this mocks please edit MockResponseInterceptor file.
On Fixes, only specific scenarios were added as fixed on raw. To add more cases please add them to /debug/res/raw folder.
To change between modes of mocks, please edit EnvironmentModule file.