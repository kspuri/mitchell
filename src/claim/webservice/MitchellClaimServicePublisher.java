package src.claim.webservice;

import javax.xml.ws.Endpoint;

public class MitchellClaimServicePublisher {
	
	public static void main(String[] args) {
	   Endpoint.publish("http://localhost:9999/webservice", new MitchellClaimServiceImpl());
    }

}