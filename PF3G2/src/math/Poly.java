package math;

import java.awt.*;

public class Poly {
	private Vector[] vertices;
	private Color color;
	
	public Poly(Vector[] vertices, Color color) {
		this.vertices = vertices;
		this.color = color;
	}
	
	public void render(Graphics graphics, Transform camera, Dimension screenSize, float fov) {
		fov = Mathf.tan(fov);
		float x = (float)screenSize.getWidth()/2;
		float y = (float)screenSize.getHeight()/2;
		
		float nearPlane = 1.0f/32;
		
		Polygon polygon = new Polygon();
		for (int i = 0; i < vertices.length; i++) {
			Vector v = camera.worldToLocal(vertices[i]);
			
			if (v.z() >= nearPlane) {
				polygon.addPoint((int)(-v.x()/v.z()*y*fov + x),
						(int)(-v.y()/v.z()*y*fov + y));
			} else {
				Vector prev = camera.worldToLocal(
						vertices[(i + vertices.length - 1) % vertices.length]);
				Vector next = camera.worldToLocal(vertices[(i + 1) % vertices.length]);
				
				if (prev.z() >= nearPlane) {
					float interp = (v.z() - nearPlane)/(v.z() - prev.z());
					Vector a = v.times(1 - interp).plus(prev.times(interp));
					polygon.addPoint((int)(-a.x()/a.z()*y*fov + x),
							(int)(-a.y()/a.z()*y*fov + y));
				}
				if (next.z() >= nearPlane) {
					float interp = (v.z() - nearPlane)/(v.z() - next.z());
					Vector a = v.times(1 - interp).plus(next.times(interp));
					polygon.addPoint((int)(-a.x()/a.z()*y*fov + x),
							(int)(-a.y()/a.z()*y*fov + y));
				}
			}
		}
		graphics.setColor(color);
		graphics.fillPolygon(polygon);
		graphics.setColor(Color.BLACK);
		graphics.drawPolygon(polygon);
	}
}
