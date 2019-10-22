## 1. Executing a Function with the IBM Cloud CLI

1. Run the following command to invoke a test function from the command-line:

   ```
   ibmcloud fn action invoke whisk.system/utils/echo -p message hello --result
   ```

   You should get back a result that looks like this:

   ```
   {
       "message": "hello"
   }
   ```

	This command verifies that IBM Cloud CLI is configured correctly . If this does not work, please contact the workshop organiser to provide assistance!

<p  align="center">
	<font size="4">
 		<a href="SETUP.md"><< Back</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="README.md">Index</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="STEP2.md">Next >></a></td>
 </font>
</p>