## 10. Connecting to Database

IBM Cloud has a catalog of services available to handle the needs of the enterprise. A common need within enterprises is to store customer informnation in a database for longterm persistence or later processing. Let's walk through connecting our service to a Cloudant, a NoSql datastore.


```
ibmcloud resource service-instance-create cloudant-serverless cloudantnosqldb lite MY_REGION -p '{"legacyCredentials": true}'
```

Remember the name; we've called this Cloudant database: cloudant-serverless

We need to create some credentials to access the database. This command will create a service key with admin (Manager) privileges for the cloudant-serverless database:

```
ibmcloud resource service-key-create creds_cloudantserverless Manager --instance-name cloudant-serverless
```

You can get these credentials whenever you need them by running:

```
ibmcloud resource service-key creds_cloudantserverless
```

<p  align="center">
	<font size="4">
 		<a href="STEP9.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="STEP11.md">Next >></a></td>
 </font>
</p>