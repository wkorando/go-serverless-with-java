## 3. Getting Familiar with OpenWhisk Commands

Let's take a deeper look at some of the common commands you will be using when running functions on OpenWhisk.

### Executing Functions Asynchronously

So far we have been executing functions synchronously with the `--result` tag. Let's take a look at executing functions asynchronously.

1. To execute a function in asynchronous mode simply omit `--result` when invoking the function:  

	```
	ibmcloud fn action invoke helloJava --param name World
	```

   You should get a response that includes an id you can use to look up the result of the function later:

    ```
    ok: invoked /_/helloJava with id c51e11cf3bad42a39e11cf3badb2a3a3
    ```

2. Use the below command to retrieve the result of the function invocation:

    ```
    ibmcloud fn activation result [id]
    ```
    You should get a response that looks something like this:

    ```
	{
	    "greetings": "Hello World"
	}
	```

	**Note:** Functions execute in asynchronous mode by default, you can also use the tag `--blocking` explicitly invoke a function in asynchronous mode.

### Viewing Function Invocation Information

When invoking a function OpenWhisk is generating diagnostic information that can be used for tracking system usage, debugging, and other purposes.

1. You can view the invocation information of the function we executed earlier with this command:

	```
	ibmcloud fn activation get [id]
	```

	You should get a response back that looks something like this:

	```
	{
	    "namespace": "[youremail]@mail.com_dev",
	    "name": "helloJava",
	    "version": "0.0.1",
	    "subject": "[youremail]@mail.com",
	    "activationId": "c51e11cf3bad42a39e11cf3badb2a3a3",
	    "start": 1568061913141,
	    "end": 1568061913482,
	    "duration": 341,
	    "response": {
	        "status": "success",
	        "statusCode": 0,
	        "success": true,
	        "result": {
	            "greetings": "Hello! Welcome to OpenWhisk"
	        }
	    },
	    "logs": [
	        "2019-09-09T20:45:13.478869Z    stderr: Sep 09, 2019 8:45:13 PM com.example.FunctionApp main",
	        "2019-09-09T20:45:13.478917Z    stderr: INFO: invoked with params:"
	    ],
	    "annotations": [
	        {
	            "key": "path",
	            "value": "[youremail]@mail.com_dev/helloJava"
	        },
	        {
	            "key": "waitTime",
	            "value": 514
	        },
	        {
	            "key": "kind",
	            "value": "java:8"
	        },
	        {
	            "key": "timeout",
	            "value": false
	        },
	        {
	            "key": "limits",
	            "value": {
	                "concurrency": 1,
	                "logs": 10,
	                "memory": 256,
	                "timeout": 60000
	            }
	        },
	        {
	            "key": "initTime",
	            "value": 308
	        }
	    ],
	    "publish": false
	}
	```


### Viewing Function Invocation Logs

`ibmcloud fn activation get` returns the logs from an invocation, but you can also just view the logs from innvocation to make debugging a bit easier.

1. To view the logs from an invocation run the following:

	```
	ibmcloud fn activation logs [id]
	```
	You should get a return thaty looks like this:

	```
	2019-09-09T21:16:27.917303Z    stderr: Sep 09, 2019 9:16:27 PM com.example.FunctionApp main
	2019-09-09T21:16:27.917347Z    stderr: INFO: invoked with params:
	```
2. For longer running functions, you can tail the logs a function is producing with the following command:

	```
	ibmcloud fn activation poll [id]
	```

### Retrieve Most Recent Function Execution

For shorthand purposes you can use the tag `--last` in-lieu of an id to retrieve information about an activation.

```
ibmcloud fn activation [get|result|logs] --last
```

### Show Recent Function Invocations

You can view recent function invocations; id, function executed with the following:

```
ibmcloud fn activation list
```

### Show Available Functions

You can view a list of all functions available in the current namespace with the following:

```
ibmcloud fn list
```

<p  align="center">
	<font size="4">
 		<a href="STEP2.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="STEP4.md">Next >></a></td>
 </font>
</p>
