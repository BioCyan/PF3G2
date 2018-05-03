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
	private boolean onGround;
	private Transform camera;
	private UserMovement movement;
	public Player() {
		 movement = new UserMovement();
		 position = new Vector();
		 hitbox = new Block(new Vector(-0.25f, -1, -0.25f), new Vector(0.25f, 0.25f, 0.25f));
		 camera = new Transform();
		 onGround = false;
	}
	
	public UserMovement getMovement() {return movement;}
	public Vector getPosition() {return position;}
	public Transform getCamera() {return camera;}
	
	public void setMovement(UserMovement movement) {this.movement=movement;}
	public void setPosition(Vector position) {this.position=position;}
	public void setCamera(Transform camera) {this.camera=camera;}
	
	public void move(float deltaTime, float moveX, float moveZ, float yawAngle, float pitchAngle, List<Block> blocks) {
		//Time is measured in milliseconds so this does happen
		if (deltaTime == 0) {
			return;
		}
		
		Rotation rot = new Rotation(yawAngle, pitchAngle);
		Vector direction = rot.localToWorld(new Vector(moveX, 0, moveZ));
		if (onGround) {
			movement.friction(deltaTime);
		}
		else {
			movement.airFriction(deltaTime, movement.getAirFriction());
		}
		movement.accelerate(deltaTime, direction);
		movement.fall(deltaTime);
		
		float oldYSpeed = movement.getVelocity().y();
		Vector target = position.plus(movement.getVelocity().times(deltaTime));
		traceMove(target, blocks);
		float newYSpeed = movement.getVelocity().y();
		onGround = (oldYSpeed < 0 && newYSpeed != oldYSpeed);
		
		
		//preventing the player to go below ground level and rest the y value to 0 (JT)
		/*
		if(position.y()<0) {
			position = position.minus(new Vector(0,position.y(),0));
			movement.setMovement(new Vector(movement.getMovement().x(),0,movement.getMovement().z()));
		}
		*/
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
				float enter = 0;
				float exit = 1;
				boolean inside = true;
				for (int i = 0; i < 6; i++) {
					int axis = i / 2 + 1;
					boolean side = i % 2 == 1;
					//Check whether our path crosses into the collision plane for this axis
					//(Doesn't necessarily mean we actually hit the block)
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
					
					//Move advance the farthest that definitely won't intersect the block
					if (!targetIn && !positionIn) {
						enter = 1;
						break;
					} else if (targetIn && !positionIn && displacement.get(axis) != 0) {
						float distance = (boundary - position.get(axis))/displacement.get(axis);
						if (distance >= enter) {
							blockCollideAxis = axis;
							enter = distance;
						}
					} else if (!targetIn && positionIn) {
						float distance = 1 - (target.get(axis) - boundary)/displacement.get(axis);
						if (distance < exit) {
							exit = distance;
						}
					}
				}
				
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
			
			Vector nextDisplacement = displacement;
			if (collideAxis != 0) {
				Vector m = movement.getVelocity();
				movement.setVelocity(m.minus(new Vector(collideAxis, m.get(collideAxis))));
				nextDisplacement = displacement.minus(new Vector(collideAxis, displacement.get(collideAxis)));
			}
			
			//Failsafe to avoid ending up inside the block
			if (maxAdvance < 1) {
				maxAdvance -= 1.0f/1024;
			}
			if (maxAdvance < 0) {
				maxAdvance = 0;
			}
			position = position.plus(displacement.times(maxAdvance));
			displacement = nextDisplacement.times(1 - maxAdvance);
			target = position.plus(displacement);
		}
	}

	public void jump() {
		if(onGround) {
			movement.jump();
		}
	}
}
