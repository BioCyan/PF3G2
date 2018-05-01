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
		platforms.add(new Block(new Vector(2, -1, 2), new Vector(4, 0, 4)));
		return platforms;
	}

	@Override
	public Vector getStartPosition() {
		return new Vector(1, 2, 1);
	}

	@Override
	public Block getEndBlock() {
		// TODO Auto-generated method stub
		return null;
	}
}
