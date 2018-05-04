package math;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class BSPTree {
	private static Random rand;
	
	private Plane plane;
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
		rand = new Random(1);
		
		//Choose a random poly to use as the plane to split
		int splitIndex = rand.nextInt(polys.size());
		Plane splitter = polys.get(splitIndex).getPlane();
		List<Poly> far = new ArrayList<Poly>();
		List<Poly> near = new ArrayList<Poly>();
		List<Poly> middle = new ArrayList<Poly>();
		for (Poly poly : polys) {
			if (poly == polys.get(splitIndex) || poly.coplanar(splitter)) {
				//Anything exactly on the plane is rendered in between everything else
				//Ordering of these does not matter
				middle.add(poly);
			} else {
				//If a poly is partly on both sides of the plane
				//Split it into two halves for each side
				Poly farAdd = poly.clip(splitter, true);
				Poly nearAdd = poly.clip(splitter, false);
				
				//Add to appropriate lists to be made into subtrees later
				//But only if the split actually left something on that side
				if (farAdd != null) {
					far.add(farAdd);
				}
				if (nearAdd != null) {
					near.add(nearAdd);
				}
			}
		}
		//Since we don't draw backfaces, we could've also checked if the polys formed a convex surface
		//and just put it in flat list, but we never got around to it
		
		this.plane = splitter;
		this.polygons = middle;
		
		//null is used to indicate the base case of no more faces
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
		//Determines which side of the splitting plane we are on
		//and decide which half to go in the back
		if (dist < 0) {
			first = far;
			last = near;
		} else {
			first = near;
			last = far;
		}
		
		//Recursively render back to front
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
