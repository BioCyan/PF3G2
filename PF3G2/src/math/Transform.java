package math;

public class Transform {
	private Vector pos;
	private Rotation rot;

	public Transform() {
		pos = new Vector();
		rot = new Rotation();
	}
	
	public Transform(Vector pos, Rotation rot) {
		this.pos = pos;
		this.rot = rot;
	}
	
	public Transform(Vector pos, float yaw, float pitch) {
		this.pos = pos;
		rot = new Rotation(yaw, pitch);
	}
	
	public Vector worldToLocal(Vector v) {
		return rot.worldToLocal(v.minus(pos));
	}
	
	public Vector localToWorld(Vector v) {
		return pos.plus(rot.localToWorld(v));
	}
}
