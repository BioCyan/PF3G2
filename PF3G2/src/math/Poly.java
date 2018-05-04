package math;

import java.awt.*;
import java.util.ArrayList;

public class Poly {
	//This really should have been a list
	private Vector[] vertices;
	private Color color;
	
	public Poly(Vector[] vertices, Color color) {
		this.vertices = vertices;
		this.color = color;
	}
	
	public void render(Graphics graphics, Transform camera, Dimension screenSize, float fov) {
		fov = Mathf.tan(fov*Mathf.PI/360.0f);
		float x = (float)screenSize.getWidth()/2;
		float y = (float)screenSize.getHeight()/2;
		
		//Transform vertices to be relative to the camera
		//And remove all geometry behind us to avoid causing issues
		Plane nearPlane = new Plane(3, 1.0f/32);
		Poly renderPoly = project(camera).clip(nearPlane, true);
		
		//Check in case the poly was fully removed by the clip
		if (renderPoly == null) {
			return;
		}
		
		//Do not render polys facing away, that's just the hidden inside of stuff
		if (renderPoly.getPlane().distance(new Vector()) < 0) {
			return;
		}
		//We never really took advantage of this, and it looks like it caused issues with the monkey
		
		Polygon polygon = new Polygon();
		Vector[] vertices = renderPoly.vertices;
		for (int i = 0; i < vertices.length; i++) {
			Vector v = vertices[i];
			int xCoord = (int)(-v.x()/v.z()*y*fov + x);
			int yCoord = (int)(-v.y()/v.z()*y*fov + y);
			polygon.addPoint(xCoord, yCoord);
		}
		graphics.setColor(color);
		graphics.fillPolygon(polygon);
		graphics.drawPolygon(polygon);
	}
	
	//Creates the plane that the poly is coplanar with
	public Plane getPlane() {
		Vector normal = (vertices[0].minus(vertices[1]))
				.cross(vertices[0].minus(vertices[2]))
				.unit();
		float position = vertices[0].dot(normal);
		return new Plane(normal, position);
	}
	
	//Return a poly with its coordinates relative to the camera
	public Poly project(Transform camera) {
		Vector[] newVerts = new Vector[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			Vector vert = vertices[i];
			newVerts[i] = camera.worldToLocal(vert);
		}
		
		return new Poly(newVerts, color);
	}
	
	//Checks if we're exactly on top of a plane
	public boolean coplanar(Plane plane) {
		for (int i = 0; i < vertices.length; i++) {
			if (plane.distance(vertices[i]) != 0) {
				return false;
			}
		}
		return true;
	}
	
	public Poly clip(Plane plane, boolean side) {
		ArrayList<Vector> newVerts = new ArrayList<Vector>();
		for (int i = 0; i < vertices.length; i++) {
			Vector current = vertices[i];
			float currentDist = plane.distance(current);
			if (currentDist == 0 || (currentDist > 0) == side) {
				//Vertices on the correct side are kept
				newVerts.add(current);
			} else {
				//If on the wrong side check if it is connected to a vertex on the other side
				//and add vertices at the points where the connecting edges intersect
				Vector prev = vertices[(i + vertices.length - 1) % vertices.length];
				Vector next = vertices[(i + 1) % vertices.length];
				float prevDist = plane.distance(prev);
				float nextDist = plane.distance(next);
				
				if (prevDist != 0 && (prevDist > 0) == side) {
					float interp = currentDist/(currentDist - prevDist);
					newVerts.add(current.times(1 - interp).plus(prev.times(interp)));
				}
				if (nextDist != 0 && (nextDist > 0) == side) {
					float interp = currentDist/(currentDist - nextDist);
					newVerts.add(current.times(1 - interp).plus(next.times(interp)));
				}
			}
		}
		
		//If completely clipped away, indicate with null
		if (newVerts.size() < 3) {
			return null;
		}
		
		//vertices uses an array and we never got around to changing it
		Vector[] vertArray = new Vector[newVerts.size()];
		int i = 0;
		for (Vector vert : newVerts) {
			vertArray[i] = vert;
			i++;
		}
		
		return new Poly(vertArray, color);
	}
}
