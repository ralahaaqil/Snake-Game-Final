import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class ThePanel extends JPanel implements ActionListener 
{
	//_____________________________________________________________________________________________________________________________________________
	//					INITIALISING THE VARIABLES
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//					DIMENSIONS
	static final int ScreenWidth = 600; //Width of the screen
	static final int ScreenHeight = 600; //Height of the screen
	static final int UnitSize = 25; //Matrix unit sizes. Each item will have 25 pixels for the width and the height
	static final int GameUnits = (ScreenWidth*ScreenHeight)/UnitSize;
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//					SNAKE
	final int SnakeX[]=new int[GameUnits]; //X coordinate of body part
	final int SnakeY[]=new int[GameUnits]; //Y coordinate of body part
	int BodyParts=6; //Total number of body parts
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//					APPLES
	int ApplesEaten; //Number of apples eaten
	int AppleX; //X coordinate of the apple. Will appear at Random every time
	int AppleY; //Y coordinate of the apple. Will appear at Random every time
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//					MOVEMENT
	char Direction = 'R'; //Direction of the snake
	int Delay = 75; //How fast your game is
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//					MISC
	boolean Running = false;
	boolean Start = false;
	Timer Timer;
	Random Random;
	//_____________________________________________________________________________________________________________________________________________
	//					CONSTRUCTOR
	ThePanel() 
	{
		Random = new Random(); //Creating instance of Random class
		this.setPreferredSize(new Dimension(ScreenWidth,ScreenHeight));
		this.setBackground(Color.black); //Setting background colour
		this.setFocusable(true); //It lets JPanel have the power of getting focused
		this.addKeyListener(new MyKeyAdapter()); //Key listener checks if a key is pressed
		Start(); //Calling Start game method
	}
	//____________________________________________________________________________________________________________________________________________
	//					INTERFACE SCREEN
	public void Interface(Graphics g)
	{
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free",Font.BOLD,50));
		FontMetrics metrics = getFontMetrics(g.getFont()); //To get length of text
		g.drawString("Welcome to Snake Game", (ScreenWidth - metrics.stringWidth("Welcome to Snake Game"))/2, g.getFont().getSize());
		g.setFont(new Font("Ink Free",Font.PLAIN,30));
		FontMetrics metrics1 = getFontMetrics(g.getFont()); //To get length of text
		g.drawString("Use the WASD keys to control the snake", (ScreenWidth - metrics1.stringWidth("Use the WASD keys to control the snake"))/2, 100);
		FontMetrics metrics2 = getFontMetrics(g.getFont()); //To get length of text
		g.drawString("Press [Space] to Start the game", (ScreenWidth - metrics2.stringWidth("Press [Space] to Start the game"))/2, 140);
		FontMetrics metrics3 = getFontMetrics(g.getFont()); //To get length of text
		if (Start == true)
		{
			super.paintComponent(g); //Clears previous stuff on the screen
			Draw(g);
		}
	}
	//__________________________________________________________________________________________________________________________________________
	//					METHOD THAT STARTS THE GAME
	public void Start() 
	{
		NewApple(); //Calling new apple method to create a new apple for us
		Running = true;
		Timer = new Timer(Delay,this); //This will dictate how fast the game is Running
		Timer.start(); //Starts Timer
	}
	//___________________________________________________________________________________________________________________________________________
	//					PAINT COMPONENT
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); //The invocation of super.paintComponent(g) passes the graphics context off to the component's UI delegate, which paints the panel's background.
		Interface(g);
	}
	//___________________________________________________________________________________________________________________________________________
	//					DRAWING THE ITEMS
	public void Draw(Graphics g)
	{
		if (Running)
		{
			//This is just to visualize
			/*for (int i=0;i<ScreenHeight/UnitSize;i++)
			{
				g.drawLine(i*UnitSize, 0, i*UnitSize, ScreenHeight);
				g.drawLine(0, i*UnitSize, ScreenWidth, i*UnitSize);
			}*/
			//------------------------------------------------------------------------------------------------------------------------------------
			//					DRAWING THE APPLE
			g.setColor(Color.red); //Setting colour to red
			g.fillOval(AppleX, AppleY, UnitSize, UnitSize); //How large the apple is
			//------------------------------------------------------------------------------------------------------------------------------------
			//					DRAWING THE SNAKE
			for(int i = 0;i<BodyParts;i++)
			{
				if(i==0)
				{
					g.setColor(Color.green); //Setting head of snake to green
					g.fillRect(SnakeX[i], SnakeY[i], UnitSize, UnitSize); 
				}
				else
				{
					g.setColor(new Color(45,180,0));
					g.fillRect(SnakeX[i], SnakeY[i], UnitSize, UnitSize);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont()); //To get length of text
			g.drawString("Score: "+ApplesEaten, (ScreenWidth - metrics.stringWidth("Score: "+ApplesEaten))/2, g.getFont().getSize());
		}
		
		else
		{
			GameOver(g);
		}
	}
	//______________________________________________________________________________________________________________________________________________
	//					GENERATING A NEW APPLE
	public void NewApple()
	{
		AppleX = Random.nextInt((int)(ScreenWidth/UnitSize))*UnitSize; 
		AppleY = Random.nextInt((int)(ScreenHeight/UnitSize))*UnitSize;
	}
	//______________________________________________________________________________________________________________________________________________
	//					MOVEMENT OF THE SNAKE
	public void Move() 
	{
		//					ITERATING THE BODY PARTS OF THE SNAKE
		for(int i = BodyParts;i>0;i--)
		{
			SnakeX[i] = SnakeX[i-1]; //Shifting coordinates by 1 spot
			SnakeY[i]=SnakeY[i-1];
		}
		//------------------------------------------------------------------------------------------------------------------------------------------
		//					MAKING THE NECESSARY ADJUSTMENTS W.R.T DIRECTION
		switch(Direction)
		{
		case 'U':
			SnakeY[0] = SnakeY[0] - UnitSize;
			break;
		case 'D':
			SnakeY[0] = SnakeY[0] + UnitSize;
			break;
		case 'L':
			SnakeX[0] = SnakeX[0] - UnitSize;
			break;
		case 'R':
			SnakeX[0] = SnakeX[0] + UnitSize;
			break;
		}
	}
	//______________________________________________________________________________________________________________________________________________
	//					CHECKING IF THE SNAKE HAS EATEN THE APPLE
	public void CheckApple()
	{
		if((SnakeX[0]==AppleX) && (SnakeY[0]==AppleY))
		{
			BodyParts++;
			ApplesEaten++;
			NewApple();
		}
	}
	//______________________________________________________________________________________________________________________________________________
	//					CHECKING FOR COLLISIONS
	public void CheckCollisions()
	{
		//					CHECKS IF HEAD TOUCHES BODY PARTS
		for (int i = BodyParts;i>0;i--)
		{
			if((SnakeX[0]==SnakeX[i]) && (SnakeY[0]==SnakeY[i]))
			{
				Running = false;
			}
		}
		//------------------------------------------------------------------------------------------------------------------------------------------
		//					CHECKS IF HEAD TOUCHES THE BORDERS
		if (SnakeX[0] < 0) //Left border
		{
			SnakeX[0]=ScreenWidth;
		}
		if (SnakeX[0] > ScreenWidth) //Right border
		{
			SnakeX[0]=0;
		}
		if (SnakeY[0] < 0) //Top border
		{
			SnakeY[0]=ScreenHeight;
		}
		if (SnakeY[0] > ScreenHeight) //Bottom border
		{
			SnakeY[0]=0;
		}
		
		if (!Running)
		{
			Timer.stop();
		}
	}
	//______________________________________________________________________________________________________________________________________________
	//					GAME OVER SCREEN
	public void GameOver(Graphics g)
	{
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics1 = getFontMetrics(g.getFont()); //To get length of text
		g.drawString("Score: "+ApplesEaten, (ScreenWidth - metrics1.stringWidth("Score: "+ApplesEaten))/2, g.getFont().getSize());
		//Game OVER text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics2 = getFontMetrics(g.getFont()); //To get length of text
		g.drawString("GAME OVER", (ScreenWidth - metrics2.stringWidth("GAME OVER"))/2, ScreenHeight/2);
	}
	//______________________________________________________________________________________________________________________________________________
	//					ACTION PERFORM
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(Running)
		{
			Move(); //Calling move function
			CheckApple(); //Caling CheckApple function to check to see if we've run into the apple
			CheckCollisions(); //Calling CheckCollisions function to check to see if we've collided
		}
		repaint();
		
	}
	//______________________________________________________________________________________________________________________________________________
	//					CHECKING FOR KEYS PRESSED
	public class MyKeyAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_SPACE:
				Start = true;
				break;
			case KeyEvent.VK_A:
				if (Direction != 'R')
				{
					Direction = 'L';
				}
				break;
			case KeyEvent.VK_D:
				if (Direction != 'L')
				{
					Direction = 'R';
				}
				break;
			case KeyEvent.VK_W:
				if (Direction != 'D')
				{
					Direction = 'U';
				}
				break;
			case KeyEvent.VK_S:
				if (Direction != 'U')
				{
					Direction = 'D';
				}
				break;
			}
		}
	}

}
