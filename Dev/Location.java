public class Location
{
	/*
	*	The board is basically a 2D array of locations in which
	*	either a flag, bomb, or number will be stored.
	*
	*	This class contains methods which get and set whether
	*	each location contains either a flag, bomb, or number.
	*	It also has a method which allows you to get and set
	*	the distance from the nearest mine.
	*/


	//	Declare all variables
	boolean flag;
	boolean bomb;
	boolean numberBool;
	int number;
  
	//	Constructor
	public Location()
	{
		//Commented out because when the board calls it a million
		//times, there's way too much shit being printed to the
		//terminal
		//System.out.println("Temporary constructor to build the board.");
		//System.out.println("If this printed, that means it's working!");
		flag = false;
		bomb = false;
		numberBool = false;
		number = 0;
		
	}
	  
	//	Getters
	public Boolean hasFlag()
	{
		return flag;
	}
	
	public Boolean hasBomb()
	{
		return bomb;
	}
	
	public Boolean hasNumber()
	{
		return numberBool;
	}
	
	// Setters
	
	// Returns a boolean so that you know whether not the flag was
	// successfully set
	public Boolean setFlag(boolean flag)
	{
		this.flag = flag;
		return true;
	}
	
	// Returns a boolean so that you know whether not the bomb was
	// successfully set
	public Boolean setBomb(boolean bomb)
	{
		this.bomb = bomb;
		return this.bomb;
	}
	
	// Returns a boolean so that you know whether not the number in the
	// location was set successfully
	public Boolean setNumber(int number)
	{
		this.number = number;
		return true;
	}
	
	//	Driver
	public static void main(String[] args)
	{
	
	}
}