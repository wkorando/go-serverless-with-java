# Go Serverless with Java

Serverless functions are an emerging technology for decomposing business operations into very fine grain chunks of code. Functions provide advantages to organization by increasing agility and cost savings by allowing "scale to zero" when a function is no longer being actively used.

In this workshop we will look at how to write Serverless Functions in Java and run them on [Apache Openwhisk](https://openwhisk.apache.org/) hosted on IBM Cloud.

## Prerequisites:

Before beginning this workshop there are a few actions you must complete. Expand below to view the steps for the OS you plan on using when you do the workshop.

<summary><h3>Mac and Linux Users</h3></summary>
<details>

1. [Java 8+](https://adoptopenjdk.net/)
2. [IBM Cloud CLI](https://github.com/IBM-Cloud/ibm-cloud-cli-release/releases/)
3. Install the IBM Cloud Functions Plugin by running this command in your terminal window:

	```
	ibmcloud plugin install cloud-functions
	```
4. <a href="https://ibm.biz/Bd2Uv6" target="_blank">Create an IBM Cloud Account</a>

</details>

<summary><h3>Windows Users</h3></summary>
<details>

1. [Java 8+](https://adoptopenjdk.net/)
2. [IBM Cloud CLI](https://github.com/IBM-Cloud/ibm-cloud-cli-release/releases/)
3. Install the IBM Cloud Functions Plugin by running this command in your terminal window:

	```
	ibmcloud plugin install cloud-functions
	```
4. <a href="https://ibm.biz/Bd2Uv6" target="_blank">Create an IBM Cloud Account</a>
5. [Install Cygwin](https://www.cygwin.com/)
	6. While installing include **curl**, **vim**, and **git** as options

<h3>Using the Command Line</h3>

It is HIGHLY encouraged to use Cygwin to complete this workshop. Cygwin translates to the same pathing as the commands shown in this guide (*nix pathing) and also has the command line tools this workshop will use.

<h3>Using the Maven Wrapper</h3>

In the guide when the maven warpper is used `./mvnw` substitute with `./mvnw.cmd`.

</details>

## Table of Contents

### [0. Workshop Setup](SETUP.md)

### [1. Executing a Function with the IBM Cloud CLI](STEP1.md)

### [2. Building and Deploying a Serverless Function](STEP2.md)

### [3. Getting Familiar with OpenWhisk Commands](STEP3.md)

### [4. Creating a Web Action](STEP4.md)

### [5. Using Functions to Return HTML](STEP5.md)

### [6. Building a Deployment Pipeline](STEP6.md)

### [7. Viewing the Functions Dashboard](STEP7.md)

### [8. Working with Sequences](STEP8.md)

### [9. Calling Functions Through the API Gateway](STEP9.md)

### [10. Connecting to a Database](STEP10.md)

### [11. Creating a Rule](STEP11.md)

<p  align="center">
	<font size="4">
 		<a href="SETUP.md">Next >></a></td>
 </font>
</p>


