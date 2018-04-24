package math;

public class Vector {
	private float x;
	private float y;
	private float z;
	
	public Vector() {x = y = z = 0;}
	
	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float x() {return x;}
	public float y() {return y;}
	public float z() {return z;}
	
	public float get(int axis) {
		switch(axis) {
		case 1:
			return x;
		case 2:
			return y;
		case 3:
			return z;
		default:
			throw new RuntimeException("Tried to access nonexistant axis " + axis);
		}
	}
	
	public float length() {
		return Mathf.sqrt(x*x + y*y + z*z);
	}
	
	public Vector unit() {
		return times(1 / length());
	}
	
	public float dot(Vector v) {
		return x*v.x() + y*v.y() + z*v.z();
	}
	
	public Vector cross(Vector v) {
		return new Vector(y*v.z() - z*v.y(),
				z*v.x() - x*v.z(),
				x*v.y() - y*v.x());
	}
	
	//Two decades and Java still has no operator overloading
	public Vector plus(Vector v) {
		return new Vector(x + v.x(), y + v.y(), z + v.z());
	}
	
	public Vector minus() {
		return new Vector(-x, -y, -z);
	}
	
	public Vector minus(Vector v) {
		return plus(v.minus());
	}
	
	public Vector times(float scale) {
		return new Vector(x*scale, y*scale, z*scale);
	}
	
	public String toString() {
		return "x: " + x + " y: " + y + " z: " + z;
	}
}
