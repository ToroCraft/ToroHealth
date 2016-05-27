package net.torocraft.toroutils.generation;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockGlowstone;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CuboidCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "cuboid";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.cuboid.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		System.out.println("cuboid");
		BlockPos pos = sender.getPosition();
		Canvas c = new Canvas(sender.getEntityWorld(), sender.getPosition());
		c.setStyles(new Style());
		c.getStyle().fill = Blocks.diamond_block.getDefaultState();
		c.getStyle().stroke = Blocks.diamond_block.getDefaultState();
		c.cuboid(new BlockPos(0, 0, 0), 500, 10, 500);
	}

}
