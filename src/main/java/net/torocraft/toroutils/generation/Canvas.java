package net.torocraft.toroutils.generation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Canvas implements ICanvas {

	private final World world;
	private final BlockPos origin;
	private int x;
	private int y;
	private int z;
	private Style style = new Style();
	
	private static final Axis X = Axis.X;
	private static final Axis Y = Axis.Y;
	private static final Axis Z = Axis.Z;

	public Canvas(World world, BlockPos origin) {
		this.world = world;
		this.origin = origin;
	}

	@Override
	public void setStyles(Style style) {
		this.style = style;
	}
	
	public Style getStyle() {
		return style;
	}

	@Override
	public void moveTo(BlockPos pos) {
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
	}

	
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	@Override
	public void rectangle(Axis normalAxis, BlockPos pos, int width, int depth) {
		
		moveTo(pos);
		line(X, width);
		line(Z, depth);
		line(X, -width);
		line(Z, -depth);
		
		
	}



	@Override
	public void cuboid(BlockPos pos, int width, int height, int depth) {
		moveTo(pos);
		for(x = pos.getX(); x < width; x++){
			for(y = pos.getY(); y < height; y++){
				for(z = pos.getZ(); z < depth; z++){
					
					fillBlock();
				}
			}
		}
	}

	private void strokeBlock() {
		//if (okToPlaceBlock()) {
			world.setBlockState(cursorCoords(), style.stroke);
		//}
	}

	private void fillBlock() {
		//if (okToPlaceBlock()) {
		
		
			world.setBlockState(cursorCoords(), style.fill);
		//}
	}
	
	@Override
	public void line(Axis axis, int length) {
		int l = computeTravelDistance(length);
		boolean isPositive = length >= 0;
		for (int i = 0; i < l; i++) {
			strokeBlock();
			if (i < l - 1) {
				if (isPositive) {
					incrementAxis(axis, 1);
				} else {
					incrementAxis(axis, -1);
				}
			}

		}
	}

	private int computeTravelDistance(int length) {
		return Math.abs(length);
	}

	private void incrementAxis(Axis axis, int amount) {
		switch (axis) {
		case X:
			x += amount;
			break;
		case Y:
			y += amount;
			break;
		case Z:
			z += amount;
			break;
		default:
			break;
		}
	}

	private boolean okToPlaceBlock() {
		return !style.ignoreOpaque || onAirBlock();
	}

	private boolean onAirBlock() {
		IBlockState currentBlock = world.getBlockState(cursorCoords());
		return !currentBlock.isOpaqueCube();
	}

	private BlockPos cursorCoords() {
		return origin.add(x, y, z);
	}

}
