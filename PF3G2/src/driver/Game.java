package driver;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import maze.Maze;
import math.*;
import model.*;
import player.Player;
import player.UserMovement;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;

	private List<Poly> polygons;
	private List<Block> blocks;
	private Dimension screenSize;
	private long time;
	private long lastFPSCheck;
	private long lastRepaint;
	private float sensitivity = 5.0f/1000;
	private float yawAngle = 0;
	private float pitchAngle = 0;
	private int frameCount = 0;
	private float moveX;
	private float moveZ;
	private float moveSpeed;
	private boolean start = false;
	private BSPTree tree;
	Player player;
	
	public Game(Dimension screenSize) {
		setFocusable(true);
		
		moveSpeed = 4;
		Maze maze = new Maze(8, 8);
		blocks = maze.getBlocks();
		polygons = new ArrayList<Poly>();
		for (Block block : blocks) {
			for (Poly poly : block.getPolys()) {
				polygons.add(poly);
			}
		}
		
		//polygons = maze.getPolys(Color.BLUE);
		tree = new BSPTree(polygons);
		
		//tree = maze.genBSP();
		player = new Player();
		this.screenSize = screenSize;
		hideCursor();
		centerMouse();
		enableEvents(MouseEvent.MOUSE_MOVED);
		
		time = System.currentTimeMillis();
		lastFPSCheck = time;
		lastRepaint = time;
	}
	
	void hideCursor() {
		 BufferedImage image = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
		 Cursor cursor = Toolkit.getDefaultToolkit()
				 .createCustomCursor(image, new Point(0,0), "InvisibleCursor");        
		 setCursor(cursor);
	}
	
	public void paintComponent(Graphics graphics) {
		long currentTime = System.currentTimeMillis();
		float deltaTime = 0.001f*(currentTime - lastRepaint);
		
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		//if game has not started yet print out title and instruction to begin
		if(!start) {
			graphics.setFont(new Font( "SansSerif", Font.BOLD, 30));
			graphics.setColor(Color.cyan);
			graphics.drawString("3D MAZE", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2)); 
			
			graphics.setColor(Color.white);
			graphics.setFont(new Font( "SansSerif", Font.BOLD, 20 ));
			graphics.drawString("Press Enter to Begin!", (int)screenSize.getWidth()/2, (int)(screenSize.getHeight()/2+20));	
		}
		if(start) {
		player.move(deltaTime, moveX, moveZ, yawAngle, pitchAngle);
		
		tree.render(graphics, player.getCamera(), screenSize, 90);
		}
		long frameTime = (System.currentTimeMillis() - lastRepaint); 

		frameCount++;			
		if(frameCount >= 15)
		{
			float fps = 1000.0f*frameCount/(System.currentTimeMillis() - lastFPSCheck);
			//System.out.println(fps);
			lastFPSCheck = System.currentTimeMillis();
			frameCount = 0;
		}
		
		if(frameTime < 1000.0/120.0)
		{
			try {
				Thread.sleep((long) (1000.0/120.0 - frameTime));
			} catch (InterruptedException e) {
				System.out.println(e);
			}	
		}
				
		lastRepaint = currentTime;
		
		//It looks like this doesn't call us directly so it's okay
		repaint();
	}
	
	private void centerMouse() {
		try {
			Robot robot = new Robot();
			robot.mouseMove((int)screenSize.getWidth()/2, (int)screenSize.getHeight()/2);
		} catch (AWTException e) {
			System.out.println(e);
		}
	}
	
	protected void processMouseMotionEvent(MouseEvent event) {
		if(start) {
			yawAngle += (event.getXOnScreen() - (int)screenSize.getWidth()/2)*sensitivity;
			pitchAngle -= (event.getYOnScreen() - (int)screenSize.getHeight()/2)*sensitivity;
		
			if (pitchAngle > 80*Mathf.PI/180) {
				pitchAngle = 80*Mathf.PI/180;
			} else if (pitchAngle < -80*Mathf.PI/180){
				pitchAngle = -80*Mathf.PI/180;
			}
		
			centerMouse();
		}
	}
	
	protected void processKeyEvent(KeyEvent event) {
		if (event.getID() == KeyEvent.KEY_PRESSED) {
			switch (event.getKeyCode()) {
			case KeyEvent.VK_W:
				moveZ= 1;
				break;
			case KeyEvent.VK_A:
				moveX = 1;
				break;
			case KeyEvent.VK_S:
				moveZ = -1;
				break;
			case KeyEvent.VK_D:
				moveX = -1;
				break;
			//adds the spacebar to move you up
			case KeyEvent.VK_SPACE:
				player.jump();
				break;
			//exits the game
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			case KeyEvent.VK_ENTER:
				start = true;
			default:
				break;
			}
		} else if (event.getID() == KeyEvent.KEY_RELEASED){
			switch (event.getKeyCode()) {
			case KeyEvent.VK_W:
				if (moveZ == 1) {
					moveZ = 0;
				}
				break;
			case KeyEvent.VK_A:
				if (moveX == 1) {
					moveX = 0;
				}
			case KeyEvent.VK_S:
				if (moveZ == -1) {
					moveZ = 0;
				}
				break;
			case KeyEvent.VK_D:
				if (moveX == -1) {
					moveX = 0;
				}
				break;
			default:
				break;
			}
		}
	}
}
