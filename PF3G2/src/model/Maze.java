package model;

import java.util.Random;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import math.*;

public class Maze {
	//Used by the maze generator to abstract a position
	//when moving and checking possible paths
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
		
		//Checks if out of bounds
		public boolean valid() {
			return row >= 0
					&& column >= 0
					&& row < grid.length
					&& column < grid[0].length;
		}
		
		//Don't go out of bounds or cut into other paths
		public boolean validPath(int direction) {
			Cell path = move(direction);
			
			if (!path.valid() || path.check()) {
				return false;
			}
			return true;
		}
		
		//Checks if moving in a direction connects back to old paths
		//so we don't dig out the whole maze endlessly
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
		
		public Vector getPosition() {
			return new Vector(row, 0.5f, column);
		}
	}
	
	private boolean[][] grid;
	private float scale;
	private Cell start;
	private Cell end;
	
	public Maze(int rows, int columns, float scale) {
		this.scale = scale;
		
		Random rand = new Random();
		grid = new boolean[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				grid[i][j] = false;
			}
		}
		
		Stack<Cell> cellStack = new Stack<Cell>();
		List<Cell> endList = new ArrayList<Cell>();
		start = new Cell();
		cellStack.push(start);
		
		
		boolean reversing = false;
		while (!cellStack.isEmpty()) {
			Cell cell = cellStack.pop();
			cell.set(true);
			
			boolean succeeded = false;
			int tryFirst = rand.nextInt(4);
			for (int i = 0; i < 4; i++) {
				int direction = (i + tryFirst) % 4;
				if (cell.validPath(direction) && !cell.reconnects(direction)) {
					succeeded = true;
					reversing = false;
					cellStack.push(cell);
					cellStack.push(cell.move(direction));
					break;
				}
			}
			
			if (!succeeded && !reversing) {
				reversing = true;
				endList.add(cell);
			}
		}
		
		float maxDistance = 0;
		for (Cell cell : endList) {
			float distance = start.getPosition().minus(cell.getPosition()).length();
			if (distance > maxDistance) {
				maxDistance = distance;
				end = cell;
			}
		}
	}
	
	//Player spawn position
	public Vector getStart() {
		return start.getPosition().times(scale);
	}
	
	//Level end position
	public Vector getEnd() {
		return end.getPosition().times(scale);
	}
	
	//Convert to Block objects
	public List<Block> getBlocks() {
		List<Block> result = new ArrayList<Block>();
		for (int i = -1; i <= grid.length; i++) {
			for (int j = -1; j <= grid[0].length; j++) {
				Cell cell = new Cell(i, j);
				if (!cell.valid() || !cell.check()) {
					result.add(new Block(scale, (new Vector(i, 0.5f, j)).times(scale)));
				}
			}
		}
		return result;
	}
	
	//Was used to test before 3D was working
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
