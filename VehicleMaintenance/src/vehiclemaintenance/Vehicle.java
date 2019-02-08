package vehiclemaintenance;

import java.util.ArrayList;
import java.util.Collections;

import javax.xml.bind.annotation.XmlElement;
/**
 * Stores a vehicles. contain a make, model, year, and list of maintenance records.
 * @author Leonard Carcaramo
 */
public class Vehicle 
{
	private String make;
	private String model;
	private int year;
	@XmlElement(name="maintenance") //needed in order for maintenance records to be stored to file as xml.
	private ArrayList<Maintenance> maintenance;
	
	/**
	 * Default Constructor (required for xml)
	 */
	public Vehicle() 
	{ 
		this("", "", 0, 0, "");
	}
	
	/**
	 * Constructor
	 * @param mk a make
	 * @param md a model
	 * @param y a year
	 * @param miles a milage
	 * @param dateCreated a date
	 */
	public Vehicle(String mk, String md, int y, int miles, String dateCreated)
	{
		this.setMake(mk);
		this.setModel(md);
		this.setYear(y);
		this.maintenance = new ArrayList<Maintenance>(); //initialize list of maintenance records.
		//maintenance object added to list to state that the vehicle was created.
		this.maintenance.add(new Maintenance(dateCreated ,miles,"Vehicle Created."));
	}

	//getters and setters.
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
	public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    //mutators for maintenance.
    public ArrayList<Maintenance> getMaintenanceList() { return this.maintenance; }
    public void sortMaintenance() {Collections.sort(this.maintenance, new MaintenanceComparator()); }
    
    public String toString()
    {
    	/* Since maintenance is sorted based on milage, the largest milage will always be at the end of of
    	 * the list. toString will always show the largest milage because it displays the milage of 
    	 * of the last element in the list of maintenance records. (It is implyed that the user will be entering 
    	 * the actual milage of the vehicle, where milage can only go up, so that toString is displaying the most
    	 * most recent milage of the vehicle since the last maintenance record.
    	 */
    	sortMaintenance();
    	return this.make + " " + this.model + "\t" + this.year + "\t"
    			+ this.maintenance.get(this.maintenance.size()-1).getMiles() + " Miles";
    }
}
