## 10. Connecting to a Database

IBM Cloud has a catalog of services available to handle many of the most common needs of an enterprise. One of the most common requirements for organizations is the longterm persistence of business valuable information in a database. In this section we will walk through connecting to a Cloudant instance. Cloudant is a NoSql datastore, based on CouchDB.

## Viewing Pre-Registered Packages

1. OpenWhisk on IBM Cloud comes with several pre-installed packages. These packages can help when interacting with common services; messaging, IBM Watson, datastores. You can view these packages by running the following command:
	
	```
	ibmcloud fn package list /whisk.system
	```

	This command returns all the packages registered under the system package `/whisk.system`. In the output you should see: `/whisk.system/websocket`, `/whisk.system/messaging`, `/whisk.system/github`, etc.. 

2. The package we are interested in is `/whisk.system/cloudant`. Pre-installed packages contain a actions, feeds, and other useful bits. You can get a condensed summary of everything within in a package with this command:

	```
	ibmcloud fn package get --summary /whisk.system/cloudant
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

	---
	**Tip**: If you want a more detailed view of a package you can also run `ibmcloud fn package get [PACKAGE_NAME]` like here:

	---

	```
	ibmcloud fn package get /whisk.system/cloudant
	```

## Binding to a Cloudant Instance

Let's create and bind a Cloudant instance to a package so we can start persisting data. 

### Copying a Package

First we must make a copy of the `/whisk.system/cloudant` package. This isn't strictly necessary for this workshop, but if we were to bind a Cloudant instance to `/whisk.system/cloudant`, then everyone in that space would be writing to the same Cloudant instance which might not be what you want! 

1. To create a copy of a package run the following: 

	```
	ibmcloud fn package bind /whisk.system/cloudant go-serverless-cloudant
	```

2. This will create a new package `/[YOUR_ACCOUNT_NAME]_dev/go-serverless-cloudant`. To view the newly created package run this command: 

	```
	ibmcloud fn package list
	```

### Creating a Cloudant Service Instance

Next we will want to create our Cloudant instance. This can be done through the IBM Cloud CLI.

1. First, make sure you're linked to your default resource group by executing

	```
	ibmcloud target -g Default
	```

	This is needed because our database will be created as resource in IBM Cloud, and resources are linked to a resource group.

1. When creating our new Cloudant instance, be sure to replace `MY_REGION` in the command below with the current region you are working in:
	
	```
	ibmcloud resource service-instance-create cloudant-serverless cloudantnosqldb lite MY_REGION -p '{"legacyCredentials": false}' 
	```

	This will create a Cloudant instance with the name `cloudant-serverless`.

1. Next, we need to create service credentials so we can connect to this datastore. To do that, run the following: 

	```
	ibmcloud resource service-key-create creds_cloudantserverless Manager --instance-name cloudant-serverless
	```

	The key `creds_cloudantserverless` gives Manager rights to the service instance `cloudant-serverless`.


## Persisting to Cloudant

With our Cloudant instance created, let's connect it with some of the functions we have created in an earlier section to start persisting data. 
	
### Configuring a Package to Write to a Database
	
The package we created earlier from `/whisk.system/cloudant` contains the `write` action. It looks something like this:  

```
 action go-serverless-cloudant/write: Write document in database
   (parameters: dbname, doc)
```
	
The action has two parameters, `dbname` and `doc`. The former will be the string value of the database that will be created later in this section. The second parameter will need to be in the form of a JSON message. We will need to make a few changes to the `golden-ratio` package so that we can start persisting data to the database.

---
**Tip:** We will be making several updates to `manifest.yml`. To see an expanded view of what the file should look like, and where all the elements go, look at the end of this section.

---

1. We can set a default input value at the package level. This value will be automatically passed in everytime an action is invoked within that package. We will use this to pass in the `dbname` value for `write`. In `manifest.yml` you will want to add the following: 
	
		
	```yaml
	  golden-ratio:
	    inputs:
	      dbname: fibonaccidb
	```

	and after the `apis` section of the `golden-ratio` package:

	```yaml
	  go-serverless-cloudant:
	    inputs:
	      dbname: fibonaccidb
	```

	So both the `golden-ratio` and the `go-serverless-cloudant` packages will have the default input parameter `dbname`.
	
2. The action `calculateRatio` is currently returning the value `ratio`. To store this in Cloudant we will need to wrap this in an object call `doc`. This could be done directly in `calculateRatio`, but we want to our functions to be atomic in their behavior. So instead let's create a new function called `buildCloudantDoc`. Go to the toolchain and open the **Orion Web IDE** to create a new Java file called `BuildCloudantDoc.java`. Copy the following code into it: 

	```java
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
	
3. Next, update the `manifest.yml` to declare this new action:

	```yaml
	      cloudantDocBuilder:
	        function: hello-world-java.jar
	        runtime: java
	        main: com.example.BuildCloudantDoc 
	```

4. Finally, update the `ratio` sequence by adding the `cloudantDocBuilder` and `go-serverless-cloudant/write` actions:

	```yaml
	     ratio:
	        actions: fibonacciNumber, calculateRatio, cloudantDocBuilder, go-serverless-cloudant/write
	```

5. Commit and push these changes via the Web IDE to trigger the deployment pipeline. Check the sequences section of this workshop if you're not sure anymore how to do this.

6. Once all stages in the pipeline successfully completed, we can bind the security credentials -- that we created at the start of this section -- to the `go-serverless-cloudant` package. For this, run the following command:

	```
	ibmcloud fn service bind cloudantnosqldb go-serverless-cloudant --instance cloudant-serverless --keyname creds_cloudantserverless
	```

	This will bind our Cloudant instance with credentials `creds_cloudantserverless` to the package: `go-serverless-cloudant`. 

### Creating a New Database

1. Now that we've bound the package `go-serverless-cloudant` to our Cloudant instance, we can invoke the `create-database` action to create the database where we will be persisting our data to. For this, run the following command: 

	```
	ibmcloud fn action invoke --result go-serverless-cloudant/create-database -p dbname "fibonaccidb"
	```
	
	This command creates the database `fibonaccidb` and should return output similar to:

	```json
	{
       "ok": true
	}
	```

### Testing the updated Sequence

5. Everything should now be in place to test the updated `ration` sequence. The sequence will persist the calculated ratio to the database. For this, run the following command:

	```
	ibmcloud fn action invoke --result golden-ratio/ratio -p number 4
	```

	which returns a JSON message similar to

	```json
	{
       "id": "03833e0253a495558a456c9c49f4367c",
       "ok": true,
       "rev": "1-681f2175bf5f993643f6d4e95a96a1e9"
	}
	```
	
	Take a note of the `id` in the return JSON message as we will need this document id in the next step.
6. To read the newly stored value from the datanase, replace &lt;DOCID&gt; in the command below with the document id from the previous step and execute it.
	
	```
	ibmcloud fn action invoke --result  go-serverless-cloudant/read -p dbname fibonaccidb -p id <DOCID>
	```
	
	You should see a reponse that looks similar to this:
	
	```json
	{
      "_id": "03833e0253a495558a456c9c49f4367c",
      "_rev": "1-681f2175bf5f993643f6d4e95a96a1e9",
      "ratio": 1.5
    }
	```

## The Manifest YAML

Here is an expanded view of what the complete `manifest.yml` file should look like:  

```yaml
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
  go-serverless-cloudant:
    inputs:
      dbname: fibonaccidb    
```

<p  align="center">
	<font size="4">
 		<a href="STEP9.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="STEP11.md">Next >></a></td>
 </font>
</p>