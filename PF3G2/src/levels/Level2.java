package levels;

import java.util.ArrayList;
import java.util.List;
import math.Vector;
import model.Block;

public class Level2 implements LevelInterface {

	private List<Block> platforms;
	
	public Level2() {
		platforms = new ArrayList<Block>();
		//platforms
		platforms.add(new Block(new Vector(0, 0.25f, 0), new Vector(2, 0.5f, 2)));
		platforms.add(new Block(new Vector(6, -0.50f, 6), new Vector(8, -0.25f, 8)));
		platforms.add(new Block(new Vector(0, -1.25f, 10), new Vector(2, -1, 12)));
		//ending
		platforms.add(getEndBlock());
	}
	
	public List<Block> getBlocks() {
		return platforms;
	}

	public Vector getStartPosition() {
		return new Vector(1, 4, 1);
	}

	public Block getEndBlock() {
		return new Block(new Vector(-1, -1, -1), new Vector(0, -.5f, 0));
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
		return new Level3();
	}

}
