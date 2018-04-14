package math;

import java.awt.*;
import java.util.List;

public class BSPTree {
	private int axis; //orientation of separating plane
	private float distance; //plane distance from origin along its normal
	private List<Poly> polygons;
	private BSPTree near;
	private BSPTree far;
	
	public BSPTree(int axis, float distance, List<Poly> polygons, BSPTree near, BSPTree far) {
		this.axis = axis;
		this.distance = distance;
		this.polygons = polygons;
		this.near = near;
		this.far = far;
	}
	
	public void render(Graphics graphics, Transform camera, Dimension screenSize, float fov) {
		Vector camPos = camera.localToWorld(new Vector());
		float dist = 0;
		switch (axis) {
		case 1:
			dist = camPos.x();
			break;
		case 2:
			dist = camPos.y();
			break;
		case 3:
			dist = camPos.z();
			break;
		}
		dist -= distance;
		
		BSPTree first, last;
		if (dist < 0) {
			first = far;
			last = near;
		} else {
			first = near;
			last = far;
		}
		
		if (first != null) {
			first.render(graphics, camera, screenSize, fov);
		}
		for (Poly poly : polygons) {
			poly.render(graphics, camera, screenSize, fov);
		}
		if (last != null) {
			last.render(graphics, camera, screenSize, fov);
		}
	}
}
