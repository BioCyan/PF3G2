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
import userMovement.UserMovement;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;

	private List<Poly> polygons;
	private Transform camera;
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
	private float moveY;
	private Vector cameraPos;
	private float moveSpeed;
	private BSPTree tree;
	UserMovement movement = new UserMovement();
	
	public Game(Dimension screenSize) {
		setFocusable(true);
		
		Poly[] tempPolys = (new Block(new Vector(0, 0, 0), new Vector(4, 4, 4))).getPolys(Color.BLUE); 
		polygons = new ArrayList<Poly>();
		for (int i = 0; i < tempPolys.length; i++) {
			polygons.add(tempPolys[i]);
		}
		tempPolys = (new Block(new Vector(2, 2, -6), new Vector(6, 6, -2))).getPolys(Color.GREEN); 
		for (int i = 0; i < tempPolys.length; i++) {
			polygons.add(tempPolys[i]);
		}
		
		moveSpeed = 4;
		Maze maze = new Maze(8, 8);
		polygons = maze.getPolys(Color.BLUE);
		tree = new BSPTree(polygons);
		
		//tree = maze.genBSP();
		
		cameraPos = new Vector(0, 1, 0);
		camera = new Transform();
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
		
		Rotation rot = new Rotation(yawAngle, pitchAngle);
		Vector wishMove = rot.localToWorld(new Vector(moveX, moveY, moveZ));
		movement.friction(deltaTime);
		movement.accelerate(deltaTime, wishMove);
		
		cameraPos = cameraPos.plus(movement.getMovement().times(deltaTime));
		camera = new Transform(cameraPos, rot);
		
		for (Poly poly : polygons) {
			//poly.render(graphics, camera, screenSize, 90);
		}
		
		tree.render(graphics, camera, screenSize, 90);
		
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
		yawAngle += (event.getXOnScreen() - (int)screenSize.getWidth()/2)*sensitivity;
		pitchAngle -= (event.getYOnScreen() - (int)screenSize.getHeight()/2)*sensitivity;
		
		if (pitchAngle > 80*Mathf.PI/180) {
			pitchAngle = 80*Mathf.PI/180;
		} else if (pitchAngle < -80*Mathf.PI/180){
			pitchAngle = -80*Mathf.PI/180;
		}
		
		centerMouse();
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
				moveY = 1;
				break;
			//exits the game
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
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
			case KeyEvent.VK_SPACE:
				if(moveY == 1) {
					moveY = 0;
				}
				break;
			default:
				break;
			}
		}
	}
}
