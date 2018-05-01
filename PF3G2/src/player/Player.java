package player;

import java.util.List;
import math.Rotation;
import math.Transform;
import math.Vector;
import model.Block;

public class Player {
	private final Vector STARTPOSITION = new Vector (0,3,0);
	private final float PLAYERHEIGHT = 1;
	private Vector position;
	private Block hitbox;
	private Transform camera;
	private UserMovement movement;
	public Player(Vector startPosition) {
		 movement = new UserMovement();
		 position = startPosition;
		 hitbox = new Block(new Vector(-0.25f, -1, -0.25f), new Vector(0.25f, 1, 0.25f));
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
	
	public void move(float deltaTime, float moveX, float moveZ, float yawAngle, float pitchAngle, List<Block> blocks) {
		Rotation rot = new Rotation(yawAngle, pitchAngle);
		Vector direction = rot.localToWorld(new Vector(moveX, 0, moveZ));
		if(movement.getMovement().y()==0) {
			movement.friction(deltaTime);
			movement.accelerate(deltaTime, direction);
		}
		movement.fall(deltaTime);
		Vector target = position.plus(movement.getMovement().times(deltaTime));
		traceMove(target, blocks);
		//preventing the player to go below ground level and rest the y value to 0 (JT)
		if(position.y()<0) {
			position = position.minus(new Vector(0,position.y(),0));
			movement.setMovement(new Vector(movement.getMovement().x(),0,movement.getMovement().z()));
		}
		//hitboxPosition(position);
		camera = new Transform(position, rot);
	}
	
	public void traceMove(Vector target, List<Block> blocks) {
		float epsilon = 1.0f/16;
		
		Vector displacement = target.minus(position);
		//Unless we find a collision we can move 100% of the way
		float maxAdvance = 1;
		
		for (Block block : blocks) {
			float enter = 0;
			float exit = 1;
			for (int i = 0; i < 6; i++) {
				
				int axis = i / 2 + 1;
				boolean side = i % 2 == 1;
				//Check whether our path crosses into the collision plane for this axis
				//(Doesn't necessarily mean we actually hit the block)
				float boundary;
				boolean targetIn, positionIn;
				if (side) {
					boundary = block.getMaxs().get(axis) - hitbox.getMins().get(axis);
					targetIn = target.get(axis) < boundary - epsilon;
					positionIn = position.get(axis) < boundary - epsilon;
				} else {
					boundary = block.getMins().get(axis) - hitbox.getMaxs().get(axis);
					targetIn = target.get(axis) > boundary + epsilon;
					positionIn = position.get(axis) > boundary + epsilon;
				}
				
				//Move advance the farthest that definitely won't intersect the block
				if (!targetIn && !positionIn) {
					enter = 1;
					break;
				} else if (targetIn && !positionIn && displacement.get(axis) != 0) {
					float distance = (boundary - position.get(axis))/displacement.get(axis);
					if (distance > enter) {
						enter = distance;
					}
				} else if (!targetIn && positionIn) {
					float distance = 1 - (target.get(axis) - boundary)/displacement.get(axis);
					if (distance < exit) {
						exit = distance;
					}
				} else if (targetIn && positionIn) {
				}
			}
			
			if (enter < 0) {
				enter = exit;
			}
			
			if (enter < maxAdvance) {
				maxAdvance = enter;
			}	
		}
		
		position = position.plus(displacement.times(maxAdvance - epsilon));
	}

	public void jump() {
		if(movement.getMovement().y()==0)
			movement.jump();
		}
}
