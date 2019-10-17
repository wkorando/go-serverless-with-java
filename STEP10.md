## 10. Connecting to Database

IBM Cloud has a catalog of services available to handle many of the most common needs of an enterprise. One of the most common requirements for organizations is the longterm persistence of business valuable informnation in a database. In this section we will walk through connecting to a Cloudant instance. Cloudant is a NoSql datastore, based on CouchDB.

## Viewing Available Packages

OpenWhisk on IBM Cloud comes with several pre-registered packages for interacting with services. To view these packages by running the following command:

```
ibmcloud wsk package list /whisk.system
```

This command returns all the packages registered under the system package `/whisk.system`. In the output you should see values like: `/whisk.system/websocket`, `/whisk.system/messaging`, and `/whisk.system/github`. The one we are interested in though is `/whisk.system/cloudant`. These packages contain a number actions, feeds, and other useful bits. You can get a condensed summary of everything contained in a package with this command:

```
ibmcloud wsk package get --summary  /whisk.system/cloudant
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

The `/whisk.system/cloudant` package contains many common database operations; `update-attachment`, `read-attachment`, `create-attachment`, and so on. A key concept within serverless is offloading more work on to the platform. In this case, instead of adding logic to the functions we are writing to handle database behavior, we will instead use these pre-existing functions. 

**Tip**: If you want a more detailed view of a package you can also run `ibmcloud wsk package get` like here:

```
ibmcloud wsk package get /whisk.system/cloudant
```

## Binding to a Cloudant Instance

Let's create and bind a Cloudant instance to a package so we can start persisting data. 

### Copying a Package

First we must make a copy of the `/whisk.system/cloudant` package. This isn't strictly necessary for this workshop, but if we were to bind the Cloudant instance to the `/whisk.system/cloudant` package, then everyone in that space would be writing to the same Cloudant instance which might not be what you want! 

To create a copy of a package run the following: 

```
ibmcloud wsk package bind /whisk.system/cloudant go-serverlesss-cloudant
```

This will create a new package '/[YOURACCOUNTNAME]_dev/go-serverlesss-cloudant'. To view the newly created package run this command: 

```
ibmcloud wsk package list
```

### Creating a Service Instance

Next we will want to create our Cloudant instance. This can be done with this command, be sure to replace `MY_REGION` with the current region you are working in:

```
ibmcloud resource service-instance-create cloudant-serverless cloudantnosqldb lite MY_REGION -p '{"legacyCredentials": false}' 
```

This will create a Cloudant instance with the name `cloudant-serverless`.

Next we need to create service credentials so we can connect to this datastore. To do that run the following: 

```
ibmcloud resource service-key creds_cloudantserverless
```

Finally to bind the Cloudant instance to the package we we just created run the following command:

```
ibmcloud wsk service bind cloudantnosqldb /[YOURACCOUNTNAME]_dev/go-serverlesss-cloudant --instance cloudant-serverless --keyname creds_cloudantserverless
```

This will bind our newly created Cloudant instance to the package: `/[YOURACCOUNTNAME]_dev/go-serverlesss-cloudant`. 


## Using Cloudant to Persist Data

With our Cloudant instance created let's connect it with some of the functions we have created in an earlier section to start persisting data. 

First we will need to create a database in our cloudant instance where we will be persisting our data to. 

```
ibmcloud fn action invoke --result /[YOURACCOUNTNAME]_dev/go-serverlesss-cloudant/create-database -p dbname "fibonaccidb"
```
This command will create the database `fibonaccidb` which we will be using to store the results of the invocation from section on sequences. 




<p  align="center">
	<font size="4">
 		<a href="STEP9.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="STEP11.md">Next >></a></td>
 </font>
</p>