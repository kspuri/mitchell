The webservice MitchellClaimService has the following functionality:
	a. Create a claim and persist it to a backing store. 
	b. Read a claim from the backing store.
	c. Find a list of claims in the backing store by date range of the LossDate.
	d. Update a claim in the backing store. 
	e. Read a specific vehicle from a specific claim.
	f. Delete a claim from the backing store.

There are 2 basic source folders. The first package (src) has the service implementation and test client. 
The package com/mitchell/examples/claim has the POJOs for XML binding. These were auto-generated using xjc, but 
the MitchellClaimType.java file has been changed a little. 

The src folder has the webservice and a mock client. There are additional files such as create-claim.xml and update-claim.xml for testing. 

Data model: For the store, another XML file called persistentStore.xml has been used. 

To compile and run the service, please run the following commands from the root folder:
javac com\mitchell\examples\claim\*.java
javac src\claim\webservice\*.java
java src.claim.webservice.MitchellClaimServicePublisher

To compile and run the test client, please run the following commands from the root folder:
javac src\claim\client\*.java
java src.claim.client.MitchellClaimClient

No third party libraries were used. JAXB is the primary tool used here.
