# cdnken

Distributed storage for blob data with a single database.

### Why?

Project goal is uniting multiple storage servers into single object database with one endpoint.

### Quick overview

* master server - http server with database connection who redirects requests to storage servers
* storage daemon - daemon server who manipulates on directory of objects
* storage server - any http server (eg. nginx) who serves objects from demon directory
