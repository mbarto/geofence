GeoFence
==================================================

**GeoFence** is a java web application that handles authorization rules, 
integrating with [GeoServer](http://www.geoserver.org) using the interface 
described in [GSIP 57](http://geoserver.org/display/GEOS/GSIP+57+-+Improving+GeoServer+authorization+framework).

How to interact with GeoFence
--------------------------------------------------
**GeoFence** provides a _GWT-based_ user interface to administer the basic objects and the authorization rules.

Furthermore, some [REST API](https://github.com/geosolutions-it/geofence/wiki/REST-API) have already been implemented that allows the administration of some ancillary data.

GeoServer will send authorization queries to GeoFence using a configurable internal protocol (by default it uses Spring remoting over HTTP).

High level arch
--------------------------------------------------
**GeoFence** is split in 3 main modules:
* The Core module contains the model, the persistence logic and the service logic
* The GUI module contains the GWT GUI used to administer the authorization rules and the related objects
* The bridge/probe module is the wrapper that converts the Rule logic into the authorization objects required by GeoServer.
This last module provides a jar file that will be plugged into GeoServer. 
The GeoFence code base includes a module that will build a GeoServer .war file ready to be integrated with GeoFence.

License
==================================================
GeoFence is free and Open Source software since it is released under the GPL V 3.0 license.

The probe for GeoServer as it implements a GeoServer Java API is released under the same license as GeoServer which is GPL V 2.0.

Download
==================================================
* [**GeoFence** .war file](http://maven.geo-solutions.it/it/geosolutions/geofence/geofence-gui-web/2.2-SNAPSHOT/geofence-gui-web-2.2-SNAPSHOT.war)
* [Latest **GeoServer 2.1.x** GeoFence integration module .jar file](http://maven.geo-solutions.it/it/geosolutions/geofence/geoserver/geofence-security21/2.2-SNAPSHOT/geofence-security21-2.2-SNAPSHOT.jar) (todo: add refs to ancillary libs)
* [Latest GeoFence **integration configuration file**](https://github.com/geosolutions-it/geofence/blob/master/src/geoserver21x/web-app/src/main/resources/geofence-geoserver.properties)


Documentation
==================================================
* [How to build GeoFence](https://github.com/geosolutions-it/geofence/wiki/Building-instructions)
* [How to configure GeoFence](https://github.com/geosolutions-it/geofence/wiki/WebApps-configuration)

Community
==================================================
* [Users forum](https://groups.google.com/forum/#!forum/geofence-users)
* [Developers forum](https://groups.google.com/forum/#!forum/geofence-developers)
