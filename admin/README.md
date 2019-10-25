# Administrator Guide

This document is meant as guide for someone intending to run this workshop. This guide offers actions that should be taken when running this workshop and tips and advice to make the running of this workshop more smooth. 

For questions about this workshop reach out to Billy Korando: 

**Email:** william dot korando @ ibm <br/>
**Slack:** @billy<br/>
**Twitter:** @BillyKorando


 
## Befor You Run the Workshop for the First Time

If you are planning on running this workshop here are some of the things you should do: 

1. Run through the workshop yourself... duh! (don't do this on your IBM account!)
2. Familiarize yourself with Serverless (also duh!)
3. Fork this repo, you will likely need to make some minor changes to regionalize this workshop (See: [Before You Start the Workshop]())
3. Be a good teammate, if you run into an issue that isn't covered in this guide or see an opportunity for improvement update this guide or the workshop itself and submit a PR to Billy Korando. 


## Before You Start the Workshop

Be sure to run through these steps before starting the workshop!

1. Update IBM Cloud creation link with VCPI for workshop 
2. Whitelist IP 
2. Update guide with region workshop is being held in, cheatsheet below: 

```
Name       Display name   
au-syd     Sydney   
jp-tok     Tokyo   
eu-de      Frankfurt   
eu-gb      London   
us-south   Dallas   
us-east    Washington DC 
```

## Common Problems

These are issues commonly encountered when running this lab:

### Pre-Existing IBM Account

<details>
  <summary>User has an Expired IBM Account</summary>
  If a user has an expired/lapsed IBM account it can be reactivated using a promo code. To 
<details>


### Blank Region When Invoking Actions

<details>
  <summary>User attempts to invoke action, but gets response saying region is blank</summary>
  Sometimes an user, even if they run `target --cf` and select a region, will still run into issues when attempting to invoke actions. Steps to resolve:
  1. Run: `ibmcloud target -r REGION` see above for region list 
<details>

### Creating Build Pipeline Problems

<details>
  <summary>Space field not being prepopulated in API area</summary>
 	A user with multiple organizations might run into this issue. Steps to resolve are as follows:
 	1. In the command like run `ibmcloud account spaces` in the output it should include the org they are using within Cloud Foundry
 	2. Make sure that same Organization is being used in the API creation Org field
<details>


## Authors

This workshop was created by:

Billy Korando 
 
Edward Ciggaar