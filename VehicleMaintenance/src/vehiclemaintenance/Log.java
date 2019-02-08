package vehiclemaintenance;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import javax.xml.bind.JAXB;
import javax.xml.bind.*;

import java.util.InputMismatchException;

/**
 * Allows the user to keep maintenance records for any amount of vehicles.
 * @author Leonard Carcaramo
 *
 */
public class Log 
{
	/*
	 * IMPORTANT INFORMATION ABOUT PARTS OF THE PROGRAM THAT DEAL WITH INDICES.
	 * user input is taken as indices [1-collectionSize].
	 * when operations are performed on a collection indices are decremented by 1.
	 */
	
	private static BufferedWriter output; //writes to file
	private static BufferedReader input; //reads file
	private static Vehicles vehicles; //list of vehicles.
	
	/**
	 * Makes changes to vehicles.xml
	 * @param args
	 */
	public static void main(String[] args) 
	{
		openFileForReading();
		readFile();
		closeFileForReading();
		openFileForWriting();
		writeToFile();
		closeFileForWriting();
	}
	
	/**
	 * Writes vehicle list to vehicles.xml
	 */
	public static void writeToFile()
	{
		JAXB.marshal(vehicles, output); //vehicles written to vehicles.xml
	}
	
	/**
	 * Reads vehicle list from vehicles.xml and allows the user to make changes to vehicle list.
	 */
	public static void readFile()
	{
		try
		{
			vehicles = JAXB.unmarshal(input, Vehicles.class); //Vehicle objects are read in from vehicles.xml
		}
		catch(DataBindingException unmarshalException)
		{
			//new vehicle list created if there is nothing in vehicles.xml or if the program is unable to read it
			vehicles = new Vehicles();
		}
		
		//options to choose from for the main menu of the program.
		String[] startOptions = {"manage maintenance", "add", "remove", "quit"};
		String startOption = ""; //stores the user's choice for start option.
		
		do //this repeats until the user chooses to quit.
		{
			//list of vehicles displayed every time main menu is brought up to allow the user to manage vehicles.
			displayVehicles(vehicles.getVehicles());
			startOption = getOption(startOptions); //user chooses one of the start options.
			switch(startOption) //executes code based on user input.
			{
				case "manage maintenance": //allow the user to manage the maintenance of a vehicle.
					//user selects a vehicle
					if (vehicles.getVehicles().size()==0) //no vehicles in vehicle list.
					{
						System.out.println("Sorry. you cannot manage maintence because there are no vehicles "
								+ "to manage.\n");
					}
					else
					{
						int vehicleIndex = getIndex(vehicles.getVehicles().size(),"Please enter the index of the vehicle that"
								+ " you would like to manage.");
						String[] manageOptions = {"add", "remove", "go back"};
						String manageOption = "";
					
						do //This repeats until the user chooses to go back to the main menu.
						{
							//all the maintenance of the selected vehicle is displayed.
							displayMaintenance(vehicles.getVehicles().get(vehicleIndex-1).getMaintenanceList(), 
									vehicles.getVehicles().get(vehicleIndex-1).toString());
							manageOption = getOption(manageOptions); //user chooses an option.
							switch(manageOption) //executes code based on user input.
							{
								case "add": //add new maintenance to vehicle
									//maintenacne object is created based on user input and added to vehicle.
									String date = createDate();
									int miles = promptInt("Please enter a milage.");
									String workDone = promptStr("Please give a breif description of what has been done "
											+ "to the vehicle.");
									vehicles.getVehicles().get(vehicleIndex-1).getMaintenanceList().add(new Maintenance(date, miles, workDone));
									vehicles.getVehicles().get(vehicleIndex-1).sortMaintenance();
									break;
								case "remove": //remove maintenance from the vehicle.
									//user removes maintenance at a specific index or removes all.
									int removeIndex = indexToRemove(vehicles.getVehicles().get(vehicleIndex-1).getMaintenanceList().size());
									boolean remove = askQuestion("Are you sure you want to remove specified maintenance?");
									if(remove)
									{
										removeMaintenance(vehicleIndex,removeIndex);
									}
									break;
								case "go back": //go back to the main menu
									boolean back = askQuestion("Are you sure you want to go back to the main menu?");
									if (!back)
									{
										manageOption = "";
									}
									break;
						
							}
						}
						while(!manageOption.equals("go back")); //do while is told to end with manageOption equals "go back"
					}	
					break;
					
				case "add": //add a new vehicle.
					//vehicle object created based on user input and added to vehicle list.
					String make = promptStr("Please enter a make.");
					String model = promptStr("Please enter a model.");
					int year = promptInt("Please enter a year.");
					int miles = promptInt("Please enter a mileage.");
					String date = createDate();
					vehicles.getVehicles().add(new Vehicle(make, model, year, miles, date));
					Collections.sort(vehicles.getVehicles(), new VehicleComparator());
					break;
					
				case "remove": //remove vehicles.
					//user removes vehicle at a specified index or removes all.
					if (vehicles.getVehicles().size() == 0)
					{
						System.out.println("No vehicles to remove.\n");
					}
					else
					{
						int removeIndex = indexToRemove(vehicles.getVehicles().size());
						boolean remove = askQuestion("Are you sure you wnat to remove specified vehicle(s)?");
						if(remove)
						{
							removeVehicles(removeIndex);
						}
					}
					break;
					
				case "quit": //user chooses to quit.
					boolean quit = askQuestion("Are you sure you want to quit?");
					if (!quit)
					{
						startOption = "";
					}
					break;
			}
		}
		while(!startOption.equals("quit")); //when startOption equals "quit" do while will stop.
	}
	
	/**
	 * Opens vehicles.xml for writing.
	 * @throws IOException
	 * @throws SecurityException
	 */
	public static void openFileForWriting() 
	{
		try 
		{
			output = new BufferedWriter(
			   Files.newBufferedWriter(Paths.get("vehicles.xml")));
		} 
		catch (IOException e)
		{
			System.err.println("Error: Unable to open vehicles.xml");
		}
		catch (SecurityException securityException)
		{
			System.err.println("You do not have permission to access vehicles.xml");
		}
	}
	
	/**
	 * Opens vehicles.xml for reading.
	 * @throws IOException
	 * @throws SecurityException
	 */
	public static void openFileForReading() 
	{
		try 
		{
			input = Files.newBufferedReader(Paths.get("vehicles.xml"));
		} 
		catch (IOException e)
		{
			System.err.println("Error: Unable to open vehicles.xml");
		}
		catch (SecurityException securityException)
		{
			System.err.println("You do not have permission to access vehicles.xml");
		}
	}
	
	/**
	 * Closes vehicles.xml after being opened for writing.
	 * @throws IOException
	 */
	public static void closeFileForWriting() 
	{
		try 
		{
			output.close();
		} 
		catch (IOException e) 
		{
			System.err.println("Error Closing vehicles.xml");
		}
	}
	
	/**
	 * Closes vehicles.xml after being opened for Reading.
	 * @throws IOException
	 */
	public static void closeFileForReading() 
	{
		try 
		{
			input.close();
		} 
		catch (IOException e) 
		{
			System.err.println("Error Closing vehicles.xml");
		}
	}
	/**
	 * Displays all the vehicles in a list of vehicle objects.
	 * @param x a list of vehicles.
	 */
	public static void displayVehicles(List<Vehicle> x)
	{
		//prints out each element in order with indexes [1-n].
		System.out.println("VEHICLES:");
		if (x.size() == 0)
		{
			System.out.println("No items found.");
		}
		else
		{
			for (int i = 0; i < x.size(); i++)
			{
				System.out.printf("%d) %s%n", i+1, x.get(i).toString());
			}
		}
		System.out.println();
	}
	/**
	 * Display all of the maintenance of a vehicle.
	 * @param x A list of Maintenance objects
	 * @param vehicle Vehicle that is having it's maintenance displayed.
	 */
	public static void displayMaintenance(List<Maintenance> x, String vehicle)
	{
		//prints out each element in order with indexes [1-n].
		System.out.println("MAINTENANCE for (" + vehicle + "):");
		if (x.size() == 0)
		{
			System.out.println("No items found.");
		}
		else
		{
			for (int i = 0; i < x.size(); i++)
			{
				System.out.printf("%d) %s%n", i+1, x.get(i).toString());
			}
		}
		System.out.println();
	}
	/**
	 * allows the user to choose an option from an array options.
	 * @param options an array of options.
	 * @return The option the user choose.
	 * @throws InvalidOptionException
	 */
	public static String getOption(String[] options)
	{
		boolean continueLoop = false; //controls do while
		String option = ""; //option user chooses
		String prompt = "Enter "; //used to format prompt.
		String exception = "Invalid Option!\nValid Options include: "; //used when handling exceptions.
		boolean goodInput = false; //is user input valid.
		
		//prompt and exception formated based on the elements in options
		for (int i = 0; i < options.length; i++)
		{
			if (i == options.length-1)
			{
				prompt += "or [" + options[i] + "] to " + options[i] + ".";
				exception += "and [" + options[i] + "].";
			}
			else
			{
				prompt += "[" + options[i] + "] to " + options[i] + ", ";
				exception += "[" + options[i] + "], ";
			}
		}
		do //repeats until user enters good input.
		{
			continueLoop = false; //tells do while to stop.
			try
			{
				option = promptStr(prompt); //gets user input.
				option = option.toLowerCase(); //eliminates case sensitivity.
				for (String s : options) //checks to see if user input is valid
				{
					if (option.equals(s)) 
					{
						goodInput = true;
					}
				}
				if (!goodInput) //exception thrown if user input is not valid.
				{
					InvalidOptionException invalidOptionException = new InvalidOptionException();
					throw invalidOptionException;
				}
			}
			catch(InvalidOptionException invalidOptionException) //exception handled.
			{
				System.out.println(exception);
				continueLoop = true; //tells do while to loop again.
			}
		}
		while(continueLoop);
		return option; 
	}
	/**
	 * Creates a date in (mm/dd/yy) format.
	 * @return a properly formatted date.
	 */
	public static String createDate()
	{

		String month = getDatePeice("month");
		String day = getDatePeice("day");
		String year = getDatePeice("year");
		
		return month + "/" + day + "/" + year;
	}
	/** 
	 * allows the user to create a date piece needed in order to make a full date in the form (mm/dd/yy).
	 * @param promptKey The part of a date that is being looked for (month/day/year).
	 * @return a 2 digit date piece formatted as a String.
	 * @throws InvalidDateException
	 * @throws InputMismatchException
	 */
	public static String getDatePeice(String promptKey)
	{
		boolean continueLoop = false; //controls do while.
		
		int element = 0; //Date piece that the user inputs.
		
		do
		{
			continueLoop = false; //tells do while to stop.
			try
			{
				element = promptInt("Please enter a " + promptKey + ". (2 digits)");//user input.
				
				//checks to see if input is valid. If not, exception thrown.
				if (element > 99 || element < 0)
				{
					InvalidDateException invalidDateException = new InvalidDateException();
					throw invalidDateException;
				}
			}
			catch (InvalidDateException invalidDateException) //exception handled for invlaid date.
			{
				System.out.println("A " + promptKey + " can only be a positive INTEGER with a max of two didgets.");
				continueLoop = true; //tells do while to loop again.
			}
			catch (InputMismatchException inputMismatchException) //exception handled for input mismatch.
			{
				System.out.println("A " + promptKey + " can only be a positive INTEGER with a max of two didgets.");
				continueLoop = true; //tells do while to loop again.
			}
		}
		while (continueLoop);
		

		//element properly formatted and returned below.
		if (element < 10)
		{
			return "0" + element;
		}
		else
		{
			return "" + element;
		}
	}
	/**
	 * prompts and takes user input for an int and returns it. (handles InputMismatchException)
	 * @param s A prompt.
	 * @return An integer.
	 * @throws InputMismatchException
	 */
	public static int promptInt(String s)
	{
		boolean continueLoop = false; //controls do while.
		int output = 0;
		
		do
		{
			continueLoop = false; //tell do while to stop.
			try
			{
				Scanner input = new Scanner(System.in);
				System.out.println(s);
				output = input.nextInt();
			}
			catch(InputMismatchException inputMismatchException) //handle input mismatch
			{
				System.out.println("Please enter an INTEGER only when entering a number.");
				continueLoop = true; //tell do while to loop again.
			}
		}
		while(continueLoop);
		return output;
	}
	/**
	 * Prompts and takes user input for a String and then returns it.
	 * @param s A prompt
	 * @return A String
	 */
	public static String promptStr(String s)
	{
		Scanner input = new Scanner(System.in);
		System.out.println(s);
		return input.nextLine();
	}
	/**
	 * Asks user a question that requires a yes or no answer.
	 * @param question Question being asked.
	 * @return boolean true "yes", or false "no"
	 * @throws InvalidOptionException
	 */
	public static boolean askQuestion(String question)
	{
		Boolean continueLoop = false; //controls do while.
		boolean answer = true; //store user's option of "yes" true or "no" false.
		
		do
		{
			continueLoop = false; //tells do while to stop.
			try
			{
				String option = promptStr(question + " [Yes/No]:"); //user enters yes or no.
				
				//checks to see if user input is valid and changes answer accordingly.
				if (option.toLowerCase().equals("yes"))
				{
					answer = true;
				}
				else if (option.toLowerCase().equals("no"))
				{
					answer = false;
				}
				else //if user input is invalid, exception will be thrown.
				{
					InvalidOptionException invalidOptionException = new InvalidOptionException();
					throw invalidOptionException;
				}
			}
			catch (InvalidOptionException invalidOptionException) //exception handled.
			{
				System.out.println("\nPlease enter only valid options.\n"
						+ "Valid options include [yes] or [no] only.\n");
				continueLoop = true; //tells do while to continue.
			}
		}
		while(continueLoop);
		
		return answer;
	}
	
	/**
	 * Asks the user if he or she want to remove an element at a specified index, or if he or she wants remove all.
	 * @param collectionSize An int representing the size of the collection being changed.
	 * @return -1 to remove all or some valid index.
	 */
	public static int indexToRemove(int collectionSize)
	{
		int index = 0; //will be set to -1 to remove all or a specific index to remove.
		String[] options = {"remove all", "remove at index"}; //remove options.
		String option = getOption(options); //user input.
		switch(option) //executes based on user input.
		{
			case "remove all": //index is set to -1 for "remove all"
				index = -1;
				break;
			case "remove at index": //gets the an index from the user if option equals "remove at index"
				index = getIndex(collectionSize, "Please enter the index of the item that you would like to remove.");
				break;
		}
		return index;
	}
	/**
	 * Asks the user to enter the index of a collection. Valid indices: [1-collectionSize]
	 * @param collectionSize Size of the collection that the user is choosing an index from.
	 * @param prompt A prompt.
	 * @return An index.
	 * @throws InvlidOptionException
	 */
	public static int getIndex(int collectionSize, String prompt)
	{
		boolean continueLoop = false; //controls do while.
		int index = 0; //index specified by the user.
		
		do
		{
			try
			{
				continueLoop = false; //tells do while to stop.
				index = promptInt(prompt); //user inputs an index.
				if (index < 1 || index > collectionSize)//checks to see if index is valid.
				{
					InvalidOptionException invalidOptionException = new InvalidOptionException();
					throw invalidOptionException;
				}
			}
			catch(InvalidOptionException invalidOptionException)
			{
				System.out.println("Please enter a valid index only. [1-" + collectionSize + "]");
				continueLoop = true; //tells do while to continue
			}
		}
		while(continueLoop);
		return index;
	}
	/**
	 * Removes vehicles from list based on removeIndex (-1 to remove all, or at index)
	 * @param removeIndex (-1 to remove all, or at index)
	 */
	public static void removeVehicles(int removeIndex)
	{
		if(removeIndex == -1)
		{
		vehicles.getVehicles().removeAll(vehicles.getVehicles());
		}
		else
		{
		vehicles.getVehicles().remove(removeIndex-1);
		}
		Collections.sort(vehicles.getVehicles(), new VehicleComparator());
	}
	/**
	 * Removes maintenance from a vehicle based on removeIndex (-1 to remove all, or at index)
	 * @param vehicleIndex Index of the vehicle being edited.
	 * @param removeIndex (-1 to remove all, or at index)
	 */
	public static void removeMaintenance(int vehicleIndex, int removeIndex)
	{
		vehicleIndex--;
		int size = vehicles.getVehicles().get(vehicleIndex).getMaintenanceList().size();
		String date = vehicles.getVehicles().get(vehicleIndex).getMaintenanceList().get(size-1).getDate();
		int miles = vehicles.getVehicles().get(vehicleIndex).getMaintenanceList().get(size-1).getMiles();
		
		if(removeIndex == -1)
		{
			vehicles.getVehicles().get(vehicleIndex).getMaintenanceList().removeAll(vehicles.getVehicles().get(vehicleIndex).getMaintenanceList());
			vehicles.getVehicles().get(vehicleIndex).getMaintenanceList().add(new Maintenance(date, miles, 
					"All maintenance has been deleted.\n"
					+ "However, we saved the most recent date and milage for this vehicle."));
		}
		else
		{
			vehicles.getVehicles().get(vehicleIndex).getMaintenanceList().remove(removeIndex-1);
			if (size == 1)
			{
				vehicles.getVehicles().get(vehicleIndex).getMaintenanceList().add(new Maintenance(date, miles, 
						"All maintenance has been deleted.\n"
						+ "However, we saved the most recent date and milage for this vehicle."));
			}
		}
		vehicles.getVehicles().get(vehicleIndex).sortMaintenance();
	}
}
