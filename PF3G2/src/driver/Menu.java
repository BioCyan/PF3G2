package driver;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
public class Menu implements driver.GameInterface{
	public static void startScreen(Graphics graphics) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 30));
		graphics.setColor(Color.cyan);
		graphics.drawString("The J.U.M.P. Unusual Multi-dimensional Platformer", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2)); 
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 20 ));
		graphics.setColor(Color.white);
		graphics.drawString("Press Enter to Begin!", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2+20));	
		graphics.drawString("PRESS 'I' FOR INSTRUCTION MENU", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2+60));
	}
	
	public static void pauseScreen(Graphics graphics) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 30));
		graphics.setColor(Color.white);
		graphics.drawString("GAME PAUSED", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2)); 
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 20 ));
		graphics.drawString("PRESS 'P' TO UNPAUSE", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2+20));
	}
	public static void instructionScreen(Graphics graphics) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 30));
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		graphics.setColor(Color.white);
		graphics.drawString("---GAME INSTRUCTIONS---", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2));
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 20));
		graphics.drawString("Press Enter to Begin!", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2+20));	
		graphics.drawString("Use W/A/S/D to move around, space to jump, and P to pause.", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2+40));
	}
	public static void endScreen(Graphics graphics) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 50));
		graphics.setColor(Color.red);
		graphics.drawString("GAME OVER", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2)); 
		graphics.drawString("PRESS 'R' TO RESTART", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2+40));
	}
}


