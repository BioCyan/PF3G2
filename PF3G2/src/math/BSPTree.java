package math;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class BSPTree {
	private static Random rand = new Random();
	
	private Plane plane;
	//private int axis; //orientation of separating plane
	//private float distance; //plane distance from origin along its normal
	private List<Poly> polygons;
	private BSPTree near;
	private BSPTree far;
	
	public BSPTree(int axis, float distance, List<Poly> polygons, BSPTree near, BSPTree far) {
		plane = new Plane(axis, distance);
		this.polygons = polygons;
		this.near = near;
		this.far = far;
	}
	
	public BSPTree(Plane plane, List<Poly> polygons, BSPTree near, BSPTree far) {
		this.plane = plane;
		this.polygons = polygons;
		this.near = near;
		this.far = far;
	}
	
	public BSPTree(List<Poly> polys) {
		int splitIndex = rand.nextInt(polys.size());
		Plane splitter = polys.get(splitIndex).getPlane();
		List<Poly> far = new ArrayList<Poly>();
		List<Poly> near = new ArrayList<Poly>();
		List<Poly> middle = new ArrayList<Poly>();
		for (Poly poly : polys) {
			if (poly == polys.get(splitIndex) || poly.coplanar(splitter)) {
				middle.add(poly);
			} else {
				Poly farAdd = poly.clip(splitter, true);
				Poly nearAdd = poly.clip(splitter, false);
				
				if (farAdd != null) {
					far.add(farAdd);
				}
				if (nearAdd != null) {
					near.add(nearAdd);
				}
			}
		}
		
		this.plane = splitter;
		this.polygons = middle;
		
		if (near.size() == 0) {
			this.near = null;
		} else {
			this.near = new BSPTree(near);
		}
		
		if (far.size() == 0) {
			this.far = null;
		} else {
			this.far = new BSPTree(far);
		}
	}
	
	public void render(Graphics graphics, Transform camera, Dimension screenSize, float fov) {
		Vector camPos = camera.localToWorld(new Vector());
		float dist = plane.distance(camPos);
		
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
