package math;

public class Plane {
	private int axis; //x = 1, y = 2, z = 2
	private float position;
	
	public Plane(int axis, float position) {
		this.axis = axis;
		this.position = position;
	}
	
	public float distance(Vector v) {
		return v.get(axis) - position;
	}
}
