package src.claim.webservice;

import java.util.ArrayList;
import java.util.Date;
import javax.jws.WebMethod;
import com.mitchell.examples.claim.*;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface MitchellClaimService {

	/**
	 * Creates a claim and persists it to the backing store.
	 * @param filename Filename of the MitchellClaim XML file to be stored
	 * @return true if claim is stored, else false
	 */
	@WebMethod
	boolean createClaim (String filename) throws MitchellClaimServiceException; 
	

	/**
	 * Returns a claim from the backing store.
	 * @param claimNumber Claim number of the desired claim
	 * @return claim to be read if claim number exists, else null
	 */
	@WebMethod
	MitchellClaimType readSpecificClaim (String claimNumber) throws MitchellClaimServiceException;
	

	/**
	 * Returns a list of claims within the desired loss date range.
	 * @param dateStart the lower bound of the loss date
	 * @param dateEnd the upper bound of the loss date
	 * @return claim to be read if claim number exists, else null
	 */
	@WebMethod
	MitchellClaimList readClaimByLossDateRange (Date dateStart, Date dateEnd) throws MitchellClaimServiceException;
	

	/**
	 * Returns a specific vehicle of a specific claim from the backing store.
	 * @param claimNumber Claim number of the desired claim
	 * @param inputVin VIN of the desired vehicle
	 * @return vahicle information if claim number and vin exists, else null
	 */
	@WebMethod
	VehicleInfoType getSpecificVehicle (String claimNumber, String inputVin) throws MitchellClaimServiceException;

	/**
	 * Deletes a claim from the backing store.
	 * @param claimNumber Claim number of the claim to be deleted
	 * @return Deleted claim if claim number exists, else null
	 */
	@WebMethod
	MitchellClaimType deleteClaim (String claimNumber) throws MitchellClaimServiceException;

	/**
	 * Updates a claim from the backing store. If the claim is new, adds to store.
	 * @param filename Name of the file with the updated
	 * @return updated claim
	 */
	@WebMethod
	MitchellClaimType updateClaim (String filename) throws MitchellClaimServiceException;
}