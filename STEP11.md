
# 11. Creating a Rule

OpenWhisk supports the creation of rules. Rules are the combination of a trigger and an action to invoke when that trigger condition is met. When utilizing a serverless architecture organizations will often make use of rules to handle the execution of actions, rather than executing them manually. The triggering event might be the time of day, a message published to a topic, or in the case of this secion, when a record is written to a database. 

## Defining a Trigger

```
packages:
  golden-ratio:
  	 triggers:
  	   fibonacciAdd:
  	     feed: /go-serverlesss-cloudant/changes
  	     inputs: 
  	     	 dbname: fibonaccidb
  	     	 lifecycleEvent: CREATE
    inputs:
      dbname: fibonaccidb
    actions:
      fibonacciNumber:
        function: hello-world-java.jar
        runtime: java
        main: com.example.FibonacciNumber
      calculateRatio:
        function: hello-world-java.jar
        runtime: java
        main: com.example.CalculateRatio
      cloudantDocBuilder:
        function: hello-world-java.jar
        runtime: java
        main: com.example.BuildCloudantDoc 
    sequences:
      ratio:
        actions: fibonacciNumber,calculateRatio,cloudantDocBuilder,/go-serverlesss-cloudant/write
```


<p  align="center">
	<font size="4">
 		<a href="STEP10.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>
 </font>
</p>