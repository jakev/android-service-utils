ServiceUtils
============
The `ServiceUtils` library is a collection of Java classes to assist with interacting directly with Android System Services using the `SystemServiceConnector` class. This class exposes a simple API to connect and invoke methods of services without the need to import private classes (such as the "android.os.ServiceManager" class) or generate stub classes to tell the IDE about API details. To achieve this, `ServiceUtils` makes use of the Reflection API.

Installing
----------
For the time being, you'll need to build the AAR and import the `ServiceUtils` library into your Android Studio project. This will be streamlined very soon.

Examples
--------
Using the `SystemServiceConnector` class is easy: create a new object `SystemServiceConnector`, passing the service name and Java class name (both of which can be found using the `adb shell service list` command):

```java
    SystemServiceConnector ssc = new SystemServiceConnector("phone",
            "com.android.internal.telephony.ITelephony");
```

Next,use the `callMethod(...)` or `callMethod()` class to invoke the desired method:

```java
    ssc.callMethod("dial", "123456789");
```

A `ConnectorException` will be thrown in error conditions. You can view the system logs to extract additional details.

For a working example of how to use the API, see the included `app/` Android application.

License
-------
The `ServiceUtils` library  is licened under the Apache License, Version 2.0. This means it is freely available for use and modification in a personal and professional capacity.
