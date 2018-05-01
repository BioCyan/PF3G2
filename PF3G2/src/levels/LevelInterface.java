package levels;

import java.util.List;
import java.util.ArrayList;
import model.Block;
import math.Vector;

public interface LevelInterface {
	public List<Block> getBlocks();
	public Vector getStartPosition();
	public Block getEndBlock();
}
