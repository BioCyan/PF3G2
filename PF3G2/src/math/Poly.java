package math;

import java.awt.*;
import java.util.ArrayList;

public class Poly {
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
		
		Plane nearPlane = new Plane(3, 1.0f/32);
		Poly renderPoly = project(camera).clip(nearPlane, true);
		
		if (renderPoly == null) {
			return;
		}
		if (renderPoly.getPlane().distance(new Vector()) < 0) {
			return;
		}
		
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
		graphics.setColor(color);
		graphics.drawPolygon(polygon);
	}
	
	public Plane getPlane() {
		Vector normal = (vertices[0].minus(vertices[1]))
				.cross(vertices[0].minus(vertices[2]))
				.unit();
		float position = vertices[0].dot(normal);
		return new Plane(normal, position);
	}
	
	public Poly project(Transform camera) {
		Vector[] newVerts = new Vector[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			Vector vert = vertices[i];
			newVerts[i] = camera.worldToLocal(vert);
		}
		
		return new Poly(newVerts, color);
	}
	
	public Poly clip(Plane plane, boolean side) {
		ArrayList<Vector> newVerts = new ArrayList<Vector>();
		for (int i = 0; i < vertices.length; i++) {
			Vector current = vertices[i];
			float currentDist = plane.distance(current);
			if ((currentDist > 0) == side) {
				newVerts.add(current);
			} else {
				Vector prev = vertices[(i + vertices.length - 1) % vertices.length];
				Vector next = vertices[(i + 1) % vertices.length];
				float prevDist = plane.distance(prev);
				float nextDist = plane.distance(next);
				
				if ((prevDist > 0) == side) {
					float interp = currentDist/(currentDist - prevDist);
					newVerts.add(current.times(1 - interp).plus(prev.times(interp)));
				}
				if ((nextDist > 0) == side) {
					float interp = currentDist/(currentDist - nextDist);
					newVerts.add(current.times(1 - interp).plus(next.times(interp)));
				}
			}
		}
		
		if (newVerts.size() == 0) {
			return null;
		}
		
		Vector[] vertArray = new Vector[newVerts.size()];
		int i = 0;
		for (Vector vert : newVerts) {
			vertArray[i] = vert;
			i++;
		}
		
		return new Poly(vertArray, color);
	}
}
