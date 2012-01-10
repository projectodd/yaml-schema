Introduction
------------

yaml-schema provides Java-based schema-based validation facilities for YAML documents. 

Examples
--------

TBD

Data Types
----------

yaml-schema supports the following datatypes:

* string (str)
* integer (int)
* natural
* enumeration
* map
* list

Dependencies
------------

yaml-schema depends only on the snakeyaml YAML parser and the jboss-logging library for logging.

Building
--------

yaml-schema runs on Maven 3, and can be built using good old mvn install.

If you're a regular JBoss developer, see:

* http://community.jboss.org/wiki/MavenGettingStarted-Developers

Otherwise, see: 

* http://community.jboss.org/wiki/MavenGettingStarted-Users

Once your repositories are configured, simply type:

    mvn install

Tests can be executed using mvn test.
