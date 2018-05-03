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
		platforms.add(new Block(new Vector(5, 0.25f, 0), new Vector(7, 0.5f, 2)));
		platforms.add(new Block(new Vector(10, 0.25f, 0), new Vector(12, 0.5f, 2)));
		platforms.add(new Block(new Vector(8, 0.25f, 2), new Vector(14, 10, 4)));
		//after corner
		platforms.add(new Block(new Vector(14, 0.25f, 3), new Vector(16, 0.5f, 5)));
		platforms.add(new Block(new Vector(16, 0.25f, 8), new Vector(2, 0.5f, 2)));
		/*platforms.add(new Block(new Vector(6, -0.50f, 6), new Vector(8, -0.25f, 8)));
		platforms.add(new Block(new Vector(0, -1.25f, 12), new Vector(2, -1, 14)));
		//ending
*/		platforms.add(getEndBlock());
	}
	
	public List<Block> getBlocks() {
		return platforms;
	}

	public Vector getStartPosition() {
		return new Vector(1, 4, 1);
	}

	public Block getEndBlock() {
		return new Block(new Vector(-3, -1, -3), new Vector(-1, -.5f, -1));
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
