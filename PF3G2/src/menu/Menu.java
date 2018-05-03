package menu;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
public class Menu implements gameInterface.GameInterface{
	public static void startScreen(Graphics graphics) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 30));
		graphics.setColor(Color.cyan);
		graphics.drawString("3D MAZE", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2)); 
		graphics.setColor(Color.white);
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 20 ));
		graphics.drawString("Press Enter to Begin!", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2+20));	
	}
	
	public static void pauseScreen(Graphics graphics) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 30));
		graphics.setColor(Color.white);
		graphics.drawString("GAME PAUSED", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2)); 
		graphics.setColor(Color.white);
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 20 ));
		graphics.drawString("PRESS 'P' TO UNPAUSE", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2+20));		
	}
	
	public static void endScreen(Graphics graphics) {
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 50));
		graphics.setColor(Color.red);
		graphics.drawString("GAME OVER", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2)); 
		graphics.drawString("PRESS 'R' TO RESTART", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2+40));
	}
}

