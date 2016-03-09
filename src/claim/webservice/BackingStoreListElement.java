package src.claim.webservice;

import com.mitchell.examples.claim.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;


public class BackingStoreListElement {

	@XmlAttribute
	public String claimNumber; 
 
	@XmlElement
	public MitchellClaimType claim = new MitchellClaimType();
}