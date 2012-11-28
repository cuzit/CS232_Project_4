import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.*;


public class BoardUI extends JFrame {

    private JMenu options = new JMenu("Game");
    private JMenuItem startNew = new JMenuItem("Start New Game");
    private JMenuItem resetGame = new JMenuItem("Reset Game");
    private JMenuItem saveGame = new JMenuItem("Save Game");
    private JMenuItem loadGame = new JMenuItem("Load Game");
    private JLabel timeLabel = new JLabel("0");
    private JLabel mineLabel = new JLabel ("--");
    private Timer timer;
    private Board board;
	private Location[][] grid;
	private ImageIcon flagIcon;
	private Timer clock;
	private Boolean started;
	private int width;
	private int height;
    
    public BoardUI(int width, int height){
		this.width = width;
		this.height = height;

	// creates the frame and ads the componets to the frame
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        
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
		board = new Board(width, height);
		grid = new Location(width, height);
		JButton[][] buttons = new JButton[width][height];
		for(int i = 0; i < width; i++)
			{
			for( int j = 0; j < height; j++)
				{
					buttons[i][j] = new JButton();
					buttons[i][j].addActionListener(this);
					layout.add(buttons[i][j]);
					}
				}
		bottomPanel.add(layout);
        add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
        
		started = false;
		buttons.addMouseListener(new MouseAdapter()
		{
			//mouse
			public void mousePressed (MouseEvent e) 
			{	
			
				if(!started)
				{
					clock = new Timer(1000, timerActionListener);
					clock.start();
					started = true;
				}
				int width = 0;
				int height = 0;
				for(int i = 0;i < width;i++) 
				{
					for(int j = 0;j < height;j++) 
					{
						if(e.getSource().equals(grid[i][j]))
						{
							width = i;
							height = j;
						}
					}
					
				}
				if(e.getButton() == MouseEvent.BUTTON1) 
				{ 
					cascade(width,height);
					board[width][height].show(grid[width][height]);
					if(grid[width][height].hasNumber())
					{
						int num = grid.getNumber();
						switch(num)
						{
							case 1:flagIcon = new ImageIcon("1.png");
							case 2:flagIcon = new ImageIcon("2.png");
							case 3:flagIcon = new ImageIcon("3.png");
							case 4:flagIcon = new ImageIcon("4.png");
							case 5:flagIcon = new ImageIcon("5.png");
							case 6:flagIcon = new ImageIcon("6.png");
							case 7:flagIcon = new ImageIcon("7.png");
							case 8:flagIcon = new ImageIcon("8.png");
						}
					}
					if(grid[width][height] == -1)
					{
						JOptionPane.showMessageDialog(this,"Game Over!","",JOptionPane.INFORMATION_MESSAGE);
						board.resetBoard();
					}
					if(isWinner())
					{
						JOptionPane.showMessageDialog(this, "YOU WIN! Starting New Game", "CONGRATULATIONS", JOptionPane.INFORMATION_MESSAGE);
						board.resetBoard();
					}
				}
				else if(e.getButton() == MouseEvent.BUTTON3) 
				{
					if(flagsAvailable())
					{
						if(!grid[width][height].hasFlag())
						{
							grid[width][height].setFlag(true);
							ImageIcon flagIcon = new ImageIcon("flag.png");
						}
						else 
						{
							 
								board[width][height].setFlag(false);
						}	
					}
				}
				updateMineNum();
			}
		});
		
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }   

    //checks to see which menu item was clicked
    public void actionPerformed(ActionEvent e) {
        // creates a new board class and reset the timer and bomb label
        if(e.getSource() == startNew){
            board = new Board();
            mineLabel.setText(Integer.toString(board.returnBombCount()));
            timer = new Timer(1000, timerActionListener);
            timer.start();
        }
        //resets the game by calling the board resetBoard class. Resets the timer label and bomb label
        if(e.getSource() == resetGame){
            timer.stop();
            board.resetBoard();
            mineLabel.setText(Integer.toString(board.returnBombCount()));
            timeLabel.setText("0");
            timer = new Timer(1000, timerActionListener);
            timer.start();
        }
        // calls the save game function
        if(e.getSource() == saveGame){
            saveGame();
        }
        // class the load game function and starts the timer 
        if(e.getSource() == loadGame){
            Location[][] boardArray = loadGame();
            //set board class with this location array
            // to load a game we need to be able to pass the board class a 2d array of locations 
            // that represent the loaded game. One way to due this is to create a contructor in the board class that 
            // takes in a 2d array of locations as a parameter. Then set all the private attributes 
            // of the board based on the 2d array passed in.
            timer = new Timer(1000, timerActionListener);
            timer.start();
        }
    }
    // controls the timer, increments the time by one every time this action listner fires
    ActionListener timerActionListener = new ActionListener(){
        public void actionPerformed(ActionEvent evt) {
            int count = Integer.parseInt(timeLabel.getText());
            count++;
            timeLabel.setText(Integer.toString(count));
        }
    };
    //saves the game by wrting the current state of the board class' locaction array to a text file
    public void saveGame(){
        try{
            FileWriter fstream = new FileWriter("Saved_Game.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            // writes static game information to file. 
            //Includes: time, width of the board, heigth of the board, and bomb count
            out.write(timeLabel.getText() + " ");
            out.write("\n");
            out.write(String.valueOf(board.returnHeight()) + " ");
            out.write("\n");
            out.write(String.valueOf(board.returnWidth()) + " ");
            out.write("\n");
            out.write(String.valueOf(board.returnBombCount()) + " ");
            out.write("\n");
            // loops throgth the board class' locaction array
            for(int i = 0; i < board.returnHeight(); i++){
                for(int j = 0; j < board.returnWidth(); j++){
                    // if at the current index,  if the location represents a bomb. Write b to the file
                    if(board.returnBomb(i, j)){
                        out.write("b");
                        out.write("\n");
                    }
                    // if at the current index, if the location represents a flag. Write f to the file
                    else if(board.returnFlag(i, j)){
                        out.write("f");
                        out.write("\n");
                    }
                    //If at the current index, if the locaitn is not a flag nor bomb. Write the number of the location to the file
                    else{
                        out.write(Integer.toString(board.returnNumber(i, j)));
                        out.write("\n");
                    }
                }
            }
            out.close();
            
        }
        catch(IOException e) { System.out.println("Unable to write!!");}
    }
	
//keeps track of number of flags	
private int numFlags() 
	{
		int count = 0;
		for(int i = 0; i < width; i++) 
		{
			for(int j = 0; j < height; j++) 
			{
				if(grid[i][j].hasFlag())
					count++;
			}
		}
		return count;
	}
	private void updateMineNum() 
	{
		int count = board.determineBombCount - numFlags();
		mineLabel.setText(String.valueOf(count));			
	}
	private boolean flagsAvailable() 
	{
		if((board.determineBombCount - numFlags()) > 0)
			return true;
		else 
			return false;
	}
	
public static void main(String[] args)
	{
		BoardUI window;
		if(args.length == 2)
		{
			window = new BoardUI(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		}
		else
			window = new BoardUI(8,8);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


    //loads the game by reading the contents of the file into a 2-d array of locations
    public Location[][] loadGame(){
        int width, height;
        Location[][] boardArray;
        //linked list used to store the contents of the locatoin array
        LinkedList list = new LinkedList();
        try{
            Scanner reader = new Scanner(new File("Saved_Game.txt"));
            //reads in the static game information and assigns the lable accordingly 
            timeLabel.setText(String.valueOf(reader.nextInt()));
            width = reader.nextInt();
            height = reader.nextInt();
            mineLabel.setText(String.valueOf(reader.nextInt()));
            boardArray = new Location[width][height];
            //reads the content of the location array into a linked list
            while(reader.hasNext()){
                list.add(reader.next());
            }
            reader.close();
            // loops through the 2d array of locations
            for(int i = 0; i < width; i++){
                for (int j = 0; j < height; j++){
                    // if the item in the linked list is a b, then create a new instance of location and set it represent a bomb
                    // and remove the item form the list
                    if (list.peek().toString().equals("b")){
                        boardArray[i][j] = new Location();
                        boardArray[i][j].setBomb(true);
                        list.remove();
                    }
                    // if the item in the linked list is a f, then create a new instance of location and set it represent a flag
                    // and remove the item form the list
                    else if(list.peek().toString().equals("f")){
                        boardArray[i][j] = new Location();
                        boardArray[i][j].setFlag(true);
                        list.remove();
                    }
                    // if the item in the linked list is not a b nor f, then create a new instance of location and set its number to what is in the list
                    // and remove the item form the list
                    else{
                        boardArray[i][j] = new Location();
                        boardArray[i][j].setNumber(new Integer(list.remove().toString()));
                    }
                }
            }
            return boardArray;
            
        }
        catch(FileNotFoundException e){
            System.out.println(e.toString());
            return new Location[0][0];
        }
    }
        
}