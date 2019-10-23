# 2. Building and Deploying a Serverless Function

Let's build and deploy our own Java serverless function.

1. Build and jar the Java application:

	```
	./mvnw package
	```
2. Deploy the function to IBM Cloud:

	```
	ibmcloud fn action create helloJava target/hello-world-java.jar --main com.example.FunctionApp
	```
3. Execute the function:

	```
	ibmcloud fn action invoke --result helloJava --param name World
	```

	You should see:

	```json
	{
	    "greetings": "Hello World"
	}
	```

	`--result` means just show the results. Omit that, and see what you get back :)
This also adds the `--blocking` flag, discussed in the next section.

<p  align="center">
	<font size="4">
 		<a href="STEP1.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="STEP3.md">Next >></a></td>
 </font>
</p>