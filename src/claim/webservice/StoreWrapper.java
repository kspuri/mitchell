package src.claim.webservice;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.mitchell.examples.claim.*;
 
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StoreWrapper {
 
   @XmlJavaTypeAdapter(BackingStoreAdapter.class)
   Map<String, MitchellClaimType> map = 
    new HashMap<String, MitchellClaimType>();
 
   public Map getMap() {
      return map;
   }
 
   public void setMap(Map map) {
      this.map = map;
   }
 
}