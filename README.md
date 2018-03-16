README for ATM Engine Project (atmEngine)
==========================================
 
-------
Version
-------
The latest application version is below.

 - **1.0-SNAPSHOT**

----------------------
Synopsis
----------------------
The ATM Engine provides RESTFull services to model an ATM machine services. The application is a **standalone Java jar executable**.

----------------------
Installation
----------------------
Once the application is built, the executable jar can be run in place or placed into a location when the user wants the application to run.

The Application has only being installed and tested on a Windows 7 environment.

-----------------------------------------------
Supported Deployment/Installation Environment
------------------------------------------------
 - Microsoft Windows 7 operating system
 - Java 1.8.0_121

The Application has only being installed and tested on a Windows 7 environment. The behaviour of some of the
File operations may differ on other operating systems.

---------------------------------
Build Environment
---------------------------------
 - Microsoft Windows 7 operating system
 - Java 1.8.0_121
 - Maven 3.2.5

The Application has only being built and tested on a Windows 7 environment.

---------------------------------
Building the application
---------------------------------
To build the application the user will need access to a **Maven repository** that has holds the 3rd party 
dependencies required.

To build the application go the the directory where the application's **pom.xml** is located @ ~\atmEngine\ 
and run the maven build command below. The application's executable jar will be produced in the target directory. 

 - **mvn clean install**

-------------------------------------
Running ATM Engine
-------------------------------------
To run the ATM Engine application you can use the Java virtual machine to run the executable jar as below. 

 - **java -jar atmEngine-1.0-SNAPSHOT.jar**

The will use the default Web container port of 8080 is used by the application, so the URL for the REST interface must use that port.
 
----------------------
Usage
----------------------
There are 3 methods on the REST interface provided by the ATM Engine.

 - **getBalance** Get the current balance for a user account
 - **getMaximumWithdrawalBalance** Get the current balance total funds available to the user account
 - **makeAccountWithdrawal** Make a withdrawal from the user account

The service methods can be invoked by hitting the relevant URLs, examples are given below showing the parameters required.

http://localhost:8080/getBalance?accountNo=123456789&accountPin=1234
http://localhost:8080/getMaximumWithdrawalBalance?accountNo=123456789&accountPin=1234
http://localhost:8080/makeAccountWithdrawal?accountNo=123456789&accountPin=1234&withdrawalAmount=5

----------------------------------
3rd Party Jars required for Build 
----------------------------------
TODO

-------------------------------------------
Tests / Business requirement Validation
-------------------------------------------
ATM Engine Service layer methods have a high % coverage.

-------------------------
Performance & Profiling 
-------------------------
Not performed.

----------------------------------
License
----------------------------------
This is freeware, no license required.

