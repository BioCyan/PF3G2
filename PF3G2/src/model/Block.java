package model;

import java.util.Random;
import math.*;

import java.awt.*;

public class Block {
	private static Random rand = new Random();
	
	private Vector mins;
	private Vector maxs;
	
	public Block(Vector mins, Vector maxs) {
		this.mins = mins;
		this.maxs = maxs;
	}
	
	public Block(float size, Vector pos) {
		Vector sizeOffset = (new Vector(size, size, size)).times(0.5f);
		this.mins = pos.minus(sizeOffset);
		this.maxs = pos.plus(sizeOffset);
	}
	
	public Vector getMins() {return mins;}
	public Vector getMaxs() {return maxs;}
	
	public Poly[] getPolys() {
		Poly[] result = new Poly[6];
		
		//I could just write out all eight vertices and six faces
		//but what fun is that?
		Vector[] verts = new Vector[8];
		for (int i = 0; i < 8; i++) {
			float[] coords = new float[3];
			int j = 1;
			for (int axis = 0; axis < 3; axis++) {
				//Interpret the bits of i as which side of the cube
				//along each axis each vertex is
				boolean side = (i / j % 2) == 1;
				if (side) {
					coords[axis] = maxs.get(axis + 1);
				} else {
					coords[axis] = mins.get(axis + 1);
				}
				j *= 2;
			}
			verts[i] = new Vector(coords[0], coords[1], coords[2]);
		}
		
		//Values of binary digits
		int[] digits = {1, 2, 4};
		for (int i = 0; i < 6; i++) {
			Vector[] polyVerts = new Vector[4];
			int axis = i / 2;
			
			//Which side of the cube we're adding our face
			int side1 = i % 2;
			//Keeps track of which side we're adding the next vertex
			int side2 = 0;
			//Like side2 but on a different access
			int side3 = 0;
			
			for (int j = 0; j < 4; j++) {
				//Use our sides to choose the axis digits of our vertices
				//depending on which axis the poly is facing
				int index = digits[axis] * side1;
				index += digits[(axis + 1) % 3] * side2;
				index += digits[(axis + 2) % 3] * side3;
				polyVerts[j] = verts[index];
				
				//Flip the sides in such a way that we go in a counterclockwise loop
				if (j % 2 != side1) {
					side2 = 1 - side2;
				} else {
					side3 = 1 - side3;
				}
			}
			
			Color randColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
			result[i] = new Poly(polyVerts, randColor);
		}
		
		return result;
	}
}
