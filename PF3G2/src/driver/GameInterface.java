package driver;

import java.awt.Dimension;
import java.awt.Toolkit;

public interface GameInterface {
	public final float ACCELERATIONOFGRAVITY=15,FRICTION=24,ACCELERATION=60, MAXSPEED=6, JUMPSPEED = 16;
	public float sensitivity = 5.0f/1000;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
}
