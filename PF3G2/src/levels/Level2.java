package levels;

import java.util.ArrayList;
import java.util.List;
import math.Vector;
import model.Block;

public class Level2 implements LevelInterface {

	private List<Block> platforms;
	
	public Level2() {
		platforms = new ArrayList<Block>();
		//start platform
		platforms.add(new Block(new Vector(0, 0.25f, -2), new Vector(18, 10, 0)));
		platforms.add(new Block(new Vector(-2, 0.25f, 0), new Vector(0, 10, 4)));
		platforms.add(new Block(new Vector(0, 0.25f, 0), new Vector(2, 0.5f, 2)));
		//straight jumps
		platforms.add(new Block(new Vector(5, 0.25f, 0), new Vector(7, 0.5f, 2)));
		platforms.add(new Block(new Vector(10, 0.25f, 0), new Vector(12, 0.5f, 2)));
		//corner #1
		platforms.add(new Block(new Vector(8, 0.25f, 2), new Vector(14, 10, 8)));
		platforms.add(new Block(new Vector(14, 0.25f, 3), new Vector(16, 0.5f, 5)));
		platforms.add(new Block(new Vector(14, 0.25f, 8), new Vector(16, 0.5f, 10)));
		//corner #2
		platforms.add(new Block(new Vector(16, 0.25f, 8), new Vector(20, 10f, 12)));
		platforms.add(new Block(new Vector(17, 0.25f, 12), new Vector(19, 0.5f, 14)));
		//stairs
		platforms.add(new Block(new Vector(20, 0.25f, 11), new Vector(22, 0.5f, 12)));
		platforms.add(new Block(new Vector(20, 0.50f, 8), new Vector(22, 1, 9)));
		//stair corner 1
		platforms.add(new Block(new Vector(19, 1, 6), new Vector(20, 1.75f, 8)));
		platforms.add(new Block(new Vector(16, 1.5f, 6), new Vector(17, 2, 8)));
		// something
		platforms.add(new Block(new Vector(16, 2.25f, 0), new Vector(17, 2.5f, 2)));
		platforms.add(new Block(new Vector(11, 2.75f, 1), new Vector(12, 3, 2)));
		platforms.add(new Block(new Vector(6, 3.25f, 0), new Vector(7, 3.5f, 1)));
		//ending		
		platforms.add(getEndBlock());
	}
	
	public List<Block> getBlocks() {
		return platforms;
	}

	public Vector getStartPosition() {
		return new Vector(1, 2, 1);
	}

	public Block getEndBlock() {
		return new Block(new Vector(0, 2.75f, 4), new Vector(2, 3, 6));
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
