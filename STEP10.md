## 10. Connecting to Database

IBM Cloud has a catalog of services available to handle many of the most common needs of an enterprise. One of the most common requirements for organizations is the longterm persistence of business valuable informnation in a database. In this section we will walk through connecting to a Cloudant instance. Cloudant is a NoSql datastore, based on CouchDB.

## Viewing Pre-Registered Packages

1. OpenWhisk on IBM Cloud comes with several pre-installed packages. These packages can help when interacting with common services; messaging, IBM Watson, datastores. You can view these packages by running the following command:
	
	```
	ibmcloud wsk package list /whisk.system
	```

	This command returns all the packages registered under the system package `/whisk.system`. In the output you should see: `/whisk.system/websocket`, `/whisk.system/messaging`, `/whisk.system/github`, etc.. 

2. The package we are interested in is `/whisk.system/cloudant`. Pre-installed packages contain a actions, feeds, and other useful bits. You can get a condensed summary of everything within in a package with this command:

	```
	ibmcloud wsk package get --summary /whisk.system/cloudant
	```

	You will get a response back that looks something like this:

	```
	package /whisk.system/cloudant: Cloudant database service
	   (parameters: *apihost, *bluemixServiceName, dbname, host, iamApiKey, iamUrl, overwrite, password, username)
	 action /whisk.system/cloudant/delete-attachment: Delete document attachment from database
	   (parameters: attachmentname, dbname, docid, docrev, params)
	 action /whisk.system/cloudant/update-attachment: Update document attachment in database
	   (parameters: attachment, attachmentname, contenttype, dbname, docid, docrev, params)
	 action /whisk.system/cloudant/read-attachment: Read document attachment from database
	   (parameters: attachmentname, dbname, docid, params)
	 action /whisk.system/cloudant/create-attachment: Create document attachment in database
	   (parameters: attachment, attachmentname, contenttype, dbname, docid, docrev, params)
	 action /whisk.system/cloudant/read-changes-feed: Read Cloudant database changes feed (non-continuous)
	   (parameters: dbname, params)
	 action /whisk.system/cloudant/delete-query-index: Delete index from design document
	   (parameters: dbname, docid, indexname, params)
	 action /whisk.system/cloudant/delete-view: Delete view from design document
	   (parameters: dbname, docid, params, viewname)
	 action /whisk.system/cloudant/manage-bulk-documents: Create, Update, and Delete documents in bulk
	   (parameters: dbname, docs, params)
	...
	```

	The `/whisk.system/cloudant` package contains many common database operations; `update-attachment`, `read-attachment`, `create-attachment`, and so on. A key concept within serverless is offloading work on to the platform. In this case, instead of adding logic to the functions we are writing to handle database behavior, we will instead use these pre-existing functions. 

**Tip**: If you want a more detailed view of a package you can also run `ibmcloud wsk package get [PACKAGE_NAME]` like here:

```
ibmcloud wsk package get /whisk.system/cloudant
```

## Binding to a Cloudant Instance

Let's create and bind a Cloudant instance to a package so we can start persisting data. 

### Copying a Package

First we must make a copy of the `/whisk.system/cloudant` package. This isn't strictly necessary for this workshop, but if we were to bind a Cloudant instance to `/whisk.system/cloudant`, then everyone in that space would be writing to the same Cloudant instance which might not be what you want! 

1. To create a copy of a package run the following: 

	```
	ibmcloud wsk package bind /whisk.system/cloudant go-serverlesss-cloudant
	```

2. This will create a new package '/[YOUR_ACCOUNT_NAME]_dev/go-serverlesss-cloudant'. To view the newly created package run this command: 

	```
	ibmcloud wsk package list
	```

### Creating a Cloudant Service Instance

Next we will want to create our Cloudant instance. This can be done through the IBM Cloud CLI.

1. We will want to create our new Cliudant instance, be sure to replace `MY_REGION` in the command below with the current region you are working in:
	
	```
	ibmcloud resource service-instance-create cloudant-serverless cloudantnosqldb lite MY_REGION -p '{"legacyCredentials": false}' 
	```

	This will create a Cloudant instance with the name `cloudant-serverless`.

1. Next we need to create service credentials so we can connect to this datastore. To do that run the following: 

	```
	ibmcloud resource service-key creds_cloudantserverless
	```

1. Finally to bind the Cloudant instance to the package we we just created run the following command:

	```
	ibmcloud wsk service bind cloudantnosqldb /[YOUR_ACCOUNT_NAME]_dev/go-serverlesss-cloudant --instance cloudant-serverless --keyname creds_cloudantserverless
	```

	This will bind our newly created Cloudant instance to the package: `/[YOUR_ACCOUNT_NAME]_dev/go-serverlesss-cloudant`. 


## Persisting to Cloudant

With our Cloudant instance created let's connect it with some of the functions we have created in an earlier section to start persisting data. 

### Creating a New Database

1. First we will need to create a database in our cloudant instance where we will be persisting our data to. YOu can create a database with the following command: 

	```
	ibmcloud wsk action invoke --result /[YOUR_ACCOUNT_NAME]_dev/go-serverlesss-cloudant/create-database -p dbname "fibonaccidb"
	```
	This command will create the database `fibonaccidb` which we will be using to store the results of the sequence we created in the section on sequences. 
	
### Configuring a Package to Write to a Database
	
The package we created earlier from `/whisk.system/cloudant` contains the `write` action. It looks something like this:  

```
 action /[YOUR_ACCOUNT_NAME]_dev/go-serverlesss-cloudant/write: Write document in database
   (parameters: dbname, doc)
```
	
The action has two parameters, `dbname` and `doc`. The former will be the string value of the database we just created, the second will need to be in the form of a JSON message. We will need to make a few changes to the `golden-ratio` package so that we can start persisting data to `fibonaccidb`

**Tip:** We will be making several updates to `manifest.yml`, to see an expanded view of what the file should look like and where all the elements go, look at the end of this section.

1. We can set a default input value at the package level. This value will be automatically passed in everytime an action is invoked within that package. We will use this to pass in the `dbname` value for `write`. In `manifest.yml` you will want to add the following: 
	
		
	```
	  golden-ratio:
	    inputs:
	      dbname: fibonaccidb
	```
2. The action `calculateRatio` is currently returning the value `ratio`. To store this in Cloudant we will need to wrap this in an object call `doc`. This could be done directly in `calculateRatio`, but we want to our functions to be atomic in their behavior. So instead let's create a new function called `buildCloudantDoc`. Create a new Java file called `BuildCloudantDoc.java` and copy the following code into it: 

	```
	package com.example;
	
	import com.google.gson.JsonObject;
	
	public class BuildCloudantDoc {
	
		   public static JsonObject main (JsonObject args) {
		      JsonObject response = new JsonObject();
		      args.remove("dbname");
		      response.add("doc", args);
		      return response;
		   }  
	}
	```

	Note that we are also removing the value `dbname`. As mentioned above, default parameters are passed into every action invocation. We don't want to persist this value to our database, so we will remove it be fore building our response. 
	
3. Update `manifest.yml` to declare this new action:

	```
	      cloudantDocBuilder:
	        function: hello-world-java.jar
	        runtime: java
	        main: com.example.BuildCloudantDoc 
	```

4. Finally update the `ratio` sequence adding the `cloudantDocBuilder` and `/go-serverlesss-cloudant/write` actions:

	```
	     ratio:
	        actions: fibonacciNumber,calculateRatio,cloudantDocBuilder,/go-serverlesss-cloudant/write
	```
	
5. Invoke the `ratio` sequence to persist a value to the database:

	```
	ibmcloud wsk action invoke --result golden-ratio/ratio -p number 4
	```
6. We can read the newly stored value with the following command:
	
	```
	ibmcloud wsk action invoke --result  /[YOUR_ACCOUNT_NAME]_dev/go-serverlesss-cloudant/read -p dbname fibonaccidb	
	```
	
	You should be a reponse that looks similar to this:
	
	```
		SOME TEXT
	```
	
Here is an expanded view of what the complete `golden-ratio` package should look like:  

```
packages:
  golden-ratio:
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
        actions: fibonacciNumber,calculateRatio,cloudantDocBuilder,/go-serverlesss-package/write
```

<p  align="center">
	<font size="4">
 		<a href="STEP9.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="STEP11.md">Next >></a></td>
 </font>
</p>