presto-hazelcast
================


[PrestoDb](http://prestodb.io/) connector for [Hazelcast](http://hazelcast.org/).

Presto is an open source distributed SQL query engine for running interactive analytic queries against data sources of all sizes ranging from gigabytes to petabytes. 

This is an **experiment** in using PrestoDb to run SQL queries on top of Hazelcast. It starts a PrestoDb server and an embedded Hazelcast instance. The connector is used to query maps as if they were tables. Objects in maps are treated as rows.

Usage
================

1. Start the server using gradle (2+). This will start the server on port 8383:

 - `gradle runServer`
 
2. Download presto [client](http://prestodb.io/docs/current/installation/cli.html) and run it using:

 - `./presto --server localhost:8383 --catalog hazelcast --schema maps` 

3. Run some SQL queries

 - `SHOW TABLES;` 
 - `SELECT active, SUM(sales) FROM employees GROUP BY active;`
 - Joins:
 - `SELECT departments.name, SUM(sales) FROM departments INNER JOIN employees ON id = department GROUP BY departments.name;`

The server comes with some example data in table *emplyees*.
You can connect to the embedded Hazelcast instance (port 5701) using a client and put some more data in.

Config
================

Config is in src/main/resources


Run from IDE
============

Main class: main.HazelcastPresto  
Wroking dir: src/main/resources  
JVM Args: -ea -Xmx1G -Dconfig=config.properties -Dlog.levels-file=log.properties  

The code for the connector itself is in the *plugin* package.

TODOs
=====

Lots of things to improve:
 - use map key as an id
 - make distributed and partiton aware
 - result paging
 - make configurable
 - ...
 
