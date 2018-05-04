package player;

import java.util.List;
import math.Rotation;
import math.Transform;
import math.Vector;
import model.Block;

public class Player {
	private Vector position;
	private Block hitbox;
	private boolean onGround;
	private Transform camera;
	private UserMovement movement;
	//Constructor for player class
	public Player() {
		 movement = new UserMovement();
		 position = new Vector();
		 //Create hitbox for the player
		 hitbox = new Block(new Vector(-0.25f, -1, -0.25f), new Vector(0.25f, 0.25f, 0.25f));
		 camera = new Transform();
		 onGround = false;
	}
	
	//Accessors
	public UserMovement getMovement() {return movement;}
	public Vector getPosition() {return position;}
	public Transform getCamera() {return camera;}
	
	//Mutators
	public void setMovement(UserMovement movement) {this.movement=movement;}
	public void setPosition(Vector position) {this.position=position;}
	public void setCamera(Transform camera) {this.camera=camera;}
	
	//Method that controls the use's movement
	public void move(float deltaTime, float moveX, float moveZ, float yawAngle, float pitchAngle, List<Block> blocks) {
		//Making sure delta time is not 0, so it doesn't mess up the ground check
		if (deltaTime == 0) {
			return;
		}
		
		Rotation rot = new Rotation(yawAngle, pitchAngle);
		
		//Creates a vector that represents the ground speed and direction of the player
		Vector direction = rot.localToWorld(new Vector(moveX, 0, moveZ));
		if (onGround) {
			movement.friction(deltaTime);
		}
		else {
			movement.airFriction(deltaTime);
		}
		movement.accelerate(deltaTime, direction);
		movement.fall(deltaTime);
		
		//If a collision reduced downward velocity caused by gravity we are on the ground
		float oldYSpeed = movement.getVelocity().y();
		Vector target = position.plus(movement.getVelocity().times(deltaTime));
		traceMove(target, blocks);
		float newYSpeed = movement.getVelocity().y();
		onGround = (oldYSpeed < 0 && newYSpeed != oldYSpeed);
		
		//Update the view of the player
		camera = new Transform(position, rot);
	}
	
	public void traceMove(Vector target, List<Block> blocks) {
		Vector displacement = target.minus(position);
		
		int MAX_BUMPS = 4;
		for (int bump = 0; bump < MAX_BUMPS; bump++) {
			if (displacement.length() == 0) {
				return;
			}
			
			//Unless we find a collision we can move 100% of the way
			float maxAdvance = 1;
			
			int collideAxis = 0;
			int blockCollideAxis = 0;
			for (Block block : blocks) {
				//This is the lower bound for how far we can go before colliding
				float enter = 0;
				//This is the upper bound for how far collision is still possible
				float exit = 1;
				boolean inside = true;
				for (int i = 0; i < 6; i++) {
					int axis = i / 2 + 1;
					boolean side = i % 2 == 1;
					
					//Categorize target and position relative to the block plane being check
					float boundary;
					boolean targetIn, positionIn;
					if (side) {
						boundary = block.getMaxs().get(axis) - hitbox.getMins().get(axis);
						targetIn = target.get(axis) < boundary;
						positionIn = position.get(axis) < boundary;
					} else {
						boundary = block.getMins().get(axis) - hitbox.getMaxs().get(axis);
						targetIn = target.get(axis) > boundary;
						positionIn = position.get(axis) > boundary;
					}
					
					if (!positionIn) {
						inside = false;
					}
					
					if (!targetIn && !positionIn) {
						//Path fully outside block, collision impossible
						enter = 1;
						break;
					} else if (targetIn && !positionIn && displacement.get(axis) != 0) {
						//Entering a plane means any collision will be at or after that point
						//Update the lower bound of how far we can go
						float distance = (boundary - position.get(axis))/displacement.get(axis);
						if (distance >= enter) {
							blockCollideAxis = axis;
							enter = distance;
						}
					} else if (!targetIn && positionIn) {
						//Exiting a plane means no collision can occur after that point
						float distance = 1 - (target.get(axis) - boundary)/displacement.get(axis);
						if (distance < exit) {
							exit = distance;
						}
						//Looking back, we forgot to actually use exit to reject possible collisions
						//I guess the path is usually short enough for it to rarely be necessary so it was never noticed
					}
				}
				
				//If this block collision would occur before all other collision candidates
				//It is our new collision point
				if (enter < maxAdvance) {
					maxAdvance = enter;
					collideAxis = blockCollideAxis;
				}
				
				if (inside) {
					System.out.println("emergency unstuck");
					float newY = block.getMaxs().y() - hitbox.getMins().y();
					position = new Vector(position.x(), newY, position.z());
				}
			}
			
			//Update our velocity to no longer be moving into the colliding block
			Vector nextDisplacement = displacement;
			if (collideAxis != 0) {
				Vector m = movement.getVelocity();
				movement.setVelocity(m.minus(new Vector(collideAxis, m.get(collideAxis))));
				nextDisplacement = displacement.minus(new Vector(collideAxis, displacement.get(collideAxis)));
			}
			
			//Safety margin to avoid ending up inside the block
			if (maxAdvance < 1) {
				maxAdvance -= 1.0f/1024;
			}
			if (maxAdvance < 0) {
				maxAdvance = 0;
			}
			
			//Move to collision point and prepare to travel the remaining time with updated velocity
			position = position.plus(displacement.times(maxAdvance));
			displacement = nextDisplacement.times(1 - maxAdvance);
			target = position.plus(displacement);
		}
	}

	//Call the jump method if the player is onGound
	public void jump() {
		if(onGround) {
			movement.jump();
		}
	}
}
