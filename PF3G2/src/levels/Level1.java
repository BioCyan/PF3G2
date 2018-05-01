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
		platforms.add(new Block(new Vector(8, -1, 8), new Vector(0, -1, 0)));
		return platforms;
	}

	@Override
	public Vector setStartPosition(Vector position) {
		
		return null;
	}
}
