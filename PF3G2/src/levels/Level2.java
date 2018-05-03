package levels;

import java.util.ArrayList;
import java.util.List;
import math.Vector;
import model.Block;

public class Level2 implements LevelInterface {

	private List<Block> platforms;
	
	public Level2() {
		platforms = new ArrayList<Block>();
		platforms.add(new Block(new Vector(0, 0, 0), new Vector(2, 0.5f, 2)));
		platforms.add(getEndBlock());
	}
	
	public List<Block> getBlocks() {
		return platforms;
	}

	public Vector getStartPosition() {
		return new Vector(1, 2, 1);
	}

	public Block getEndBlock() {
		
		return new Block(new Vector(6, -1, 6), new Vector(8, -.5f, 8));
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
