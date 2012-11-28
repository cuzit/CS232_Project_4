public class Minesweeper
{
	public static void main(String[] args)
    {
        BoardUI window;
        if(args.length == 2)
        {
			/*
			*	Since args is an array to strings just
			*	take the next 2 strings after the name
			*	of the class and use it to get the height
			*	and width and pass it to BoardUI
			*/
			int width = Integer.parseInt(args[0]);
			int height = Integer.parseInt(args[1]);
			
			/*
			*	Instantiate BoardUI using the width and height
			*	provided by the user
			*/
            window = new BoardUI(width, height);
        }
        else
		{
			/*
			*	If the user doesn't specify a width and height
			*	then use an 8 x 8 grid as the default
			*/
            window = new BoardUI(8,8);
		}
    }
}