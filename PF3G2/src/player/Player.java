package player;

import math.Vector;

public class Player {
	private final Vector STARTPOSITION = new Vector (0,1,0);
	private final float PLAYERHEIGHT = 1;
	private Vector position;
	private UserMovement movement;
	public Player() {
		 movement = new UserMovement();
		 position = STARTPOSITION;
	}
	
	public UserMovement getMovement() {return movement;}
	public Vector getPosition() {return position;}
	public void setMovement(UserMovement movement) {this.movement=movement;}
	public void setPosition(Vector position) {this.position=position;}
	
	

}
