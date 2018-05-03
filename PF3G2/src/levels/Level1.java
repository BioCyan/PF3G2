package levels;

import model.Block;
import math.*;
import java.util.List;
import java.util.ArrayList;

public class Level1 implements LevelInterface {
	private List<Block> platforms;
	
	public Level1() {
		platforms = new ArrayList<Block>();
		//floor
			platforms.add(new Block(new Vector(-1, -2, -1), new Vector(8, -1, 8)));
			//walls
			platforms.add(new Block(new Vector(-1, -1, -1), new Vector(8, 10, 0)));
			platforms.add(new Block(new Vector(8, -1, 0), new Vector(9, 10, 8)));
			platforms.add(new Block(new Vector(-1, -1, 8), new Vector(8, 10, 9)));
			platforms.add(new Block(new Vector(-2, -1, -1), new Vector(-1, 10, 8)));
			//platforms
			platforms.add(new Block(new Vector(2, -1, 2), new Vector(4, -0.25f, 4)));
			platforms.add(new Block(new Vector(4, -0.25f, 4), new Vector(6, 0.50f, 6)));
			platforms.add(new Block(new Vector(4, 0.5f, 0), new Vector(6, 1, 2)));
			platforms.add(new Block(new Vector(6, 1.01f, 2), new Vector(8, 1.75f, 4)));
			platforms.add(new Block(new Vector(6, 1.75f, 6), new Vector(8, 2.50f, 8)));
			platforms.add(new Block(new Vector(0, 2.25f, 6), new Vector(2, 2.50f, 8)));
			platforms.add(new Block(new Vector(0, 2.25f, 1), new Vector(2, 2.50f, 3)));
			//exit blocks
			platforms.add(new Block(new Vector(2, 2.25f, 0), new Vector(3, 4.50f, 1)));
			platforms.add(new Block(new Vector(-1, 2.25f, 0), new Vector(0, 4.50f, 1)));
			platforms.add(new Block(new Vector(-1, 4.50f, 0), new Vector(3, 5.50f, 1)));
			platforms.add(getEndBlock());
	}
 
	public List<Block> getBlocks() {
		return platforms;
	}

	public Vector getStartPosition() {
		return new Vector(1, 0, 1);
	}

	public Block getEndBlock() {
		Block end = new Block(new Vector(0, 2.25f, 0), new Vector(2, 2.50f, 1));
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
		return new Level2();
	}
}
