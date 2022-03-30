# Installation details 


## Pre-requisites Software
* Java Version 1.8
* IntelliJi Editor 
* GIT
* Browser
* My SQL Server
* Any SQL work Server work Bench
* Postman

## Code Repo Download
``
$ cd ${your_code_checkout_dir}
$ git clone --single-branch --branch master https://github.com/kaulkartik/playerinfo.git
``

## Databse Setup 
* download the my sql server from https://dev.mysql.com/downloads/mysql/
``
$ export MYSQL_HOME=/usr/local/mysql 
$ alias start_mysql='sudo $MYSQL_HOME/bin/mysqld_safe &'
$ alias stop_mysql='sudo $MYSQL_HOME/bin/mysqladmin shutdown'
$ start_mysql
$ Intialise the MYSQL on default port 3306
$ In the work bench execute the following commands in /src/main/resources/project.sql in serial way
$ Use My SQL work bench and to connect  
`` 

## Editor Run 
* import project as existing git project in Intelliji
* Run Application PlayerinfoApplication.java 

## Accesing Endpoints
* You can see swagger page : http://localhost:8080/swagger-ui/index.html#/
* Use post Postman to send and test request

## Caution for GET/Post request
#### Valid Date format for POST : YYYY-MM-DD HH:MM:SS | 2022-03-31 02:44:46
#### Valid Date format for GET : YYYY-MM-DD





