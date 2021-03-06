import java.awt.*;
import java.awt.event.*;
import java.awt.Font.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.*;


public class BoardUI extends JFrame implements ActionListener
{
    private JMenu options;
	private JMenuItem startNew;
    private JMenuItem resetGame;
    private JMenuItem saveGame;
    private JMenuItem loadGame;
    private JLabel timeLabel;
    private JLabel mineLabel;
	private JButton[][] buttons;
    private Timer timer;
    private Board board;
	private Location grid[][];
	private Font font;
	private ImageIcon icon;
	private Timer clock;
	private Boolean started;
	private int width;
	private int height;
	private int x;
	private int y;
    
    public BoardUI(int width, int height, int bombs)
	{

		board = new Board(width, height, 10);		
		grid = new Location[width][height];
		this.width = width;
		this.height = height;

		// Creates the frame and ads the componets to the frame
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
		
		options = new JMenu("Game");
		startNew = new JMenuItem("Start New Game");
		resetGame = new JMenuItem("Reset Game");
		saveGame = new JMenuItem("Save Game");
		loadGame = new JMenuItem("Load Game");
		timeLabel = new JLabel("0");
		mineLabel = new JLabel(String.valueOf(board.returnBombCount()));
        
        JPanel bottom = new JPanel();
		JPanel layout = new JPanel();
        bottom.setLayout(new FlowLayout());
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
               
        JMenuBar bar = new JMenuBar();
        options.add(startNew);
        options.add(resetGame);
        options.add(saveGame);
        options.add(loadGame);
        startNew.addActionListener(this);
        resetGame.addActionListener(this);
        saveGame.addActionListener(this);
        loadGame.addActionListener(this);
        bar.add(options);
        top.add(bar, BorderLayout.NORTH);
        
        bottom.add(new JLabel("Time: "));
        bottom.add(timeLabel);
        bottom.add(new JLabel("Mine: "));
        bottom.add(mineLabel);
        
        topPanel.add(top, BorderLayout.NORTH);
        topPanel.add(bottom, BorderLayout.SOUTH);
        
		
		layout.setLayout(new GridLayout(width,height));
		
		buttons = new JButton[width][height];
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				buttons[i][j] = new JButton();
				buttons[i][j].addActionListener(this);
				layout.add(buttons[i][j]);
			
				buttons[i][j].addMouseListener(new MouseAdapter()
				{
					//mouse
					public void mouseClicked (MouseEvent e) 
					{	
						if(!started)
						{
							clock = new Timer(1000, timerActionListener);
							clock.start();
							started = true;
						}

						if(e.getButton() == MouseEvent.BUTTON1) 
						{
							for(int i = 0;i < board.returnWidth(); i++) 
							{
								for(int j = 0; j < board.returnHeight(); j++) 
								{
									if(e.getSource().equals(grid[i][j]))
									{
										x = i;
										y = j;
									}
								}	
							}
							
							if (board.returnBomb(x, y))
							{
								if (started)
								{
									clock.stop();
									started = false;
								}
								
								buttons[x][y].setBackground(Color.white);
								icon = new ImageIcon("mine.png");
								
								for(int i = 0;i < board.returnWidth(); i++) 
								{
									for(int j = 0; j < board.returnHeight(); j++) 
									{
										if(board.returnBomb(i, j))
										{
											buttons[i][j].setIcon(icon);
										}
									}	
								}

								JOptionPane.showMessageDialog(null,"Game Over!","",JOptionPane.INFORMATION_MESSAGE);
								board.resetBoard();
								timeLabel.setText("0");
								resetButtons();
							}
							
							else if(board.returnNumber(x, y) != 0)
							{
								int num = board.returnNumber(x, y);
								buttons[x][y].setText(String.valueOf(board.returnNumber(x, y)));
								buttons[x][y].setBackground(Color.white);
								board.setFlipped(x, y, true);
							}
							
							
							else if (board.returnNumber(x, y) == 0)
							{
								// After the number is shown cascade the surrounding numbers
								//buttons[x][y].setText(String.valueOf(board.returnNumber(x, y))); // used for testing purposes only
								cascade(x, y);
							}
							
							if(board.determineWinner() == true)
							{
								if (started)
								{
									clock.stop();
									started = false;
								}
								JOptionPane.showMessageDialog(null, "You have won the game in " + timeLabel.getText() + " seconds! Starting a new game now.", "CONGRATULATIONS!", JOptionPane.INFORMATION_MESSAGE);
								board.resetBoard();
								timer = new Timer(1000, timerActionListener);
								timeLabel.setText("0");
								mineLabel.setText(Integer.toString(board.determineBombCount()));
								resetButtons();
							}
						}
						
						else if(e.getButton() == MouseEvent.BUTTON3) 
						{
							for (int i = 0; i < board.returnWidth(); i++)
							{
								for (int j = 0; j < board.returnHeight(); j++)
								{
									if (e.getSource() == buttons[i][j])
									{
										x = i;
										y = j;
									}
								}
							}
							
							if(flagsAvailable())
							{
								if(!board.returnFlag(x, y))
								{
									board.setFlag(x, y, true);
									icon = new ImageIcon("flag.png");
									buttons[x][y].setIcon(icon);
								}
								
								else 
								{
									board.setFlag(x, y, false);
									buttons[x][y].setIcon(null);
								}
								updateMineNum();
							}
							
							else
							{
								board.setFlag(x, y, false);
								buttons[x][y].setIcon(null);
							}
							
							if(board.determineWinner() == true)
							{
								if (started)
								{
									clock.stop();
									started = false;
								}
								JOptionPane.showMessageDialog(null, "You have won the game in " + timeLabel.getText() + " seconds! Starting a new game now.", "CONGRATULATIONS!", JOptionPane.INFORMATION_MESSAGE);
								board.resetBoard();
								timer = new Timer(1000, timerActionListener);
								timeLabel.setText("0");
								mineLabel.setText(Integer.toString(board.determineBombCount()));
								resetButtons();
							}
						}
						
					}
				});
			}
		}
			
        add(topPanel, BorderLayout.NORTH);
		add(layout, BorderLayout.CENTER);
        
		started = false;
		
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		setTitle("Minesweeper");
        pack();
        setVisible(true);
    }   

    //checks to see which menu item was clicked
    public void actionPerformed(ActionEvent e)
	{
        // creates a new board class and reset the timer and bomb label
        if(e.getSource() == startNew)
		{
            board = new Board();
			this.dispose();
			new BoardUI(board.returnWidth(), board.returnHeight(), board.returnBombCount());
			if (started)
			{
				clock.stop();
				started = false;
			}
			
			resetButtons();
            mineLabel.setText(Integer.toString(board.determineBombCount()));
			timeLabel.setText("0");
            timer = new Timer(1000, timerActionListener);
        }
        //resets the game by calling the board resetBoard class. Resets the timer label and bomb label
        if(e.getSource() == resetGame)
		{
			
            if (started)
			{
				clock.stop();
				started = false;
			}
			
            board.resetBoard();
			resetButtons();
            mineLabel.setText(Integer.toString(board.returnBombCount()));
            timeLabel.setText("0");
            timer = new Timer(1000, timerActionListener);
        }
        
		// Calls the save game function
        if(e.getSource() == saveGame)
        {
            saveGame();
        }
        // class the load game function and starts the timer 
        if(e.getSource() == loadGame)
		{
			Location[][] boardArray = loadGame();
			board = new Board(boardArray);
			timer = new Timer(1000, timerActionListener);
			timer.start();
				
			for(int i = 0; i < width; i++)
			{
				for(int j = 0; j < height; j++)
				{
					if(board.returnFlipped(i, j) == true)
					{
						int num = board.returnNumber(i, j);
						if (num != 0)
						{
							buttons[i][j].setText(String.valueOf(board.returnNumber(i, j)));
							buttons[i][j].setBackground(Color.white);
						}
						
						else
						{
							buttons[i][j].setBackground(Color.white);
						}						
					}
					
					else if(board.returnFlag(i, j) == true)
					{
						icon = new ImageIcon("flag.png");
						buttons[i][j].setIcon(icon);
					}
				}
			}
		}
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				if (e.getSource() == buttons[i][j])
				{
					x = i;
					y = j;
				}
			}
		}
    }
    // controls the timer, increments the time by one every time this action listner fires
    ActionListener timerActionListener = new ActionListener()
	{
        public void actionPerformed(ActionEvent evt)
		{
            int count = Integer.parseInt(timeLabel.getText());
            count++;
            timeLabel.setText(Integer.toString(count));
        }
    };
    //saves the game by wrting the current state of the board class' locaction array to a text file
    public void saveGame()
	{
        try
		{
            FileWriter fstream = new FileWriter("Saved_Game.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            
			//	Writes static game information to file. 
            //	Includes: time, width of the board, height of the board, and bomb count
            out.write(timeLabel.getText() + " ");
            out.write("\n");
            out.write(String.valueOf(board.returnHeight()) + " ");
            out.write("\n");
            out.write(String.valueOf(board.returnWidth()) + " ");
            out.write("\n");
            out.write(String.valueOf(board.returnBombCount()) + " ");
            out.write("\n");
			
            // loops through the board class' location array
            for(int i = 0; i < board.returnWidth(); i++)
			{
                for(int j = 0; j < board.returnHeight(); j++)
				{
                    // if at the current index,  if the location represents a bomb. Write b to the file
                    if(board.returnBomb(i, j))
					{
                        out.write("b");
                        out.write("\n");
                    }
                    
                    // if at the current index, if the location represents a flag. Write f to the file
                    if(board.returnFlag(i, j))
					{
                        out.write("f");
                        out.write("\n");
                    }
                    
                    //If at the current index, if the location is not a flag nor bomb. Write the number of the location to the file
                    else
					{
                        out.write(Integer.toString(board.returnNumber(i, j)));
                        out.write("\n");
                        //If the tile has been flipped by the player, write
                        //the next line has an 'r'. If it has not been
                        //flipped, write the next line as an 'u.'
                        if(board.returnFlipped(i, j) == true)
						{
							out.write("r\n");
                        }
                        else
						{
							out.write("u\n");
                        }
                    }
                }
            }
            out.close();
            
            JOptionPane.showMessageDialog(null, "Your game has been saved.", "Game Saved", JOptionPane.INFORMATION_MESSAGE);
            
        }
        catch(IOException e)
		{
			System.out.println("Unable to write!!");
		}
    }
	
	// Keeps track of number of flags	
	private int numFlags() 
	{
		int count = 0;
		for(int i = 0; i < board.returnWidth(); i++) 
		{
			for(int j = 0; j < board.returnHeight(); j++) 
			{
				if(board.returnFlag(i, j))
				{
					count++;
				}
			}
		}
		return count;
	}
	
	private void updateMineNum() 
	{
		int count = board.returnBombCount() - numFlags();
		mineLabel.setText(String.valueOf(count));
	}
	
	private boolean flagsAvailable() 
	{
		if((board.returnBombCount() - numFlags()) > 0)
			return true;
		else 
			return false;
	}
	
	public void resetButtons()
	{
		for (int i = 0; i < board.returnWidth(); i++)
		{
			for (int j = 0; j < board.returnHeight(); j++)
			{
				buttons[i][j].setBackground(null);
				buttons[i][j].setIcon(null);
				buttons[i][j].setText("");
			}
		}
	}
	
	public static void main(String[] args)
    {
        BoardUI window;
		int width = 0;
		int height = 0;
		int bombs = 0;
        if(args.length == 2)
        {
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
			
            window = new BoardUI(width, height, 10);
        }
		
		if (args.length == 3)
		{
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
			bombs = Integer.parseInt(args[2]);
					
			window = new BoardUI(width, height, bombs);
		}
        else
		{
            window = new BoardUI(8, 8, 10);
		}
    }


    //	Loads the game by reading the contents of the file into a 2-D array of locations
    public Location[][] loadGame()
	{
        int width, height;
        Location[][] boardArray;
        
		// This linked list is used to store the contents of the location array
        LinkedList list = new LinkedList();
        try
		{
            Scanner reader = new Scanner(new File("Saved_Game.txt"));
            
			//	Read in the static game information and assigns to the grid accordingly 
            timeLabel.setText(String.valueOf(reader.nextInt()));
            height = reader.nextInt();
            width = reader.nextInt();
            mineLabel.setText(String.valueOf(reader.nextInt()));
            boardArray = new Location[width][height];
            
			// Read the content of the location array into the linked list
            while(reader.hasNext())
			{
                list.add(reader.next());
            }
			
            reader.close();
            // loops through the 2d array of locations
            for(int i = 0; i < width; i++)
			{
                for (int j = 0; j < height; j++)
				{
                    // if the item in the linked list is a b, then create a new instance of location and set it represent a bomb
                    // and remove the item form the list
                	if (list.peek().toString().equals("b") || list.peek().toString().equals("f"))
                	{
	                    if (list.peek().toString().equals("b"))
						{
	                        boardArray[i][j] = new Location();
	                        boardArray[i][j].setBomb(true);
	                        list.remove();
	                    }
	                    
						// if the item in the linked list is a f, then create a new instance of location and set it represent a flag
	                    // and remove the item form the list
	                    if(list.peek().toString().equals("f"))
						{
	                        boardArray[i][j] = new Location();
	                        boardArray[i][j].setFlag(true);
	                        list.remove();
	                    }
                	}
                    
					// if the item in the linked list is not a b nor f, then create a new instance of location and set its number to what is in the list
                    // and remove the item form the list
                    else
					{
                        boardArray[i][j] = new Location();
                        boardArray[i][j].setNumber(new Integer(list.remove().toString()));
                        
						//Determine whether the tile is flipped or not
                        if(list.peek().toString().equals("r"))
						{
							boardArray[i][j].setFlipped(true);
							list.remove();
                        }
						
                        else if(list.peek().toString().equals("u"))
						{
						  boardArray[i][j].setFlipped(false);
						  list.remove();
                        }
                        else
						{
							JOptionPane.showMessageDialog(null, "An unknown error has occurred. Please try again.", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
            
			JOptionPane.showMessageDialog(null, "Successfully loaded previously saved game.", "Load Game", JOptionPane.INFORMATION_MESSAGE);
            
            return boardArray;
        }
		
        catch(FileNotFoundException e)
		{
            JOptionPane.showMessageDialog(this, "There are no saved games");
            return new Location[0][0];
        }
    }
	
	public void cascade(int x, int y)
	{
		/*
		*	This code structure comes almost directly from the
		*	determineNumberofNearbyMines function from the
		*	Board class. If you need to see the logic of how
		*	this works, look for the comments there.
		*/
		
		for(int i = (x - 1); i < (x + 2); i++)
		{
			if(i >= 0 && i < board.returnWidth())
			{
				for(int j = (y - 1); j < (y + 2); j++)
				{
					if(j >= 0 && j < board.returnHeight())
					{
						if(board.returnNumber(i, j) == 0 && board.returnFlipped(i, j) == false)
						{
						  
						  
						/*
						*	Right here should be the code to "pop"
						*  	the button and make it dissappear, replacing
						*	it with an image for the number or whatever
						*
						*
						*	Changing the number stored in a Location
						*	should ONLY be done in the Cascade function
						*	and NOWHERE else, as it could break things.
						*	It is safe to do here because it keeps the
						*	cascade function from calling itself
						*	endlessly because of the recursion. Besides,
						*	after a Location is cleared from the Board,
						*	the number it stores is permanently irrelevant.
						*/
							  buttons[i][j].setBackground(Color.white);
							  board.setFlipped(i, j, true);
							  cascade(i, j);
						}
					
						if(board.returnFlipped(i, j) == false)
						{
							if(board.returnBomb(i, j) == false)
							{
								//Pop it
								buttons[i][j].setBackground(Color.white);
								buttons[i][j].setText(String.valueOf(board.returnNumber(i, j)));
								board.setFlipped(i, j, true);
							}
						}
					}
				}
			}
		}
	}
}