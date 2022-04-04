import javax.swing.JFrame;

public class TheFrame extends JFrame
{
	TheFrame() //Constructor
	{
		this.add(new ThePanel()); //Adding the Panel
		this.setTitle("Snake Game"); //Setting Java JFrame title
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exits JFrame when the user closes the window
		this.setResizable(false); //Does not allow the user to resize the frame
		this.pack(); //Fits the JFrame snugly around the components
		this.setVisible(true); //Makes the frame appear on the screen
		this.setLocationRelativeTo(null); //Makes the frame appear on the middle of the screen
	}
}
