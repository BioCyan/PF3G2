package model;

import math.*;
import java.awt.*;

public class Cube {
	
	public static Poly[] makeCube(float size, Vector pos, Color color) {
		Vector sizeOffset = (new Vector(size, size, size)).times(0.5f);
		Block block = new Block(pos.minus(sizeOffset), pos.plus(sizeOffset));
		return block.getPolys(color);
	}
}
