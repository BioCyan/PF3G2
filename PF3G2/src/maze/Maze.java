package maze;

import java.util.Random;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import java.awt.*;
import math.*;
import model.Cube;

public class Maze {
	private class Cell {
		private int row;
		private int column;
		
		public Cell() {
			row = 0;
			column = 0;
		}
		
		public Cell(int row, int column) {
			this.row = row;
			this.column = column;
		}
		
		public boolean check() {
			return grid[row][column];
		}
		
		public Cell move(int direction) {
			int newRow = row;
			int newColumn = column;
			switch (direction) {
			case 0:
				newRow++;
				break;
			case 1:
				newColumn++;
				break;
			case 2:
				newRow--;
				break;
			case 3:
				newColumn--;
				break;
			}
			return new Cell(newRow, newColumn);
		}
		
		public boolean valid() {
			return row >= 0
					&& column >= 0
					&& row < grid.length
					&& column < grid[0].length;
		}
		
		public boolean validPath(int direction) {
			Cell path = move(direction);
			
			if (!path.valid() || path.check()) {
				return false;
			}
			return true;
		}
		
		public boolean reconnects(int direction) {
			Cell path = move(direction);
			int from = (direction + 2) % 4;
			for (int i = 0; i < 4; i++) {
				if (i != from) {
					Cell test = path.move(i);
					if (test.valid() && test.check()) {
						return true;
					}
				}
			}
			return false;
		}
		
		public void set(boolean value) {
			grid[row][column] = value;
		}
	}
	
	private boolean[][] grid;
	private Random rand;
	
	public Maze(int rows, int columns) {
		rand = new Random();
		grid = new boolean[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				grid[i][j] = false;
			}
		}
		
		Stack<Cell> cellStack = new Stack<Cell>();
		cellStack.push(new Cell());
		
		while (!cellStack.isEmpty()) {
			Cell cell = cellStack.pop();
			cell.set(true);
			
			int tryFirst = rand.nextInt(4);
			for (int i = 0; i < 4; i++) {
				int direction = (i + tryFirst) % 4;
				if (cell.validPath(direction) && !cell.reconnects(direction)) {
					cellStack.push(cell);
					cellStack.push(cell.move(direction));
					break;
				}
			}
		}
	}
	
	public BSPTree genBSP() {
		return genSubTree(0, 0, grid.length, grid[0].length,
				new Vector(-grid.length/2.0f, 0, -grid[0].length/2.0f));
	}
	
	private BSPTree genSubTree(int minX, int minZ, int maxX, int maxZ, Vector offset) {
		int diffX = maxX - minX;
		int diffZ = maxZ - minZ;
		
		if (diffX <= 1 && diffZ <= 1) {
			return null;
		}
		
		int axis;
		float distance;
		List<Poly> polygons = new ArrayList<Poly>();
		BSPTree near;
		BSPTree far;
		
		if (diffZ > diffX) {
			axis = 3;
			int midZ = (maxZ + minZ)/2;
			distance = midZ + offset.z();
			near = genSubTree(minX, minZ, maxX, midZ, offset);
			far = genSubTree(minX, midZ, maxX, maxZ, offset);
			for (int x = minX; x < maxX; x++) {
				if (grid[x][midZ - 1] != grid[x][midZ]) {
					Vector[] vertices = new Vector[4];
					vertices[0] = new Vector(x, 0, midZ);
					vertices[1] = new Vector(x + 1, 0, midZ);
					vertices[2] = new Vector(x + 1, 1, midZ);
					vertices[3] = new Vector(x, 1, midZ);
					for (int i = 0; i < 4; i++) {
						vertices[i] = vertices[i].plus(offset);
					}
					Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
					polygons.add(new Poly(vertices, color));
				}
			}
		} else {
			axis = 1;
			int midX = (maxX + minX)/2;
			distance = midX + offset.x();
			near = genSubTree(minX, minZ, midX, maxZ, offset);
			far = genSubTree(midX, minZ, maxX, maxZ, offset);
			for (int z = minZ; z < maxZ; z++) {
				if (grid[midX - 1][z] != grid[midX][z]) {
					Vector[] vertices = new Vector[4];
					vertices[0] = new Vector(midX, 0, z);
					vertices[1] = new Vector(midX, 0, z + 1);
					vertices[2] = new Vector(midX, 1, z + 1);
					vertices[3] = new Vector(midX, 1, z);
					for (int i = 0; i < 4; i++) {
						vertices[i] = vertices[i].plus(offset);
					}
					Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
					polygons.add(new Poly(vertices, color));
				}
			}
		}
		return new BSPTree(axis, distance, polygons, near, far);
	}
	
	public List<Poly> getPolys(Color color) {
		Random rand = new Random(1);
		
		List<Poly> result = new ArrayList<Poly>();
		float midX = grid.length/2.0f;
		float midY = grid[0].length/2.0f;
		for (int i = -1; i <= grid.length; i++) {
			for (int j = -1; j <= grid[0].length; j++) {
				Cell cell = new Cell(i, j);
				if (!cell.valid() || !cell.check()) {
					Color randColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
					float size = rand.nextFloat() + 0.5f;
					for (Poly poly : Cube.makeCube(size, new Vector(i - midX, size*0.5f - 0.5f, j - midY), randColor)) {
						result.add(poly);
					}
				}
			}
		}
		return result;
	}
	
	public String toString() {
		String result = "";
		for (int i = -1; i <= grid.length; i++) {
			for (int j = -1; j <= grid[0].length; j++) {
				Cell cell = new Cell(i, j);
				if (cell.valid() && cell.check()) {
					result += " ";
				} else {
					result += "\u2588";
				}
			}
			result += "\n";
		}
		
		return result;
	}
}
