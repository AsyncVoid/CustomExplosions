package me.Async.CE.storage;

import org.bukkit.block.Block;

public class BlockLoc {

	public final int x;
	public final int y;
	public final int z;
	
	public BlockLoc(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BlockLoc(Block b)
	{
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
	}
	
	@Override
    public boolean equals(Object o) {
        if(o == this)
        	return true;
        if(!(o instanceof BlockLoc))
        		return false;
        BlockLoc other = (BlockLoc) o;
        return other.x == this.x && other.y == this.y && other.z == this.z;
    }
	
	@Override
    public int hashCode() { //Objects.hash(x, y, z);
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }
}
