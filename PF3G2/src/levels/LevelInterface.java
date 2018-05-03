package levels;

import java.util.List;
import model.Block;
import math.Vector;

public interface LevelInterface {
	public List<Block> getBlocks();
	public Vector getStartPosition();
	public float resetYValue();
	public Block getEndBlock();
	public LevelInterface nextLevel();
}
