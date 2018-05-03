package player;

import driver.GameInterface;
import math.Vector;

public class UserMovement implements GameInterface {
	private Vector velocity;
	//Constructor
	public UserMovement() {
		velocity = new Vector(0,0,0);
	}
	
	//Accessors
	public Vector getVelocity() {return velocity;}
	public float getAirFriction() {return INAIRFRICTION;}
	
	//Mutators
	public void setVelocity(Vector movement) {this.velocity=movement;}
	
	//Gives the player a speed on the y axis
	public void jump() {velocity = velocity.plus(new Vector (0,JUMPSPEED,0));}
	
	//Gives the player a 
	public void fall(float deltaTime) {
		velocity = velocity.minus(new Vector (0,ACCELERATIONOFGRAVITY*deltaTime,0));
	}
	
	//This method increases the speed of the player
	public void accelerate(float deltaTime, Vector direction) {
		Vector yMovement = new Vector(0,velocity.y(),0);
		Vector groundMovement =new Vector(velocity.x(),0,velocity.z());
		Vector groundDirection = new Vector(direction.x(),0,direction.z());
		groundMovement = groundMovement.plus(groundDirection.unit().times(ACCELERATION*deltaTime));
		if(groundMovement.length()>MAXSPEED){
			groundMovement = groundMovement.unit().times(MAXSPEED);
		}
		velocity = groundMovement.plus(yMovement);
	}
	
	//This method decrease the speed of the player until it's 0
	public void friction(float deltaTime) {
		Vector yMovement = new Vector(0,velocity.y(),0);
		Vector groundMovement =new Vector(velocity.x(),0,velocity.z());
		float speed = groundMovement.length();
		speed -= FRICTION*deltaTime;
		if(speed<0)
			speed = 0;
		velocity = groundMovement.unit().times(speed).plus(yMovement);
	}
	
	//Slows the player down while in air
	public void airFriction(float deltaTime) {
		Vector yMovement = new Vector(0,velocity.y(),0);
		Vector groundMovement =new Vector(velocity.x(),0,velocity.z());
		float speed = groundMovement.length();
		speed -= INAIRFRICTION*deltaTime;
		if(speed<0)
			speed = 0;
		velocity = groundMovement.unit().times(speed).plus(yMovement);
	}
}
