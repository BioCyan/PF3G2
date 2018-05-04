package driver;

import java.awt.Dimension;
import java.awt.Toolkit;

//Serves as a way for different classes to access the game settings
//There might've been a better way to go about this
public interface GameInterface {
	public final float ACCELERATIONOFGRAVITY = 15, FRICTION = 24, ACCELERATION = 60, MAXSPEED = 6, JUMPSPEED = 6, INAIRFRICTION = 10;
	public static float sensitivity = 5.0f/1000;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
}
