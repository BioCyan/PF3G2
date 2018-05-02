package levels;

import model.Block;
import math.*;
import java.util.List;
import java.util.ArrayList;

public class Level1 implements LevelInterface {
	private List<Block> platforms;
	private Level2 level2;
	
	public Level1() {
		platforms = new ArrayList<Block>();
		level2 = new Level2();
	}
 
	public List<Block> getBlocks() {
		//floor
		platforms.add(new Block(new Vector(-1, -2, -1), new Vector(8, -1, 8)));
		//walls
		platforms.add(new Block(new Vector(-1, -1, -1), new Vector(8, 10, 0)));
		platforms.add(new Block(new Vector(8, -1, 0), new Vector(9, 10, 8)));
		platforms.add(new Block(new Vector(-1, -1, 8), new Vector(8, 10, 9)));
		platforms.add(new Block(new Vector(-2, -1, -1), new Vector(-1, 10, 8)));
		//platforms
		platforms.add(new Block(new Vector(2, -1, 2), new Vector(4, -0.5f, 4)));
		platforms.add(new Block(new Vector(4, -0.5f, 4), new Vector(6, 0, 6)));
		//platforms.add(getEndBlock());
		return platforms;
	}

	public Vector getStartPosition() {
		return new Vector(1, 2, 1);
	}

	public Block getEndBlock() {
		Block end = new Block(new Vector(6, -1, 6), new Vector(8, -.5f, 8));
		return end;
	}

	public float resetYValue() {
		float resetValue = 0;
		for(Block something: platforms) {
			if(something.getMins().y() < resetValue) {
				resetValue = something.getMins().y();
			}
		}
		return resetValue;
	}

	public LevelInterface nextLevel() {
		return level2;
	}
}
