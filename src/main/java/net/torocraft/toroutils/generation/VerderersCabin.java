package net.torocraft.toroutils.generation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VerderersCabin {
	
	private final BlockPos origin;
	private final World world;
	private final BlockMap map;
	
	public VerderersCabin(World world, BlockPos origin) {
		this.world = world;
		this.origin = origin;
		this.map = new BlockMap(world, origin);
	}
	
	public void generate() {
		map.loadLocalFile("/net/torocraft/generation/verderers_cabin.txt");
		map.generate();
	}
	
}
