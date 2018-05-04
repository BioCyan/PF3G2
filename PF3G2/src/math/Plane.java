package math;

//Represents an infinite plane
public class Plane {
	//Direction perpendicular to the plane
	private Vector normal;
	//Signed distance from the origin along that direction
	private float position;
	
	public Plane(Vector normal, float position) {
		this.normal = normal;
		this.position = position;
	}
	
	public Plane(int axis, float position) {
		this.position = position;
		switch (axis) {
		case 1:
			normal = new Vector(1, 0, 0);
			break;
		case 2:
			normal = new Vector(0, 1, 0);
			break;
		case 3:
			normal = new Vector(0, 0, 1);
			break;
		}
	}
	
	//Returns the signed distance from the plane
	//Towards the normal is positive, away is negative
	public float distance(Vector v) {
		return normal.dot(v) - position;
	}
}
