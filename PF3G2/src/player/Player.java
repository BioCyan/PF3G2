package player;

import math.Rotation;
import math.Transform;
import math.Vector;

public class Player {
	private final Vector STARTPOSITION = new Vector (0,1,0);
	private final float PLAYERHEIGHT = 1;
	private Vector position;
	private Transform camera;
	private UserMovement movement;
	public Player() {
		 movement = new UserMovement();
		 position = STARTPOSITION;
		 camera= new Transform();
	}
	
	public UserMovement getMovement() {return movement;}
	public Vector getPosition() {return position;}
	public Transform getCamera() {return camera;}
	
	public void setMovement(UserMovement movement) {this.movement=movement;}
	public void setPosition(Vector position) {this.position=position;}
	public void setCamera(Transform camera) {this.camera=camera;}
	
	
	public void move(float deltaTime, float moveX, float moveZ, float yawAngle, float pitchAngle) {
		Rotation rot = new Rotation(yawAngle, pitchAngle);
		Vector direction = rot.localToWorld(new Vector(moveX, 0, moveZ));
		if(movement.getMovement().y()==0) {
			movement.friction(deltaTime);
			movement.accelerate(deltaTime, direction);
		}
		movement.fall(deltaTime);
		position = position.plus(movement.getMovement().times(deltaTime));
		//preventing the player to go below ground level and rest the y value to 0 (JT)
		if(position.y()<0) {
			position = position.minus(new Vector(0,position.y(),0));
			movement.setMovement(new Vector(movement.getMovement().x(),0,movement.getMovement().z()));
		}
		camera = new Transform(position, rot);
	}

	public void jump() {
		if(movement.getMovement().y()==0)
			movement.jump();
		}
}
