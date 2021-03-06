package driver;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
public class Menu implements driver.GameInterface{
	
	//Display the startScreen
	public static void startScreen(Graphics graphics) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 30));
		graphics.setColor(Color.cyan);
		graphics.drawString("The J.U.M.P. Unusual Multi-dimensional Platformer", 60, (int)(screenSize.getHeight()/2)); 
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 20 ));
		graphics.setColor(Color.white);
		graphics.drawString("Press Enter to Begin!", 60, (int)(screenSize.getHeight()/2+30));	
		graphics.drawString("PRESS 'I' FOR INSTRUCTION MENU", 60, (int)(screenSize.getHeight()/2+60));
	}
	
	//Display the pauseScreen
	public static void pauseScreen(Graphics graphics) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 30));
		graphics.setColor(Color.white);
		graphics.drawString("GAME PAUSED", 60, (int)(screenSize.getHeight()/2)); 
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 20 ));
		graphics.drawString("PRESS 'P' TO UNPAUSE", 60, (int)(screenSize.getHeight()/2+20));
	}
	
	//Display instructionScreen
	public static void instructionScreen(Graphics graphics) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 30));
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		graphics.setColor(Color.white);
		graphics.drawString("---GAME INSTRUCTIONS---", 60, (int)(screenSize.getHeight()/2));
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 20));
		graphics.drawString("Press Enter to Begin!", 60, (int)(screenSize.getHeight()/2+30));	
		graphics.drawString("Use W/A/S/D to move around, space to jump, and P to pause.", 60, (int)(screenSize.getHeight()/2+60));
	}
	
	//Display endScreen
	public static void endScreen(Graphics graphics, String finalTime) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 50));
		graphics.setColor(Color.red);
		graphics.drawString("GAME OVER", 60, (int)(screenSize.getHeight()/2)); 
		graphics.drawString("PRESS 'R' TO RESTART", 60, (int)(screenSize.getHeight()/2+40));
		graphics.setColor(Color.white);
		graphics.drawString(finalTime, 60, (int)(screenSize.getHeight()/2+80)); 
	}
}


