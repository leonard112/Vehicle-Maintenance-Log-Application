package vehiclemaintenance;

/**
 * Stores a maintenance record.
 * @author Leonard Carcaramo
 */
public class Maintenance 
{
	private String date; //the day work was done.
	private int miles; //how many miles on the vehicle.
	private String workDone; //what was done to the vehicle.
	
	/**
	 * Default Constructor (required for xml)
	 */
	public Maintenance()
	{
		this("", 0, "");
	}
	/**
	 * Constructor
	 * @param d a date
	 * @param m a milage
	 * @param w work done
	 */
	public Maintenance(String d, int m, String w)
	{
		this.setDate(d);
		this.setMiles(m);
		this.setWorkDone(w);
	}

	//getters and setters
	public String getDate() { return date; }
	public void setDate(String date) { this.date = date; }
	public int getMiles() { return miles; }
    public void setMiles(int miles) { this.miles = miles; }
    public String getWorkDone() { return workDone; }
    public void setWorkDone(String workDone) { this.workDone = workDone; }
    
    public String toString()
    {
    	return "Date: " + this.date + " Milage: " + this.miles + " Work Done: " + this.workDone;
    }
}
