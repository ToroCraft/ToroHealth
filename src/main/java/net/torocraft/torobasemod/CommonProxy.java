package net.torocraft.torobasemod;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.torocraft.torobasemod.block.ToroBaseModBlocks;
import net.torocraft.torobasemod.crafting.ToroBaseModRecipes;
import net.torocraft.torobasemod.item.ToroBaseModItems;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {

    }

    public void init(FMLInitializationEvent e) {
        ToroBaseModItems.init();
    	ToroBaseModBlocks.init();
    	ToroBaseModRecipes.init();
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}
