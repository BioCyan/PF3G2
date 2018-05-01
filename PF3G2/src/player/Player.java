package player;

import java.util.List;
import math.Rotation;
import math.Transform;
import math.Vector;
import model.Block;

public class Player {
	private final Vector STARTPOSITION = new Vector (0,1,0);
	private final float PLAYERHEIGHT = 1;
	private Vector position;
	private Block hitbox;
	private Transform camera;
	private UserMovement movement;
	public Player() {
		 movement = new UserMovement();
		 position = STARTPOSITION;
		 hitbox = new Block(new Vector(-0.25f, 0, -0.25f), new Vector(0.25f, 1, 0.25f));
		 camera= new Transform();
	}
	
	public UserMovement getMovement() {return movement;}
	public Vector getPosition() {return position;}
	public Transform getCamera() {return camera;}
	
	public void setMovement(UserMovement movement) {this.movement=movement;}
	public void setPosition(Vector position) {this.position=position;}
	public void setCamera(Transform camera) {this.camera=camera;}
	
	private void hitboxPosition(Vector position) {
		hitbox.setMins(position.plus(new Vector(-1, 0, -1)));
		hitbox.setMaxs(position.plus(new Vector(1, 1, 1)));
	}
	
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
		//hitboxPosition(position);
		camera = new Transform(position, rot);
	}
	
	public void traceMove(Vector target, List<Block> blocks) {
		Vector direction = target.minus(position).unit();
		//Unless we find a collision we can move 100% of the way
		float maxAdvance = 1;
		
		for (Block block : blocks) {
			float advance = 1;
			for (int i = 0; i < 6; i++) {
				int axis = i / 2 + 1;
				boolean side = i % 2 == 1;
				//Check whether our path crosses into the collision plane for this axis
				//(Doesn't necessarily mean we actually hit the block)
				float boundary;
				boolean intersecting;
				if (side) {
					boundary = block.getMaxs().get(axis) - hitbox.getMins().get(axis);
					intersecting = target.get(axis) < boundary;
				} else {
					boundary = block.getMins().get(axis) - hitbox.getMaxs().get(axis);
					intersecting = target.get(axis) > boundary;
				}
				
				//Move advance the farthest that definitely won't intersect the block
				if (intersecting && direction.get(axis) != 0) {
					float distance = (boundary - position.get(axis))/direction.get(axis);
					if (distance < advance) {
						advance = distance;
					}
				}
			}
			if (advance > maxAdvance) {
				maxAdvance = advance;
			}
			
			position = position.plus(direction.times(maxAdvance));
		}
	}

	public void jump() {
		if(movement.getMovement().y()==0)
			movement.jump();
		}
}
