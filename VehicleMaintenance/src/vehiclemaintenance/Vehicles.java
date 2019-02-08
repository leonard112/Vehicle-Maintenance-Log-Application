package vehiclemaintenance;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 * Stores a list of Vehicle objects. (Needed for xml implementation).
 * @author Leonard Carcaramo
 */
public class Vehicles {
   @XmlElement(name="vehicle") //required for a vehicle to be stored in a file as xml.
   private List<Vehicle> vehicles = new ArrayList();

   //getter
   public List<Vehicle> getVehicles() { return vehicles; }
}
