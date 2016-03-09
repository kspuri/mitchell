package src.claim.webservice;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;
import com.mitchell.examples.claim.*;
import java.util.Date;
import javax.jws.WebService;


@WebService
public class MitchellClaimServiceImpl implements MitchellClaimService {

	/* Persistent store */
	private File backingStore = new File("persistentStore.xml");
	/* Runtime store */
	private Map<String, MitchellClaimType> map = new HashMap<String, MitchellClaimType>();
	/* Wrapper object for runtime store */
	private StoreWrapper store = new StoreWrapper();

	/**
	 * Creates a claim and persists it to the backing store.
	 * @param filename Filename of the MitchellClaim XML file to be stored
	 * @return true if claim is stored, else false
	 */
	@Override
	public boolean createClaim (String filename) throws MitchellClaimServiceException
	{
		File toBeCreated = new File(filename);
		MitchellClaimType claim = parseClaim(toBeCreated);
		map.put(claim.getClaimNumber(), claim);
		updateStore();
		return true;
	}

	/**
	 * Returns a claim from the backing store.
	 * @param claimNumber Claim number of the desired claim
	 * @return claim to be read if claim number exists, else null
	 */
	@Override
	public MitchellClaimType readSpecificClaim (String claimNumber) throws MitchellClaimServiceException
	{
		Map<String, MitchellClaimType> readList = readStore();
		return readList.get(claimNumber);
	}

	/**
	 * Returns a list of claims within the desired loss date range.
	 * @param dateStart the lower bound of the loss date
	 * @param dateEnd the upper bound of the loss date
	 * @return claim to be read if claim number exists, else null
	 */
	@Override
	public MitchellClaimList readClaimByLossDateRange (Date dateStart, Date dateEnd) 
																	throws MitchellClaimServiceException
	{
		Date toCompare = null;
		Map<String, MitchellClaimType> readList = readStore();
		ArrayList<MitchellClaimType> claimList = new ArrayList<MitchellClaimType>();
		for (MitchellClaimType m: readList.values())
		{
			toCompare = m.getLossDate().toGregorianCalendar().getTime();
			if (toCompare.after(dateStart) && toCompare.before(dateEnd))
				claimList.add(m);
		}
		return new MitchellClaimList(claimList);
	}

	/**
	 * Returns a specific vehicle of a specific claim from the backing store.
	 * @param claimNumber Claim number of the desired claim
	 * @param inputVin VIN of the desired vehicle
	 * @return vahicle information if claim number and vin exists, else null
	 */
	@Override
	public VehicleInfoType getSpecificVehicle (String claimNumber, String inputVin)
																	throws MitchellClaimServiceException
	{
		Map<String, MitchellClaimType> readList = readStore();
		MitchellClaimType claim = readList.get(claimNumber);
		if (claim == null)
			return null;
		List<VehicleInfoType> vehicleList = claim.getVehicles().getVehicleDetails();
		for (VehicleInfoType vehicle: vehicleList)
			if (vehicle.getVin().equals(inputVin))
				return vehicle; 
		return null;
	}

	/**
	 * Deletes a claim from the backing store.
	 * @param claimNumber Claim number of the claim to be deleted
	 * @return Deleted claim if claim number exists, else null
	 */
	@Override
	public MitchellClaimType deleteClaim (String claimNumber) throws MitchellClaimServiceException
	{
		MitchellClaimType deleted = map.remove(claimNumber);
		updateStore();
		return deleted;
	}

	/**
	 * Updates a claim from the backing store. If the claim is new, adds to store.
	 * @param filename Name of the file with the updated
	 * @return updated claim
	 */
	@Override
	public MitchellClaimType updateClaim (String filename) throws MitchellClaimServiceException
	{
		File updated = new File(filename); 
		MitchellClaimType changed = parseClaim(updated);
		Map<String, MitchellClaimType> readList = readStore();

		// check if claim with that number exists
		String cnumber = changed.getClaimNumber();
		MitchellClaimType orig = readList.get(cnumber);

		// if claim is new, add to store
		if (orig == null)
		{
			map.put(cnumber, changed);
			updateStore();
			return changed;
		}

		// Update existing claim 
		orig = updateFields(orig, changed);

		// Replace the changed claim in the store
		map.replace(cnumber, orig);
		updateStore();
		return orig;
	}


	/*
	 * Parse the input file into a claim.
	 */
	private MitchellClaimType parseClaim (File file) throws MitchellClaimServiceException
	{
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("com.mitchell.examples.claim");
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			JAXBElement<MitchellClaimType> parsedElem = (JAXBElement<MitchellClaimType>) jaxbUnmarshaller.unmarshal(file);
			return parsedElem.getValue();
		}
		catch (JAXBException e)
		{
			throw new MitchellClaimServiceException("Error in parsing given XML file.");
		}
	}

	/*
	 * Store the current claims into the backing store.
	 */
	private boolean updateStore () throws MitchellClaimServiceException
	{
		try {
			store.setMap(map);
			JAXBContext jaxbContext = JAXBContext.newInstance(StoreWrapper.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); //just for pretty
			jaxbMarshaller.marshal(store, backingStore);
			return true;
		}
		catch (JAXBException e)
		{
			throw new MitchellClaimServiceException("Error in writing to the store.");
		}
	}

	/*
	 * Return the claims in the current backing store.
	 */
	private Map<String, MitchellClaimType> readStore () throws MitchellClaimServiceException
	{
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(StoreWrapper.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StoreWrapper storeWrapper = (StoreWrapper) jaxbUnmarshaller.unmarshal(backingStore);
			return storeWrapper.getMap();
		}
		catch (JAXBException e)
		{
			throw new MitchellClaimServiceException("Error in parsing given store file.");
		}
	}

	/*
	 * Update fields of orig with fields in changed. Ignores null fields in changed. 
	 */
	private MitchellClaimType updateFields (MitchellClaimType orig, MitchellClaimType changed) 
	{
		try {
			Method[] listMethods = orig.getClass().getMethods();

			//loop through all getters
			for (Method fn: listMethods)
			{
				String methodName = fn.getName();

				if(methodName.startsWith("get"))
				{
					Object returnedValue = fn.invoke(changed);

					// if a value was updated, it will be non-null
					if (returnedValue != null && methodName != "getClass")
					{
                    	//set the original property to the updated value
						String setterName = methodName.replaceFirst("get", "set");
						Method setter = orig.getClass().getMethod(setterName, fn.getReturnType());
						setter.invoke(orig, returnedValue);
					}
				}
			}
		}

		catch (Exception e)
		{
			System.err.println("This should never happen.");
		}
		return orig;
	}

}