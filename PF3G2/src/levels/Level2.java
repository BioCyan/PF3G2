package levels;

import java.util.ArrayList;
import java.util.List;
import math.Vector;
import model.Block;

public class Level2 implements LevelInterface {

	private List<Block> platforms;
	private Level2 level2;
	
	public Level2() {
		platforms = new ArrayList<Block>();
		//level3 = new Level3();
	}
	
	public List<Block> getBlocks() {
		platforms.add(new Block(new Vector(-1, -2, -1), new Vector(8, -1, 8)));
		platforms.add(new Block(new Vector(2, -1, 2), new Vector(4, -0.5f, 4)));
		//platforms.add(getEndBlock());
		return platforms;
	}

	public Vector getStartPosition() {
		return new Vector(1, 2, 1);
	}

	public Block getEndBlock() {
		
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

}
