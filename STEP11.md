
# 11. Creating a Rule

OpenWhisk supports the concept of rules. Rules are the combination of a trigger and an action to invoke when that trigger condition is met. Rules are a central concept when developing a serverless architecture by allowing actions andd sequences to be executed automatically. In this section we will look at how to create a trigger and combine it together with an action to create a rule. 

## Defining a Trigger

Triggers allow for the creation of an event-driven architecture. Openwhisk is very flexible in how a triggering condition can be defined, it could be some external like time of day, or responding to changes in system state like a message being published to a queue, or as we will look at in this section a record being written to our Cloudant database. 

1. In the previous section we created the `go-serverless-cloudant`, within that package there is the feed `changes`:

	```
	 feed   /[YOURACCOUNTNAME]_dev/go-serverless-cloudant/changes: Database change feed
   (parameters: dbname, filter, iamApiKey, iamUrl, query_params)
   ```
	
	The `changes` feed takes several parameters. `dbname` is already being supplied by the default parameter we setup in the previous step, along with `iamApiKey`, `iamUrl` which are supplied by the credntials we provided. `filter` and `query_params` can be supplied to provide more fine grained control over what events are pulled from the feed. Additionally feeds take a `lifecycleEvent` parameter, which has six values `CREATE`, `READ`, `UPDATE`, `DELETE`, `PAUSE`, or `UNPAUSE`. We will use the `CREATE` lifecycle. 
	
1. Update the `manifest.yml` with the following, `triggers` will be at the same identation level as `actions`:

	```java
	  	 triggers:
	  	   fibonacciAdd: #Name of trrigger
	  	     feed: /go-serverlesss-cloudant/changes #fully qualified name of feed
	  	     inputs: 
	  	     	 lifecycleEvent: CREATE #Lifecycle event to check for
 	```
 	
## Defining a Rule

Rules are a combination of a trigger and an action to run. We defined the trigger, now we need to create an action to be executed with the trigger is tripped. 

1. Go to the toolchain and open the **Orion Web IDE** to create a new Java file called `ReadCloudantDoc.java` under the `com/example` package. we will simply read the raw value that was added to the database and write it to the log. Copy the following code into it:  

	```java
	package com.example;
	
	
	import java.util.logging.Logger;
	
	import com.google.gson.JsonObject;
	
	public class ReadCloudantDoc {
		   protected static final Logger logger = Logger.getLogger("basic");
	
		   public static JsonObject main (JsonObject args) {
		      System.out.println("Newly written value: " + args.toString());
		      JsonObject response = new JsonObject();
		      response.addProperty("success", true);
		      return response;
		   }  
	}
	```
2. Create the new action  `readCloudantDoc` under `golden-ratio` that executes the Java class we just created:

	```yaml
      readCloudantDoc:
        function: hello-world-java.jar
        runtime: java
        main: com.example.ReadCloudantDoc
	```
3. Finally we will define the rule with the following yaml. `rules` should also be at the same identation level as `actions`: 

	```yaml
	 rules:
      readFibonacciRatios:
        trigger: fibonacciAdd
        action: readCloudantDoc
	```
	
4. [Commit and Sync Changes with your GitLab Repo](GIT.md)

## Testing the Rule 

5. Once the build process has completed execute again the ratio sequence:

	```
	ibmcloud fn action invoke --result golden-ratio/ratio -p number 4
	```
6. Next view all the recent action executions with the following:

	```
	ibmcloud fn activation list --limit 6
	```
	
	The first item in the output should look something like this this: 
	
	```
Datetime            Activation ID                    Kind      Start Duration   Status  Entity
2019-10-30 12:43:42 0eca8efbb51a40108a8efbb51ad010aa java      cold  344ms      success [ACCOUNTNAME]_dev/readCloudantDoc:0.0.6
2019-10-30 12:43:41 49ecdaf0008e48d2acdaf0008ee8d268 unknown   warm  0s         success [ACCOUNTNAME]_dev/fibonacciAdd:0.0.2
2019-10-30 12:43:41 ae1cc785c8944e119cc785c8948e1139 nodejs:10 cold  442ms      success [ACCOUNTNAME]_dev/write:0.0.180
2019-10-30 12:43:40 791714e7533a47fb9714e7533a37fbab java      cold  317ms      success [ACCOUNTNAME]_dev/cloudantDocBuilder:0.0.10
2019-10-30 12:43:39 d006f14575494e2786f1457549ce2722 java      cold  349ms      success [ACCOUNTNAME]_dev/calculateRatio:0.0.13
2019-10-30 12:43:39 e7178dcd441f462e978dcd441f862ec4 java      cold  377ms      success [ACCOUNTNAME]_dev/fibonacciNumber:0.0.13
	```
	
	Copy the `Activation ID` from the first row in your output. 
	
7.  To view the log statement we wrote with the `readCloudantDoc` action run this command:

	```
	ibmcloud fn activation logs RETURNED_ID
	```
	
	You should get a return that looks something like this:
	
	```
	2019-10-30T11:43:42.343318Z    stdout: Newly written value: {"changes":[{"rev":"1-79633dd03e0a332454ba6bfa8e0805f3"}],"dbname":"fibonaccidb","id":"adff3e188b184ef7adb25ae987912d3b","seq":"4-g1AAAAYoeJyt1M1NwzAUB3DTIiFOdAO4gpTizzg50Q1gA7D9XJWqTRBtzrABbAAbwAawAWxAN4ANSoIrGlMVJW0vjhTJv7_fe3EGCKFWrwnoELRJr20HNG_rYWBMkI2CUZqNewElbTNIM1DJuJ3Y8SDf0lBI702n036vqRrD_MUONtpEjNeB_FhWJVa38lXvz5LRT7KwXOEuBbSbJWC7l4mFQj76lQmtRB8U9PGM3nI0gDFa18L8quJK0biIPvGq0qGQIYO_VdUck-4U8qknAzG8y9hyudokzgr53JMZpySiYu1JXBR06tEYOMlPvfzQ1Rp9Vcg3nqw0FvS_dlRqdLKdr-g2f-T43VwP41BKFa3ZbKffO_2h9JFAKIhYOHvdhjv-0fFPpdZIZkDxNZvu9Genv8wvl5Q0BrB1vJUH8-rS3-a1Uak4XfxrrDSYd6d_lHQWCYvJZgYzcfxn6TZYQjGzGxnMl9NLN4LGVOJQlfX-N2uR6kQ"}
	```
## The Manifest YAML
<details>
<summary>Here is an expanded view of what the complete <code>manifest.yml</code> file should look like:</summary>

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
      readCloudantDoc:
        function: hello-world-java.jar
        runtime: java
        main: com.example.ReadCloudantDoc 
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
    triggers:
      fibonacciAdd:
        feed: /[YOURACCOUNTNAME]_dev/go-serverless-cloudant/changes
        inputs: 
          dbname: fibonaccidb
          lifecycleEvent: CREATE
    rules:
      readFibonacciRatios:
        trigger: fibonacciAdd
        action: readCloudantDoc
```
</details>


<p  align="center">
	<font size="4">
 		<a href="STEP10.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>
 </font>
</p>