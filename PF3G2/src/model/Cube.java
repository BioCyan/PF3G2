package model;

import math.*;
import java.awt.*;
import java.util.ArrayList;

public class Cube {
	
	public static Poly[] makeCube(float size, Vector pos) {
		Vector sizeOffset = (new Vector(size, size, size)).times(0.5f);
		Block block = new Block(pos.minus(sizeOffset), pos.plus(sizeOffset));
		return block.getPolys();
	}
	
	public static Block makeCubeBlock(float size, Vector pos) {
		Vector sizeOffset = (new Vector(size, size, size)).times(0.5f);
		Block block = new Block(pos.minus(sizeOffset), pos.plus(sizeOffset));
		return block;
	}
}
