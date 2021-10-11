> **Summary:** A simple demonstration of work units

## What's this? ##

The beginnings of using work units within your Interlok configurations.
A work unit contains a service-list.  That is to say you may have a complex and/or often used set of services that you find yourself copying into multiple installations of Interlok and you now want to make that list of services more modular.

The work unit allows you to black box that process making it easier for the next person to re-use your process (set of services).

## Testing me ##

The first thing is to build your work-unit.

Work unit's are simply a service-list configuration and any supporting files your services require bundled into a jar file. 

### Configuring your service-list ###

That's it, it's just a service list.  An example;

```xml
<service-collection class="service-list">
	<services>
		<service class="log-message-service">
			<unique-id>log-message-service</unique-id>
			<log-level>DEBUG</log-level>
		</service>
	</services>
</service-collection>
```

Sometimes your service-list will require additional files; for example if you're configuring a transform then you'll need the XSL file.  Sometimes the additional resource required will be remote, perhaps you're serving a transform over http.  But other times you want the resource locally packaged with your configuration.  In this case your work unit jar file will contain your service-list configuration and another supporting file like an XSL transform named "my-transform.xsl".  In these cases it's important that in your work unit's service list you refer to the packaged resource file like this using the __workunit:__ url handler;

```xml
<service-collection class="service-list">
	<services>
		<xml-transform-service>
			<unique-id>transform-service</unique-id>
			<url>workunit:my-transform.xsl</url>
		</xml-transform-service>
	</services>
</service-collection>
``` 

### Work Unit Service ###

Simply use the work-unit-service to execute your jar'd work unit.

```xml
<work-unit-service>
   <unique-id>elegant-wozniak</unique-id>
   <work-unit-name>MyWorkUnit</work-unit-name>
</work-unit-service>
```

### Dynamic Execution ###

Once you have your work unit Jar file and it's dropped into your Interlok lib directory (make sure your re-start Interlok) you can then edit your main configuration adding a new service.  The service you'll choose is the __dynamic-service-executor (with url)__. 

Assuming your work unit jar file (name is not important) contains a service-list configuration named my-service.xml at the root of the jar, then you would configure your dynamic service executor with the following URL;
```xml
<dynamic-service-executor>
  <unique-id>dynamic-service</unique-id>
  <service-extractor class="dynamic-service-from-url">
    <url>workunit:my-service.xml</url>
  </service-extractor>
</dynamic-service-executor>
```

Now imagine your work unit jar file (again, name of the jar file is unimportant) has a service-list configuration burried a couple of levels deep in a directory structure like this; __/some/directory/__ then you would configure your dynamic service like this;
```xml
<dynamic-service-executor>
  <unique-id>dynamic-service</unique-id>
  <service-extractor class="dynamic-service-from-url">
    <url>workunit:some/directory/my-service.xml</url>
  </service-extractor>
</dynamic-service-executor>
```

### Work unit service ###

The second way to configure a work unit inside your Interlok configuration is to use a work unit service.
Package your work-unit as shown above and if you have a service-list file packed in that jar work unit jar file named __my-services.xml__, then inside your main Interlok configuration you can refer to your work unit's service list simply like this;
```xml
<work-unit-service>
  <unique-id>elegant-wozniak</unique-id>
  <work-unit-name>my-services</work-unit-name>
</work-unit-service>
```
