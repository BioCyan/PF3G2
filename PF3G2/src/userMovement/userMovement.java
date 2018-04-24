package userMovement;

import math.Vector;

public class userMovement {
	private Vector xMovement, yMovement, zMovement;
	private final float ACCELERATIONOFGRAVITY=0.5f,FRICTION=0.3f,ACCELERATION=0.2f,MAXSPEED=1;
	
	public userMovement() {
		xMovement = new Vector(0,0,0);
		yMovement = new Vector(0,0,0);
		zMovement = new Vector(0,0,0);
	}
	
	public Vector getXMovement() {return xMovement;}
	public Vector getYMovement() {return yMovement;}
	public Vector getZMovement() {return zMovement;}
	
	public void setXMovement(Vector xMovement) {this.xMovement=xMovement;}
	public void setYMovement(Vector yMovement) {this.yMovement=yMovement;}
	public void setZMovement(Vector zMovement) {this.zMovement=zMovement;}
	
	public void jump() {yMovement.plus(new Vector (0,2,0));}
	public void fall() {yMovement.minus(new Vector (0,ACCELERATIONOFGRAVITY,0));}
	public void moveFoward() {
		if(zMovement.z()<=1)
			zMovement.plus(new Vector (0,0,ACCELERATION));
		else
			zMovement = new Vector(0,0,MAXSPEED);
	}
	public void moveBackward() {
		if(zMovement.z()>=-1)
			zMovement.minus(new Vector (0,0,ACCELERATION));
		else
			zMovement = new Vector(0,0,-MAXSPEED);
	}
	public void moveRight() {
		if(xMovement.x()<=1)
			xMovement.minus(new Vector (ACCELERATION,0,0));
		else
			xMovement = new Vector(MAXSPEED,0,0);
	}
	public void moveLeft() {
		if(xMovement.x()>=-1)
			xMovement.minus(new Vector (ACCELERATION,0,0));
		else
			xMovement = new Vector(-MAXSPEED,0,0);
	}
	
	
	public void stop() {	
		if(xMovement.x()>0) {
			xMovement.minus(new Vector (FRICTION,0,0));
			if(xMovement.x()<0)
				xMovement = new Vector(0,0,0);
			}
		if(xMovement.x()<0) {
			xMovement.plus(new Vector (FRICTION,0,0));
			if(xMovement.x()>0)
				xMovement = new Vector(0,0,0);
		}
		if(zMovement.z()>0) {
			zMovement.minus(new Vector (0,0,FRICTION));
			if(zMovement.z()<0)
				zMovement = new Vector(0,0,0);
			}
		if(zMovement.z()<0) {
			zMovement.plus(new Vector (0,0,FRICTION));
			if(zMovement.z()>0)
				zMovement = new Vector(0,0,0);
		}
	}
	
}
