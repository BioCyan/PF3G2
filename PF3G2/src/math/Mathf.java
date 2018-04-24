package math;

//3D games conventionally use floats instead of doubles for performance.
//I don't know whether it's actually helping me in Java though.
public class Mathf {
	public static final float PI = (float)Math.PI;

	public static float sqrt(float x) {
		return (float)Math.sqrt(x);
	}
	
	public static float sin(float x) {
		return (float)Math.sin(x);
	}
	
	public static float cos(float x) {
		return (float)Math.cos(x);
	}

	public static float tan(float x) {
		return (float)Math.tan(x);
	}
}
