package userMovement;

import math.Vector;

public class userMovement {
	private Vector movement;
	private final float ACCELERATIONOFGRAVITY=0.5f,FRICTION=0.3f,ACCELERATION=0.2f,MAXSPEED=1;
	
	public userMovement() {
		movement = new Vector(0,0,0);
	}
	
	public Vector Movement() {return movement;}
	
	public void setMovement(Vector movement) {this.movement=movement;}
	
	public void jump() {movement.plus(new Vector (0,2,0));}
	public void fall() {movement.minus(new Vector (0,ACCELERATIONOFGRAVITY,0));}
	public void moveFoward() {
		if(movement.z()<=1)
			movement.plus(new Vector (0,0,ACCELERATION));
		else
			movement = new Vector(0,0,MAXSPEED);
	}
	public void moveBackward() {
		if(movement.z()>=-1)
			movement.minus(new Vector (0,0,ACCELERATION));
		else
			movement = new Vector(0,0,-MAXSPEED);
	}
	public void moveRight() {
		if(movement.x()<=1)
			movement.minus(new Vector (ACCELERATION,0,0));
		else
			movement = new Vector(MAXSPEED,0,0);
	}
	public void moveLeft() {
		if(movement.x()>=-1)
			movement.minus(new Vector (ACCELERATION,0,0));
		else
			movement = new Vector(-MAXSPEED,0,0);
	}
	
	public void stop() {	
		float originX = movement.x(), originZ = movement.z();
		if(movement.length()!=0) {
			movement.minus(movement.times(FRICTION/movement.length()));
		if(originX*movement.x()<0||originZ*movement.z()<0)
			movement = new Vector(0,0,0);
		}	
	}
	
}
