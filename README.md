presto-hazelcast
================


[PrestoDb](http://prestodb.io/) connector for [Hazelcast](http://hazelcast.org/).

This is an experiment in using PrestoDb to run SQL queries on top of Hazelcast. It starts an embedded Hazelcast instance with an IMap and some example data. The connector is used to query maps as if they were tables. Objects in maps are treated as rows.

Usage
================

1. Start the server using gradle (2+). This will start the server on port 8383:

 - `gradle runServer`
 
2. Download presto [client](http://prestodb.io/docs/current/installation/cli.html) and run it using:

 - `./presto --server localhost:8383 --catalog hazelcast -schema maps` 

3. Run some SQL queries

 - `SHOW TABLES;` 
 - `SELECT active, SUM(sales) FROM employees GROUP BY active;`


Config
================

Config is in src/main/resources

