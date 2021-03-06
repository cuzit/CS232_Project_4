import java.util.Random;

public class Board {
  /**********
  *Variables*
  ***********/
  //Integers
  private int width;
  private int height;
  private int bombCount;
  
  //Board Array
  private Location[][] board;
  
  //Random generation
  Random rand = new Random();
  
  
  /*************
  *Constructors*
  **************/
  //Constructor called when no arguments are passed that
  //sets all value to their defaults
  public Board() {
    this.width = 8;
    this.height = 8;
    this.bombCount = determineBombCount();
    board = new Location[this.width][this.height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
	board[i][j] = new Location();
	board[i][j].setFlag(false);
	board[i][j].setBomb(false);
	board[i][j].setNumber(-1);
      }
    }
    placeBombs();
    
    for(int i = 0; i < width; i++) {
      for(int j = 0; j < height; j++) {
	int temp = determineNumberOfNearbyMines(i, j);
	board[i][j].setNumber(temp);
      }
    }
  }
  
  //Constructor for creating the board when only one
  //dimension (width or height) is passed. Assumes
  //both dimensions are the same (i.e. width = height).
  public Board(int dimension) {
    this.width = dimension;
    this.height = dimension;
    this.bombCount = determineBombCount();
    board = new Location[this.width][this.height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
	board[i][j] = new Location();
	board[i][j].setFlag(false);
	board[i][j].setBomb(false);
	board[i][j].setNumber(-1);
      }
    }
    placeBombs();
    
    for(int i = 0; i < width; i++) {
      for(int j = 0; j < height; j++) {
	int temp = determineNumberOfNearbyMines(i, j);
	board[i][j].setNumber(temp);
      }
    }
  }
  
  //Constructor called when both width and height are
  //specified.
  public Board(int width, int height) {
    this.width = width;
    this.height = height;
    this.bombCount = determineBombCount();
    board = new Location[this.width][this.height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
	board[i][j] = new Location();
	board[i][j].setFlag(false);
	board[i][j].setBomb(false);
	board[i][j].setNumber(-1);
      }
    }
    placeBombs();
    
    for(int i = 0; i < width; i++) {
      for(int j = 0; j < height; j++) {
	int temp = determineNumberOfNearbyMines(i, j);
	board[i][j].setNumber(temp);
      }
    }
  }
  
  /**********
  *Functions*
  ***********/
  //Formula to determine how many bombs should be on the board.
  //Returns this value as an integer.
  private int determineBombCount() {
    //Variables
    int seed = 0;
    int value = 0;
    
    //Stay in the loop until the number of value is under
    //a certain size. This allows the amount of bombs to
    //be somewhat random, but within an acceptable range
    //(i.e. not having too many bombs that would cause
    //the game to be unfun to play).
//    do {
//      //Generate random seed
//      while (seed == 0) {
//	if(rand.nextInt() == 1) {
//	  seed = rand.nextInt(this.width);
//	}
//	
//	else {
//	  seed = rand.nextInt(this.height);
//	}
//      }
//      
//      //Set the number of bombs based on the seed
//      value = ((this.width * this.height) / (seed * 2));
//    } while (value > (this.width * this.height) / 3);
    
    //Return the value
    while (value < 4 || value > 12){
        value = rand.nextInt(this.width * this.height);
        System.out.println(value);
    }
    return value;
  }
  
  //Set-Up Bombs
  private void placeBombs() {
    int x;
    int y;
    Boolean success;
    for(int i = 0; i < this.bombCount; i++) {
      do {
	success = false;
	x = rand.nextInt(this.width);
	y = rand.nextInt(this.height);
	if (!board[x][y].hasBomb()) {
	  success = board[x][y].setBomb(true);
	}
      } while(success == false);
    }
  }
  
  //Determine the number of adjacent cells that contain mines
  private int determineNumberOfNearbyMines(int x, int y) {
    int count = 0; //Counts the number of nearby mines
    
    //Go through each row, starting one position to the left
    //of the x value passed and ending one position to the right
    for(int i = (x - 1); i < (x + 2); i++) {
      //Check to see if the i value is actually within the
      //grid (i.e. if x=0, i = x - 1 = -1, i cannot be -1).
      if(i >= 0 && i < this.width) {
	//Go through each column (of each row), starting one
	//position up from the y value passed and ending
	//one position down.
	for(int j = (y - 1); j < (y + 2); j++) {
	  //Check if j is within an acceptable range.
	  if(j >= 0 && j < this.height) {
	    //If the position contains a bomb, increase
	    //count by 1.
	    if(board[i][j].hasBomb()) {
	      count++;
	    }
	  }
	}
      }
    }
    
    //Return the number of mines
    return count;
  }
  
  /**Board Reset**/
  //Creates a new board with the same dimensions as the
  //current board
  public void resetBoard() {
    board = new Location[this.width][this.height];
    for(int i = 0; i < this.width; i++) {
      for(int j = 0; j < this.height; j++) {
	board[i][j] = new Location();
	board[i][j].setFlag(false);
	board[i][j].setBomb(false);
	board[i][j].setNumber(-1);
      }
    }
    
    placeBombs();
    this.bombCount = determineBombCount();
    
    for(int i = 0; i < width; i++) {
      for(int j = 0; j < height; j++) {
	int temp = determineNumberOfNearbyMines(i, j);
	board[i][j].setNumber(temp);
      }
    }
  }
  
  //Creates a new board with the specified dimensions,
  //throwing away the dimensions of the current board
  public void resetBoard(int width, int height) {
    board = new Location[width][height];
    this.width = width;
    this.height = height;
    for(int i = 0; i < this.width; i++) {
      for(int j = 0; j < this.height; j++) {
	board[i][j] = new Location();
	board[i][j].setFlag(false);
	board[i][j].setBomb(false);
	board[i][j].setNumber(-1);
      }
    }
    
    placeBombs();
    this.bombCount = determineBombCount();
    
    for(int i = 0; i < width; i++) {
      for(int j = 0; j < height; j++) {
	int temp = determineNumberOfNearbyMines(i, j);
	board[i][j].setNumber(temp);
      }
    }
  }
  
  /**Getters**/
  //Get the total number of bombs in the game
  public int returnBombCount() {
    return bombCount;
  }
  
  //Get the width of the board
  public int returnWidth() {
    return this.width;
  }
  
  //Get the height of the board
  public int returnHeight() {
    return this.height;
  }
  
  //Return total size of the board
  public int returnSize() {
    return this.width * this.height;
  }
  
  //Return whether or not a bomb exists at a location
  public boolean returnBomb(int x, int y) {
    return board[x][y].hasBomb();
  }
  
  //Return whether or not a flag exists at a location
  public boolean returnFlag(int x, int y) {
    return board[x][y].hasFlag();
  }
  
  //Return the distance to the closest bomb at a location
  public int returnNumber(int x, int y) {
    return board[x][y].getNumber();
  }
  
  /**Setters**/
  
  
  /**toString**/
  public String toString() {
    String s = "";
    
    for(int i = 0; i < (this.width * 2) + 2; i++) {
      s += "-";
    }
    
    s += "\n";
    
    for(int i = 0; i < this.width; i++) {
      s += "|";
      for(int j = 0; j < this.height; j++) {
	if(board[i][j].hasBomb() == true) {
	  s += "* ";
	}
	
	else if (board[i][j].hasFlag()) {
	  s += "F ";
	}
	
	else {
	  s += board[i][j].getNumber() + " ";
	}
      }
      
      s += "|\n";
    }
    
    for(int i = 0; i < (this.width * 2) + 2; i++) {
      s += "-";
    }
    
    return s;
  }
  
  
  /*******
  *Driver*
  ********/
  public static void main(String[] args) {
    System.out.println("This main is just a driver and should");
    System.out.println("not be called except for debugging.");
    
    int width = 24;
    int height = 24;
    
    System.out.println("==Empty Constructor Tests============");
    Board emptyTest = new Board();
    System.out.println("Number of bombs: " + emptyTest.returnBombCount());
    System.out.println("Width: " + emptyTest.returnWidth());
    System.out.println("Height: " + emptyTest.returnHeight());
    System.out.println("Size: " + emptyTest.returnSize());
    System.out.println(emptyTest.toString());
    
    
    System.out.println();
    System.out.println("==One-Dimension Constructor Test=====");
    Board oneTest = new Board(width / 2);
    System.out.println("Number of bombs: " + oneTest.returnBombCount());
    System.out.println("Width: " + oneTest.returnWidth());
    System.out.println("Height: " + oneTest.returnHeight());
    System.out.println("Size: " + oneTest.returnSize());
    System.out.println(oneTest.toString());
    
    
    System.out.println();
    System.out.println("==Two-Dimension Constructor Test=====");
    Board twoTest = new Board(width, height);
    System.out.println("Number of bombs: " + twoTest.returnBombCount());
    System.out.println("Width: " + twoTest.returnWidth());
    System.out.println("Height: " + twoTest.returnHeight());
    System.out.println("Size: " + twoTest.returnSize());
    System.out.println(twoTest.toString());
    
    
    //Test resetting the board
    System.out.println("Testing resetting the board.");
    
    System.out.println();
    System.out.println("==Resetting - Same Dimensions========");
    
    emptyTest.resetBoard();
    System.out.println("Empty Constructor:");
    System.out.println("Number of bombs: " + emptyTest.returnBombCount());
    System.out.println("Width: " + emptyTest.returnWidth());
    System.out.println("Height: " + emptyTest.returnHeight());
    System.out.println("Size: " + emptyTest.returnSize());
    System.out.println(emptyTest.toString());
    
    oneTest.resetBoard();
    System.out.println("One-Dimension Constructor:");
    System.out.println("Number of bombs: " + oneTest.returnBombCount());
    System.out.println("Width: " + oneTest.returnWidth());
    System.out.println("Height: " + oneTest.returnHeight());
    System.out.println("Size: " + oneTest.returnSize());
    System.out.println(oneTest.toString());
    
    twoTest.resetBoard();
    System.out.println("Two-Dimension Constructor:");
    System.out.println("Number of bombs: " + twoTest.returnBombCount());
    System.out.println("Width: " + twoTest.returnWidth());
    System.out.println("Height: " + twoTest.returnHeight());
    System.out.println("Size: " + twoTest.returnSize());
    System.out.println(twoTest.toString());
    
    System.out.println();
    System.out.println("==Resetting - Diff Dimesnions========");
    
    width = width / 2;
    height = height / 2;
    
    emptyTest.resetBoard(width, height);
    System.out.println("Empty Constructor:");
    System.out.println("Number of bombs: " + emptyTest.returnBombCount());
    System.out.println("Width: " + emptyTest.returnWidth());
    System.out.println("Height: " + emptyTest.returnHeight());
    System.out.println("Size: " + emptyTest.returnSize());
    System.out.println(emptyTest.toString());
    
    oneTest.resetBoard(width, height);
    System.out.println("One-Dimension Constructor:");
    System.out.println("Number of bombs: " + oneTest.returnBombCount());
    System.out.println("Width: " + oneTest.returnWidth());
    System.out.println("Height: " + oneTest.returnHeight());
    System.out.println("Size: " + oneTest.returnSize());
    System.out.println(oneTest.toString());
    
    twoTest.resetBoard(width, height);
    System.out.println("Two-Dimension Constructor:");
    System.out.println("Number of bombs: " + twoTest.returnBombCount());
    System.out.println("Width: " + twoTest.returnWidth());
    System.out.println("Height: " + twoTest.returnHeight());
    System.out.println("Size: " + twoTest.returnSize());
    System.out.println(twoTest.toString());
    
  }
}