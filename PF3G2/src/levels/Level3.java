package levels;

import java.util.List;

import math.Vector;
import model.Block;
import model.Maze;

public class Level3 implements LevelInterface {
	
	private List<Block> blocks;
	private Block endBlock;
	private Vector startPos;

	public Level3() {
		Maze maze = new Maze(16, 16, 2);
		blocks = maze.getBlocks();
		startPos = maze.getStart();
		blocks.add(new Block(new Vector(-1, -1, -1), new Vector(33, 0, 33)));
		Vector endPos = maze.getEnd();
		endBlock = new Block(endPos.plus(new Vector(-0.5f, -1, -0.5f)),
				endPos.plus(new Vector(0.5f, -0.75f, 0.5f)));
		blocks.add(endBlock);
	}
	
	public List<Block> getBlocks() {
		return blocks;
	}

	public Vector getStartPosition() {
		return startPos;
	}

	public float resetYValue() {
		return -5;
	}

	public Block getEndBlock() {
		return endBlock;
	}

	public LevelInterface nextLevel() {
		return null;
	}

}
