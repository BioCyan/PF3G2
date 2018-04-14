package math;

public class Rotation {
	private Vector x;
	private Vector y;
	private Vector z;
	
	public Rotation() {
		x = new Vector(1, 0, 0);
		y = new Vector(0, 1, 0);
		z = new Vector(0, 0, 1);
	}
	
	public Rotation(Vector x, Vector  y, Vector z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Rotation(float yaw, float pitch) {
		x = new Vector(Mathf.cos(yaw), 0, Mathf.sin(yaw));
		y = new Vector(Mathf.sin(pitch)*Mathf.sin(yaw),
				Mathf.cos(pitch),
				-Mathf.sin(pitch)*Mathf.cos(yaw));
		z = x.cross(y);
	}
	
	public Vector localToWorld(Vector v) {
		return x.times(v.x()).plus(y.times(v.y())).plus(z.times(v.z()));
	}
	
	public Vector worldToLocal(Vector v) {
		return new Vector(x.dot(v), y.dot(v), z.dot(v));
	}
	
	public String toString() {
		return "X: " + x + "\nY: " + y + "\nZ: " + z + "\n";
	}
}
