public class Minesweeper
{
	public static void main(String[] args)
    {
        BoardUI window;
		int width = 0;
		int height = 0;
		int bombs = 0;
        if(args.length == 2)
        {
			/*
			*	Since args is an array to strings just
			*	take the next 2 strings after the name
			*	of the class and use it to get the height
			*	and width and pass it to BoardUI
			*/
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
			
			/*
			*	Instantiate BoardUI using the width and height
			*	provided by the user
			*/
            window = new BoardUI(width, height, 10);
        }
		
		else if (args.length == 3)
		{
			/*
			*	Since args is an array to strings just
			*	take the next 2 strings after the name
			*	of the class and use it to get the width,
			*	height, and number of bombs to pass to BoardUI
			*/
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
			bombs = Integer.parseInt(args[2]);
			
			/*
			*	Instantiate BoardUI using the width, height,
			*	and number of bombs provided by the user
			*/			
			window = new BoardUI(width, height, bombs);
		}
        else
		{
			/*
			*	If the user doesn't specify a width and height
			*	then use an 8 x 8 grid as the default
			*/
            window = new BoardUI(8, 8, 10);
		}
    }
}