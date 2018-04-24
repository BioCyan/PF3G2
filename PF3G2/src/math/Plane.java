package math;

public class Plane {
	//private int axis; //x = 1, y = 2, z = 2
	private Vector normal;
	private float position;
	
	public Plane(Vector normal, float position) {
		this.normal = normal;
		this.position = position;
	}
	
	public Plane(int axis, float position) {
		//this.axis = axis;
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
	
	public float distance(Vector v) {
		return normal.dot(v) - position;
		//return v.get(axis) - position;
	}
}
