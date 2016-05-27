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

public class ToroGenCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "torogen";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.torogen.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		//if(args[0].equals("verdererscabin")){
			new VerderersCabin(sender.getEntityWorld(), sender.getPosition()).generate();
		//}
	}

}
