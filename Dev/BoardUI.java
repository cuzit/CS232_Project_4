
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.*;


public class BoardUI extends JFrame implements ActionListener {

    private JMenu options = new JMenu("Game");
    private JMenuItem startNew = new JMenuItem("Start New Game");
    private JMenuItem resetGame = new JMenuItem("Reset Game");
    private JMenuItem saveGame = new JMenuItem("Save Game");
    private JMenuItem loadGame = new JMenuItem("Load Game");
    private JLabel timeLabel = new JLabel("0");
    private JLabel mineLabel = new JLabel ("--");
    private Timer timer;
    private Board board;
    private boolean isGameStarted = false;
    
    public BoardUI(){
     // creates the frame and ads the componets to the frame
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        
        JPanel bottom = new JPanel();
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
        
        add(topPanel, BorderLayout.NORTH);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }   

    //checks to see which menu item was clicked
    public void actionPerformed(ActionEvent e) {
        // creates a new board class and reset the timer and bomb label
        if(e.getSource() == startNew){
            if(isGameStarted){
                timer.stop();
                timeLabel.setText("0");
            }
            board = new Board();
            mineLabel.setText(Integer.toString(board.returnBombCount()));
            timer = new Timer(1000, taskPerformer);
            timer.start();
            isGameStarted = true;
        }
        //resets the game by calling the board resetBoard class. Resets the timer label and bomb label
        if(e.getSource() == resetGame){
            timer.stop();
            board.resetBoard();
            mineLabel.setText(Integer.toString(board.returnBombCount()));
            timeLabel.setText("0");
            timer = new Timer(1000, taskPerformer);
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
            timer = new Timer(1000, taskPerformer);
            timer.start();
        }
    }
    // controls the timer, increments the time by one every time this action listner fires
    ActionListener taskPerformer = new ActionListener(){
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
    //loads the game by reading the contents of the file into a 2-d array of locations
    public Location[][] loadGame(){
        int rows, cols;
        Location[][] boardArray;
        //linked list used to store the contents of the locatoin array
        LinkedList list = new LinkedList();
        try{
            Scanner reader = new Scanner(new File("Saved_Game.txt"));
            //reads in the static game information and assigns the lable accordingly 
            timeLabel.setText(String.valueOf(reader.nextInt()));
            rows = reader.nextInt();
            cols = reader.nextInt();
            mineLabel.setText(String.valueOf(reader.nextInt()));
            boardArray = new Location[rows][cols];
            //reads the content of the location array into a linked list
            while(reader.hasNext()){
                list.add(reader.next());
            }
            reader.close();
            // loops through the 2d array of locations
            for(int i = 0; i < rows; i++){
                for (int j = 0; j < cols; j++){
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
    
    public static void main(String[] args) {
      BoardUI boardtest = new BoardUI();
      boardtest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      boardtest.setTitle("MineSweeper BoardUI Test");
      boardtest.pack();
      boardtest.setVisible(true);
    }
}
