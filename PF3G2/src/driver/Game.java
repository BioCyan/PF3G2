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
	private final float MAXSPEED = 1;
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
		Maze maze = new Maze(32, 32);
		polygons = maze.getPolys(Color.BLUE);
		//tree = new BSPTree(polygons);
		
		tree = maze.genBSP();
		
		cameraPos = new Vector(-16, 0, -16);
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
		Vector movement = rot.localToWorld((new Vector(moveX, moveY, moveZ)).times(moveSpeed*deltaTime));
		cameraPos = cameraPos.plus(movement);
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
				while(movement.getMovement().z()<MAXSPEED) {
					movement.moveFoward();
					moveZ = movement.getMovement().z();
				}
				break;
			case KeyEvent.VK_A:
				while(movement.getMovement().x()<MAXSPEED) {
					movement.moveLeft();
					moveX = movement.getMovement().x();
				}
				break;
			case KeyEvent.VK_S:
				while(movement.getMovement().z()>-MAXSPEED) {
					movement.moveBackward();
					moveZ = movement.getMovement().z();
				}
				break;
			case KeyEvent.VK_D:
				while(movement.getMovement().x()>-MAXSPEED) {
					movement.moveRight();
					moveX = movement.getMovement().x();
				}
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
			case KeyEvent.VK_S:
				if (moveZ<0||moveZ>0) {
					while(moveZ<0||moveZ>0) {
						movement.stopZ();
						moveZ = movement.getMovement().z();
					}
				}
				break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_D:
				if (moveX<0||moveX>0){
					while(moveX<0||moveX>0) {
						movement.stopX();
						moveX = movement.getMovement().x();
					}
				}
				break;
			case KeyEvent.VK_SPACE:
				if(moveY == 1)
					moveY = 0;
				break;
			default:
				break;
			}
		}
	}
}
