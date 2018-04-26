package userMovement;

import math.Vector;

public class UserMovement {
	private Vector movement;
	private final float ACCELERATIONOFGRAVITY=0.5f,FRICTION=5,ACCELERATION=7, MAXSPEED=10;
	public UserMovement() {
		movement = new Vector(0,0,0);
	}
	
	public Vector getMovement() {return movement;}
	
	public void setMovement(Vector movement) {this.movement=movement;}
	
	public void jump(float deltaTime) {movement.plus(new Vector (0,2,0));}
	public void fall(float deltaTime) {movement.minus(new Vector (0,ACCELERATIONOFGRAVITY,0));}
	public void accelerate(float deltaTime, Vector direction) {
		movement = movement.plus(direction.unit().times(ACCELERATION*deltaTime));
		if(movement.length()>MAXSPEED){
			movement=movement.unit().times(MAXSPEED);
		}
	}
	
	public void friction(float deltaTime) {
		float speed = movement.length();
		speed -= FRICTION*deltaTime;
		if(speed<0)
			speed = 0;
		movement = movement.unit().times(speed);
	}
}
