package vehiclemaintenance;

import java.util.Comparator;

/**
 * Compares two Vehicle objects.
 * @author Leonard Carcaramo
 *
 */
public class VehicleComparator implements Comparator<Vehicle>
{
	/**
	 * Compares x to y.
	 * @param x a Vehicle object
	 * @param y a vehicle object
	 */
	public int compare(Vehicle x, Vehicle y)
	{
		if (x.getMake().compareTo(y.getMake()) > 0) //if the make of x is greater than the make of y.
		{
			return 1;
		}
		else if (x.getMake().compareTo(y.getMake()) < 0) //if the make of x is less than the make of y.
		{
			return -1;
		}
		else // if x and y have the same make.
		{
			if (x.getModel().compareTo(y.getModel()) > 0) //if the model of x is greater than the model of y.
			{
				return 1;
			}
			else if (x.getModel().compareTo(y.getModel()) < 0) //if the model of x is less than the model of y.
			{
				return -1;
			}
			else // if x and y have the same model.
			{
				if (x.getYear() > y.getYear()) //if the year of x is greater than the year of y.
				{
					return 1;
				}
				else if (x.getYear() < y.getYear()) //if the year of x is less than the year of y.
				{
					return -1;
				}
				else //x and y have the same year
				{
					//make sure both x and y's maintenance lists are sorted.
					x.sortMaintenance();
					y.sortMaintenance();
					//if the milage of x is greater than the milage of y.
					if (x.getMaintenanceList().get(x.getMaintenanceList().size()-1).getMiles() >
					y.getMaintenanceList().get(y.getMaintenanceList().size()-1).getMiles()) 
					{
						return 1;
					}
					//The milage of x is less than the milage of y.
					else if (x.getMaintenanceList().get(x.getMaintenanceList().size()-1).getMiles() <
					y.getMaintenanceList().get(y.getMaintenanceList().size()-1).getMiles())
					{
						return -1;
					}
					else // x and y are equal.
					{
						return 0;
					}
				}
			}
		}
	}
}
