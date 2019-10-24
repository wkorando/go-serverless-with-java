
# 11. Creating a Rule

OpenWhisk supports the creation of rules. Rules are the combination of a trigger and an action to invoke when that trigger condition is met. When utilizing a serverless architecture organizations will often make use of rules to handle the execution of actions, rather than executing them manually. The triggering event might be the time of day, a message published to a topic, or in the case of this secion, when a record is written to a database. 

---
THIS STEP IS CURRENTLY UNDER CONSTRUCTION CHECK BACK LATER

---



## Defining a Trigger

1. Feed

	```
	 feed   /whisk.system/cloudant/changes: Database change feed
   (parameters: dbname, filter, iamApiKey, iamUrl, query_params)
   ```

1. Define the trigger in `manifest.yml`

	```java
	  	 triggers:
	  	   fibonacciAdd:
	  	     feed: /go-serverlesss-cloudant/changes
	  	     inputs: 
	  	     	 dbname: fibonaccidb
	  	     	 lifecycleEvent: CREATE
 	```

## Defining a Rule

Rules are a combination of a trigger and an action to run. We defined the trigger, now we need to create an action to be executed with the trigger is tripped. 

1. Go to the toolchain and open the **Orion Web IDE** to create a new Java file called `ReadCloudantDoc.java` under the `com/example` package. Copy the following code into it:  

	```java
	package package com.example;
	
	
	import java.util.logging.Logger;
	
	import com.google.gson.JsonObject;
	
	public class ReadCloudantDoc {
		   protected static final Logger logger = Logger.getLogger("basic");
	
		   public static JsonObject main (JsonObject args) {
		      System.out.println("Newly written value: " + args.get("doc").toString());
		      JsonObject response = new JsonObject();
		      response.addProperty("success", true);
		      return response;
		   }  
	}
	```
2. Create a new action `readCloudantDoc` that executes the Java class we just created:

	```yaml
      readCloudantDoc:
        function: hello-world-java.jar
        runtime: java
        main: com.example.ReadCloudantDoc
	```
3. 


	```yaml
      readFibonacciRatios:
        trigger: fibonacciAdd
        action: readCloudantDoc
	```
```
# wskdeploy manifest file

packages:
  default:
    version: 1.0
    license: Apache-2.0
    actions:
      helloJava:
        function: hello-world-java.jar
        runtime: java
        main: com.example.FunctionApp
      webHello:
        function: hello-world-java.jar
        runtime: java
        main: com.example.WebHello      
        web-export: true
  golden-ratio:
    inputs: #Define default parameters to be passed into all action invocations in this packge
      dbname: fibonaccidb
  	 triggers:
  	   fibonacciAdd:
  	     feed: /go-serverlesss-cloudant/changes
  	     inputs: 
  	     	 dbname: fibonaccidb
  	     	 lifecycleEvent: CREATE
    actions:
      fibonacciNumber:
        function: hello-world-java.jar
        runtime: java
        main: com.example.FibonacciNumber
      calculateRatio:
        function: hello-world-java.jar
        runtime: java
        main: com.example.CalculateRatio
      calculateRatioWeb:
        function: hello-world-java.jar
        runtime: java
        main: com.example.CalculateRatioWeb
      cloudantDocBuilder:
        function: hello-world-java.jar
        runtime: java
        main: com.example.BuildCloudantDoc  
    sequences:
      ratio:
        actions: fibonacciNumber, calculateRatio, cloudantDocBuilder, go-serverless-cloudant/write
        web: true
      ratioWeb:
        actions: fibonacciNumber, calculateRatioWeb
        web: true
    apis:
      ratioAPI: #Endpoint ID
        api: #API Basepath
          ratio: #Endpoint Path
            ratio: #Function Reference
              method: GET
              response: json
```


<p  align="center">
	<font size="4">
 		<a href="STEP10.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>
 </font>
</p>