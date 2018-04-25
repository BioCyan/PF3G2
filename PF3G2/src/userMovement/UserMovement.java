package userMovement;

import math.Vector;

public class UserMovement {
	private Vector movement;
	private final float ACCELERATIONOFGRAVITY=0.5f,FRICTION=0.05f,ACCELERATION=0.02f;
	
	public UserMovement() {
		movement = new Vector(0,0,0);
	}
	
	public Vector getMovement() {return movement;}
	
	public void setMovement(Vector movement) {this.movement=movement;}
	
	public void jump() {movement.plus(new Vector (0,2,0));}
	public void fall() {movement.minus(new Vector (0,ACCELERATIONOFGRAVITY,0));}
	public void moveFoward() {
			movement = movement.plus(new Vector (0,0,ACCELERATION));
	}
	public void moveBackward() {
			movement = movement.minus(new Vector (0,0,ACCELERATION));
	}
	public void moveRight() {
			movement = movement.minus(new Vector (ACCELERATION,0,0));
	}
	public void moveLeft() {
			movement = movement.plus(new Vector (ACCELERATION,0,0));
	}
	
	public void stop() {
		float originX = movement.x();
		if(movement.x()!=0) {
			if(movement.x()>0)
				movement = movement.minus(new Vector(FRICTION,0,0));
			else if(movement.x()<0)
				movement = movement.plus(new Vector(FRICTION,0,0));
			if(originX*movement.x()<0)
			movement = new Vector(0,0,0);
		}	
		float originZ = movement.z();
		if(movement.z()!=0) {
			if(movement.z()>0)
				movement = movement.minus(new Vector(0,0,FRICTION));
			else if(movement.z()<0)
				movement = movement.plus(new Vector(0,0,FRICTION));
			if(originZ*movement.z()<0) 
				movement = new Vector(0,0,0);
		}
	}
}
