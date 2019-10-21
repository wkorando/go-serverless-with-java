## 9. Calling Functions Through the API Gateway

An API Gateway can be a great way to make your functions more accessible and to monitor their usage. Let's update our function to make it accessible through the API Gateway IBM Cloud provides. 

Let's update the `ratioWeb` sequence we created earlier to be accessible through the API Gateway. 

**Note:** For a function or sequence to be accessible through the API gateway, it must also be enabled as a web action.

1. We will need to append to the end of **manifest.yml** the following:

	```yaml
    apis:
      ratioAPI: #Endpoint ID
        api: #API Basepath
          ratio: #Endpoint Path
            ratio: #Function Reference
              method: GET
			  response: json
	```
	
	Again, make sure the `apis` section has the same indentation as the `actions` and `sequences` sections of the **manifest.yml**.
	 
2. Commit and push these changes via the Web IDE to start the deployment pipeline. Check the previous section of this workshop if you're not sure anymore how to do this.

3. Once the build has completed open the [Functions API Management](https://cloud.ibm.com/functions/apimanagement) page and click the **ratioAPI** row. It should look something like this:

	![](images/view_api.png)

4. This opens the management page for the API endpoint. Under route is the route to the endpoint. Copy that value and append the endpoint path `ratio?number=5` to it. This will execute the `ratio` function with the query parameter `number` set to 5. Test some other numbers as well and look how this impacts the golden ratio.

### Rate Limiting 

There are a number of ways you can configure your API. A common one would be rate limiting. Because with functions you are paying for every CPU cycle, limiting the number of times a function can be executed can be a great way of making sure you don't get stuck with a huge bill at the end of the month because e.g. a script or application got stuck in an endless loop. 

1. As rate limiting is measured per API key, we first need to set security on the API so that we can create a key to access `ratioAPI`. For this, click the API on the [Functions API Management](https://cloud.ibm.com/functions/apimanagement) page.
	
	![](images/api_definition.png)

	Next, click 'Definitions' (1) on the left-hand side. Then, in the 'Security and Rate Limiting' section, turn on both **Require applications to authenticate via API key** (2) and the **Limit API call rate on a per-key basis** switches. Set **Maximum calls** to 20 and the **Unit of time** to Minute. Finally,scroll to the bottom of the page and click 'Save' to save the changes.

2. Now that the rate limiting and the security are set, we need a key so that we can call the API. For this, select the 'Sharing' tab (1) on the lef-hand side of the page.

	![](images/api_definition.png)

	Click 'Create API key' (2) under **Sharing Outside of Cloud Foundry organization** and provide a name for the key. Click 'Create' to complete the creation of the key.

	![](images/api_definition.png)

3. Next, we will be using `curl` and some basic `bash` scripting skills to demonstrate the working of rate limiting on our API. For this, first call the API once and check the result.
	```bash
	curl -H 'x-ibm-client-id: <your_apikey>' -H 'accept: application/json' https://3082590d.eu-gb.apiconnect.appdomain.cloud/api/ratio?number=5
	```
	
	Make sure to replace the API endpoint URL with your own. The result should be similar to
	
	```json
	{
		"ratio": 1.666666666666667
	}
	```
	
	A rate limit of 20 calls per minute will be translated to one every 3 seconds. So, if we define a loop that calls the API 10 times in a row -- without sleeping -- the calls should be block and a HTTP 400 error code should be returned. Let's call the API 10 times in a row by invoking the following loop in a bash-like shell.

	```bash
	for i in {3..13}; do curl -H 'x-ibm-client-id: <your_apikey>' -H 'accept: application/json' https://833f4b30.eu-gb.apiconnect.appdomain.cloud/api/ratio?number=$i; done;
	```

	Again, make sure the API endpoint URL matches yours. The first entry returns a proper ratio. The next so many calls are rejected and after 3 seconds one call is accepted again, followed by so many rejections again...

	```json
	{"ratio": 2}
	{"status":429,"message":"Error: Rate limit exceeded"}
	{"status":429,"message":"Error: Rate limit exceeded"}
	{"status":429,"message":"Error: Rate limit exceeded"}
	{"status":429,"message":"Error: Rate limit exceeded"}
	{"status":429,"message":"Error: Rate limit exceeded"}
	{"status":429,"message":"Error: Rate limit exceeded"}
	{"status":429,"message":"Error: Rate limit exceeded"}
	{"ratio": 1.618181818181819}
	{"status":429,"message":"Error: Rate limit exceeded"}
	{"status":429,"message":"Error: Rate limit exceeded"}
	```

4. If we now introduce a 3 seconds pause after each API call, all 10 calls should be accepted and you should see the ratio converging to the golden ratio. The sleep of 3 seconds can be added as follows:

	```bash
	for i in {3..13}; do curl -H 'x-ibm-client-id: <your_apikey>' -H 'accept: application/json' https://833f4b30.eu-gb.apiconnect.appdomain.cloud/api/ratio?number=$i; sleep 3; done;
	```
	
	The output should be:
	```json
	{
	"ratio": 2
	}{
	"ratio": 1.5
	}{
	"ratio": 1.666666666666667
	}{
	"ratio": 1.6
	}{
	"ratio": 1.625
	}{
	"ratio": 1.615384615384616
	}{
	"ratio": 1.619047619047620
	}{
	"ratio": 1.617647058823530
	}{
	"ratio": 1.618181818181819
	}{
	"ratio": 1.617977528089888
	}{
	"ratio": 1.618055555555556
	}
	```

### Analytics and Logging
We've called the API a couple of times now, so it's good to have a look at the **Analytics and Logging** section in the summary of the API. This shows the last hour of API activity and the last 25 responses to API calls.

![](images/api_analytics.png)

To recall, the Cloud Functions Dashboard gives a similar overview, but then focussed on the serverless actions and sequences and not so much on the APIs.

![](images/monitor.png)

Now that you've learned how to define an API on top of your serverless actions and sequences -- and how to set security and rate limiting on this API -- it is time to see how you can combine serverless actions with e.g. a NoSQL database.

<p  align="center">
	<font size="4">
 		<a href="STEP8.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="STEP10.md">Next >></a></td>
 </font>
</p>