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
	
	//Make a vector with only one nonzero axis
	public Vector(int axis, float value) {
		x = y = z = 0;
		switch(axis) {
		case 1:
			x = value;
			break;
		case 2:
			y = value;
			break;
		case 3:
			z = value;
			break;
		default:
			throw new RuntimeException("Tried to access nonexistant axis " + axis);
		}
	}
	
	public float x() {return x;}
	public float y() {return y;}
	public float z() {return z;}
	
	//Useful for iterating over the different axes
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
	
	//Returns a vector that has the same direction
	//but is one unit long
	public Vector unit() {
		if (length() == 0) {
			return new Vector();
		} else {
			return times(1 / length());
		}
	}
	
	//This is the vector dot product
	//Technically it returns the product of the lengths of its inputs
	//times the cosine of the angle between them
	//Useful for finding the distance in the direction of a unit vector
	public float dot(Vector v) {
		return x*v.x() + y*v.y() + z*v.z();
	}
	
	//This is the vector cross product
	//It returns a vector perpendicular to its inputs
	//with a length equals to the product of their lengths
	//times the sine of the angle between them
	//We only really use the perpendicular part
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
	
	//Was used a lot in testing
	public String toString() {
		return "x: " + x + " y: " + y + " z: " + z;
	}
}
