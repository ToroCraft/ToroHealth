package net.torocraft.toroutils.generation;

import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;

public interface ICanvas {
	void setStyles(Style style);

	void moveTo(BlockPos pos);

	void line(Axis axis, int length);

	void rectangle(Axis normalAxis, BlockPos pos, int width, int height);
	
	void cuboid(BlockPos pos, int width, int height, int depth);
}