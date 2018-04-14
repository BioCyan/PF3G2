package model;

import math.*;
import java.awt.*;

public class Cube {
	public static Poly[] makeCube(float size, Vector pos, Color color) {
		Poly[] result = new Poly[6];
		Vector[] vertices = new Vector[8];
		for (int i = 0; i < 8; i++) {
			float x = (i % 2) - 0.5f;
			float y = (i / 2 % 2) - 0.5f;
			float z = (i / 4 % 2) - 0.5f;
			vertices[i] = pos.plus(new Vector(x, y, z));
		}
		int skip = 1;
		for (int i = 0; i < 3; i++) {
			Vector[] polyVerts1 = new Vector[4];
			Vector[] polyVerts2 = new Vector[4];
			for (int j = 0; j < 4; j++) {
				polyVerts1[j] = vertices[j*skip % 7];
				polyVerts2[j] = vertices[7 - (j*skip % 7)];
			}
			
			//Vertices go in loop order instead of zig-zag.
			Vector temp = polyVerts1[0];
			polyVerts1[0] = polyVerts1[1];
			polyVerts1[1] = temp;
			temp = polyVerts2[0];
			polyVerts2[0] = polyVerts2[1];
			polyVerts2[1] = temp;
			
			
			result[i] = new Poly(polyVerts1, color);
			result[i + 3] = new Poly(polyVerts2, color);
			skip *= 2;
		}
		return result;
	}
}
