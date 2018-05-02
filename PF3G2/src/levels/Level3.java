package levels;

import java.util.List;

import math.Vector;
import model.Block;
import maze.Maze;

public class Level3 implements LevelInterface {

	public List<Block> getBlocks() {
		List<Block> result = (new Maze(16, 16)).getBlocks(2);
		result.add(new Block(new Vector(-17, -1, -17), new Vector(17, 0, 17)));
		return result;
	}

	public Vector getStartPosition() {
		return new Vector(-16, 1, -16);
	}

	public float resetYValue() {
		return -5;
	}

	public Block getEndBlock() {
		return null;
	}

	public LevelInterface nextLevel() {
		return null;
	}

}
