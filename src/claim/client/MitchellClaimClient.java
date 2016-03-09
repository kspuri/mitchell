package src.claim.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import src.claim.webservice.MitchellClaimService;
import java.util.GregorianCalendar;
import java.util.Date;

public class MitchellClaimClient{
	
	public static void main(String[] args) throws Exception {
	   
	URL url = new URL("http://localhost:9999/webservice?wsdl");
	
    QName qname = new QName("http://webservice.claim.src/", "MitchellClaimServiceImplService");
    QName qname2 = new QName("http://webservice.claim.src/", "MitchellClaimServiceImplPort");


    Service service = Service.create(url, qname);

    MitchellClaimService test = service.getPort(qname2,MitchellClaimService.class);

    System.out.println("Testing...");
    test.createClaim("create-claim.xml");
    test.updateClaim("update-claim.xml");
    System.out.println(test.getSpecificVehicle("22c9c23bac142856018ce14a26b6c299", "1M8GDM9AXKP042788"));
    System.out.println(test.readSpecificClaim("22c9c23bac142856018ce14a26b6c299"));
    GregorianCalendar begin = new GregorianCalendar(2014, 6, 5);
    GregorianCalendar end = new GregorianCalendar(2014, 6, 15);
    System.out.println(test.readClaimByLossDateRange(begin.getTime(), end.getTime()));
    test.deleteClaim("22c9c23bac142856018ce14a26b6c299");


    }

}
