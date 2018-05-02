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

	@Override
	public List<Block> getBlocks() {
		platforms.add(new Block(new Vector(-1, -2, -1), new Vector(8, -1, 8)));
		platforms.add(new Block(new Vector(2, -1, 2), new Vector(4, -0.5f, 4)));
		platforms.add(getEndBlock());
		return platforms;
	}

	@Override
	public Vector getStartPosition() {
		return new Vector(1, 2, 1);
	}

	@Override
	public Block getEndBlock() {
		Block end = new Block(new Vector(6, -1, 6), new Vector(8, -.5f, 8));
		return end;
	}

	@Override
	public float resetYValue() {
		float resetValue = 0;
		for(Block something: platforms) {
			if(something.getMins().y() < resetValue) {
				resetValue = something.getMins().y();
			}
		}
		return resetValue;
	}

	@Override
	public LevelInterface nextLevel() {
		return level2;
	}
}
