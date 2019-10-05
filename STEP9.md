## 9. Calling Functions Through the API Gateway

An API Gateway can be a great way to make your functions more accessible and to monitor their usage. Let's update our function to make it accessible through the API Gateway IBM Cloud provides. 

Let's update the `webHello` function we created earlier to be accessible through API Gateway. 

**Note:** For a function to be accessible through the API it must also be enabled as a web action.

1. We will need to append to the end of **manifest.yml** the following:

	```yaml
	apis:
	  webHello: #Endpoint ID
	    web: #API Basepath
	      hello: #Endpoint Path
	        webHello: #Function Reference
	          method: GET
	          response: http
	```
2. Commit and push these changes to start the deployment pipeline
3. Once the build has completed open the [Functions API Management](https://cloud.ibm.com/functions/apimanagement) page and click the **webHello** row. It should look something like this:
	![](images/view_api.png)
4. This opens the management page for the API endpoint. Under route is the route to the endpoint, copy that value and append the endpoint path to it `/hello` and that will call execute the `webHello` function. Like earlier you can add a query param `?name=JFall` to it as well. 

### Rate Limiting 

There are a number of ways you can configure your API. A common one would be rate limiting. Because with functions you are paying for every CPU cycle, limiting the number of times a function can be executed can be a great way of making sure you don't get stuck with a huge bill at the end of the month because a script or application got stuck in an endless loop. 

	```yaml
	apis:
	  webHello: #Endpoint ID
	    web: #API Basepath
	      hello: #Endpoint Path
	        webHello: #Function Reference
	          method: GET
	          response: http
	```

<p  align="center">
	<font size="4">
 		<a href="STEP8.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="STEP10.md">Next >></a></td>
 </font>
</p>