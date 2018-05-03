package driver;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import math.*;
import model.*;
import player.Player;
import levels.*;

public class Game extends JPanel implements GameInterface{
	private static final long serialVersionUID = 1L;

	private List<Poly> polygons;
	private List<Block> blocks;
	private Dimension screenSize;
	private long time, playerTime, startTime;
	private long lastFPSCheck;
	private long lastRepaint;
	private float yawAngle = 0;
	private float pitchAngle = 0;
	private int frameCount = 0;
	private float moveX;
	private float moveZ;
	private float resetValue;
	float fps;
	private boolean run, gameOver, paused, restart, instructions = false;
	private BSPTree tree;
	private Player player;
	private LevelInterface level;
	String playerCurrentTime;
	
	public Game(Dimension screenSize) {
		setFocusable(true);
		
		level = new Level1();
		
		player = new Player();
		
		loadLevel(level);
		
		tree = new BSPTree(polygons);
		
		this.screenSize = screenSize;
		hideCursor();
		centerMouse();
		enableEvents(MouseEvent.MOUSE_MOVED);
		
		time = System.currentTimeMillis();
		lastFPSCheck = time;
		lastRepaint = time;
	}
	
	private void loadLevel(LevelInterface level) {
		blocks = level.getBlocks();
		polygons = new ArrayList<Poly>();
		for (Block block : blocks) {
			for (Poly poly : block.getPolys()) {
				polygons.add(poly);
			}
		}
		
		Block endBlock = level.getEndBlock();
		Vector meshPos = endBlock.getMins().plus(endBlock.getMaxs()).times(0.5f);
		meshPos = meshPos.plus(new Vector(0, 0.5f, 0));
		List<Poly> mesh = FileMesh.loadMesh("monkey.obj", meshPos, 0.3f);
		for (Poly poly : mesh) {
			polygons.add(poly);
		}
		
		resetValue = level.resetYValue();
		tree = new BSPTree(polygons);
		player.setPosition(level.getStartPosition());
		player.getMovement().setVelocity(new Vector());
		yawAngle = 0;
		pitchAngle = 0;
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
		if(!run&&!gameOver&&!paused) {
			driver.Menu.startScreen(graphics);
			if(instructions) {
				driver.Menu.instructionScreen(graphics);
			}
			else {
				driver.Menu.startScreen(graphics);
			}
		}
		//if game not running and game is over, call endScreen method
		if(!run&&gameOver&&!restart) {
			driver.Menu.endScreen(graphics,playerCurrentTime);
		}
		if(paused&&!run&&!gameOver) {
			driver.Menu.pauseScreen(graphics);
		}
		if(run) {
			player.move(deltaTime, moveX, moveZ, yawAngle, pitchAngle, blocks);
			
			if(player.getPosition().y() < resetValue) {
				player.setPosition(level.getStartPosition());
				player.getMovement().setVelocity(new Vector());
			}
			
			if(level.getEndBlock() != null &&
					player.getPosition().x() > level.getEndBlock().getMins().x() && 
					player.getPosition().x() < level.getEndBlock().getMaxs().x() &&
					player.getPosition().z() > level.getEndBlock().getMins().z() &&
					player.getPosition().z() < level.getEndBlock().getMaxs().z() &&
					player.getPosition().y() > level.getEndBlock().getMaxs().y()) 
			{
				LevelInterface nextLevel = level.nextLevel();
				if(nextLevel != null) {
					level = nextLevel;
					loadLevel(level);
				}
				else {
					run = false;
					gameOver = true;
					if(restart) {
						restart = false;
						level = new Level1();
						loadLevel(level);
					}
				}
			}
			tree.render(graphics, player.getCamera(), screenSize, 90);
			displayTimer(graphics);
			displayfps(graphics, fps);
		}
		
		long frameTime = (System.currentTimeMillis() - lastRepaint); 
		frameCount++;			
		if(frameCount >= 15)
		{
			fps = 1000.0f*frameCount/(System.currentTimeMillis() - lastFPSCheck);
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
		if(run) {
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
	
	private void restartGame() {
		gameOver = false;
		run = false;
		restart = true;
		level = new Level1();
		loadLevel(level);
	}
	
	private void displayTimer(Graphics graphics) {
		playerTime = System.currentTimeMillis();
		float timer = playerTime-startTime;
		timer = ((int) timer*1000)/1000.0f;
		int milSec = (int) (timer%1000);
		int sec = (int) ((timer/1000)%60000);
		int min = (int) (sec/60);
		playerCurrentTime = min+":"+sec+":"+milSec;
		graphics.setColor(Color.white);
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 15 ));
		graphics.drawString(playerCurrentTime, 10, 35);
	}
	
	private void displayfps(Graphics graphics, float fps) {
		String currentFps = fps+"";
		graphics.setColor(Color.white);
		graphics.setFont(new Font( "SansSerif", Font.BOLD, 15));
		graphics.drawString(currentFps, 10, 15);
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
			case KeyEvent.VK_P:
				if(run&&!paused) {
					run = false;
					paused = true;
				}
				else {
					run = true;
					paused = false;
				}
				break;
			case KeyEvent.VK_I:
				if(!run&&!instructions) {
					instructions = true;
				}
				else {
					instructions = false;
				}
				break;
			case KeyEvent.VK_R:
				if(gameOver) {
					restartGame();
				}
				break;
			case KeyEvent.VK_ENTER:
				if (!run && !gameOver) {
					startTime = System.currentTimeMillis();;
					run = true;
				}
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
			default:
				break;
			}
		}
	}
}
