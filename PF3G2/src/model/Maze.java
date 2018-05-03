package model;

import java.util.Random;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import math.*;

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
		start = new Cell();
		cellStack.push(start);
		
		while (!cellStack.isEmpty()) {
			Cell cell = cellStack.pop();
			cell.set(true);
			
			int tryFirst = rand.nextInt(4);
			for (int i = 0; i < 4; i++) {
				int direction = (i + tryFirst) % 4;
				if (cell.validPath(direction) && !cell.reconnects(direction)) {
					cellStack.push(cell);
					end = cell.move(direction);
					cellStack.push(end);
					break;
				}
			}
		}
	}
	
	public Vector getStart() {
		return start.getPosition().times(scale);
	}
	
	public Vector getEnd() {
		return end.getPosition().times(scale);
	}
	
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