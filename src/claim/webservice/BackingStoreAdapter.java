package src.claim.webservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import com.mitchell.examples.claim.*;
  
public final class BackingStoreAdapter extends XmlAdapter<BackingStoreList,Map<String, MitchellClaimType>> {
  
   @Override
   public BackingStoreList marshal(Map<String, MitchellClaimType> inputMap) throws Exception {
      BackingStoreList backingList = new BackingStoreList();
      for(Entry<String, MitchellClaimType> entry : inputMap.entrySet()) {
         BackingStoreListElement listElem = new BackingStoreListElement();
         listElem.claimNumber = entry.getKey();
         listElem.claim = entry.getValue();
         backingList.list.add(listElem);
      }
      return backingList;
   }
  
   @Override
   public Map<String, MitchellClaimType> unmarshal(BackingStoreList inputMap) throws Exception {
      Map<String, MitchellClaimType> readList = new HashMap<String, MitchellClaimType>();
      for(BackingStoreListElement listElem : inputMap.list) {
         readList.put(listElem.claimNumber, listElem.claim);
      }
      return readList;
   }
  
}