public class Cascade {
  Board board;
  
  public Cascade(Board board) {
    this.board = board;
  }
  
public void cascade(int x, int y)
{
    //This code structure comes almost directly from the
    //determineNumberofNearbyMines function from the
    //Board class. If you need to see the logic of how
    //this works, look for the comments there.
    for(int i = (x - 1); i < (x + 2); i++)
	{
		if(i >= 0 && i < board.returnWidth())
		{
			for(int j = (y - 1); j < (y + 2); j++)
			{
				if(j >= 0 && j < board.returnHeight())
				{
					if(board.returnNumber(i, j) == 0)
					{
						//Right here should be the code to "pop"
						//the button and make it dissappear, replacing
						//it with an image for the number or whatever
					  
						//Changing the number stored in a Location
						//should ONLY be done in the Cascade function
						//and NOWHERE else, as it could break things.
						//It is safe to do here because it keeps the
						//cascade function from calling itself
						//endlessly because of the recursion. Besides,
						//after a Location is cleared from the Board,
						//the number it stores is permanently irrelevant.
						board.setNumber(i, j, -1);
				  
						cascade(i, j);
					}
				}
			}
		}
    }
}
  
  public static void main(String[] args) {
    System.out.println("Running...");
    Board testBoard = new Board(12, 12);
    Cascade temp = new Cascade(testBoard);
    System.out.println("==Before:=====================");
    System.out.println(testBoard.toString());
    int x = 0; int y = 0; int value = -1;
    for(int i = 0; i < testBoard.returnWidth() && value != 0; i++) {
      for(int j = 0; j < testBoard.returnHeight() && value != 0; j++) {
	value = testBoard.returnNumber(i, j);
	if(value == 0) {
	  x = i;
	  y = j;
	}
      }
    }
    temp.cascade(x, y);
    System.out.println("==After:======================");
    System.out.println(testBoard.toString());
  }
}