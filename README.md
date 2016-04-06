Nucleus Event
================

This is the Event handler for Project Nucleus. 

This project contains just one main verticle which is responsible for listening for event address on message bus. 

DONE
----
* Configured listener
* Provided a initializer and finalizer mechanism for components to initialize and clean up themselves
* Created a data source registry and register it as component for initialization and finalization
* Provided Hikari connection pool from data source registry
* Processor layer is created which is going to take over the message processing from main verticle once message is read
* Logging and app configuration
* Decode session token and retrieve userID and other details


TODO
----
* Currently email is supported for user create. Add support for rest of the applicable events
* Support for delete events


To understand build related stuff, take a look at **BUILD_README.md**.


