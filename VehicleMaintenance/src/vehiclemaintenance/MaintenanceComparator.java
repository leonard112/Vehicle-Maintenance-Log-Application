package vehiclemaintenance;

import java.util.Comparator;

/**
 * Compares two maintenance objects
 * @author Leonard Carcaramo
 */
public class MaintenanceComparator implements Comparator<Maintenance>
{
	/**
	 * Compares x to y.
	 * @param x a Maintenance object
	 * @param y a Maintenance object
	 * @returns an integer showing how x compares to y.
	 */
	public int compare(Maintenance x, Maintenance y)
	{
		if (x.getMiles() > y.getMiles()) //x has a larger milage than y.
		{
			return 1;
		}
		else if (x.getMiles() < y.getMiles()) //x has a smaller milage than y.
		{
			return -1;
		}
		else // x and y have the same milage.
		{
			return 0;
		}
	}
}
