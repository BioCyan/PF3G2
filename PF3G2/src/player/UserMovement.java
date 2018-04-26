package player;

import math.Vector;

public class UserMovement {
	private Vector movement;
	private final float ACCELERATIONOFGRAVITY=5,FRICTION=5,ACCELERATION=7, MAXSPEED=10;
	public UserMovement() {
		movement = new Vector(0,0,0);
	}
	
	public Vector getMovement() {return movement;}
	
	public void setMovement(Vector movement) {this.movement=movement;}
	
	public void jump() {movement = movement.plus(new Vector (0,10,0));}
	public void fall(float deltaTime) {
		movement = movement.minus(new Vector (0,ACCELERATIONOFGRAVITY,0).times(deltaTime));
		}
	
	//This method increases the speed of the player
	public void accelerate(float deltaTime, Vector direction) {
		movement = movement.plus(direction.unit().times(ACCELERATION*deltaTime));
		if(movement.length()>MAXSPEED){
			movement=movement.unit().times(MAXSPEED);
		}
	}
	
	//This method decrease the speed of the player until it's 0
	public void friction(float deltaTime) {
		Vector yMovement = new Vector(0,movement.y(),0);
		Vector groundMovement =new Vector(movement.x(),0,movement.z());
		float speed = groundMovement.length();
		speed -= FRICTION*deltaTime;
		if(speed<0)
			speed = 0;
		movement = groundMovement.unit().times(speed).plus(yMovement);
	}
}
