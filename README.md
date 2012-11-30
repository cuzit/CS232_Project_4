CS232_Project_4
===============
CS232 - JSU - Project 4 - Matt Silvey, Tyler Hughes, Crystal Williams, Jackie Blue, Aaron Rudolph
Minesweeper
Version 1.00

1. Parts

There are four classes to Minesweeper.

Minesweeper - The main. Calls other classes.
Location - An individual position in the grid, storing information
about that spot, such as whether or not it has been flipped,
whether or not it store a bomb or a flag, and how many mines
surround it.
Board - Creates the Minesweeper board, a 2D array of Location
objects. It contains functions for interacting with the board,
such as returning/setting values, resetting the board, determing
whether or not winning conditions have been met, and so forth.
BoardUI - The GUI frontend of the Board. All other functions,
such as the menu and the timer, are stored here as well.

2. Instructions

To use the program, you can either use the classes in the Precompiled 
Classes folder, or you can compile the program yourself.

To run the precompiled program, cd to the Precompiled Classes folder and 
type 'java Minesweeper {arg1} {arg2} {arg3}'

arg1 - The width of the board (optional).
arg2 - The height of the board (optional).
arg3 - The number of mines in the board (optional).

All of the arguments are optional.  For example, if you wanted
to play a game of Minesweeper that is 10x10:

java Minesweeper 10 10

If you want to play a game of Minesweeper that is 25x25 with 100
bombs:

java Minesweeper 25 25 100

If you want to play a game with default conditions (8x8 board, 10
bombs):

java Minesweeper

3. How to Play

The goal of Minesweeper is to clear all tiles in the board
without selecting any that hide a mine while flagging all
locations that contain a mine.  To assist you, when a tile
is cleared, a number appears showing the number of mines that
surround the tile.

Left-click to select a tile for clearing.

Right-click to place a flag. Right-click a tile with a flag to
remove the flag. You can only place as many flags as there are
mines on the board.

Watch the timer and try to win the game as fast as possible!
But be careful - if you select a tile with a mine, it's game over!

4. Credits

Matt Silvey - Team Leader, Board, BoardUI (cascade function,
determining a winner, and bug-fixing)
Tyler Hughes - Location, Minesweeper (Main), BoardUI, bug-fixing
Crystal Williams - BoardUI (top panel, saving, loading, etc),
bug-fixing
Jackie Blue - BoardUI bottom panel GUI
