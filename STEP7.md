## 7. Viewing the Functions Dashboard

IBM Cloud provides a convenient dashboard for viewing your functions. You can access this dashboard here: [https://cloud.ibm.com/functions/actions](https://cloud.ibm.com/functions/actions). It should list the following actions:

![](./images/dashboard-1.png)

These actions have been created via the CLI at the start of this lab. They have in the previous section been updated via the delivery pipline. The latter also defined an API that can be further explored in the API section of the dashboard. We'll dive further into that topic in the API Gateway section of this lab. 

1. The serverless functions functions `helloJava` and `webHello` are both written in Java. Hence, the code cannot be viewed and changed via the dashboard. They can be invoked though. 

	Invoke the function `helloJava` by clicking the action and then click Invoke.

	![](./images/dashboard-2.png)

	As you can see the result is similar to when the function is invoked via the command line. 

2. Next, change the Input by clicking 'Change input' and the input to
	
	```json
	{
		"name": "your name here.."
	}
	```
	Change the value of 'name' to your own name, or something you like and click Apply. Click Invoke to invoke this function with the changed input. The result should be 
	```json
	{
		"greetings": "Hello your name here..."
	}
	```
	Finally, return to the actions dashboard. 

### Create a new action via the Cloud Functions dashboard (OPTIONAL)

If you want to explore what the possibilities are when creating cloud functions via the UI, click the 'Create' button. In the next page, you can either create new triggers and/or sequences but also new actions via a quick template or from scratch. Select the Quickstart templates to continue and choose Hello World. You should see a screen similar to 

![](./images/dashboard-3.png)

Now select a favourite language using the dropdown (1). We've chosen for NodeJS 10 in the screenshot above. Click Deploy (2) to create the new action written in NodeJS. It outputs practically the same as our `helloJava` function. When no input is given, the function returns 
```json
"greeting": "Hello stranger!"
```
When there is input, the result is the same as for the `helloJava`. Please see for yourself by invoking the `hello-world` action with some input as well. Next, let's add more value to our serverless functions by defining sequences.


<p  align="center">
	<font size="4">
 		<a href="STEP6.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="STEP8.md">Next >></a></td>
 </font>
</p>
