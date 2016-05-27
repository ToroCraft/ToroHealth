package net.torocraft.torobasemod.item.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.torocraft.torobasemod.ToroBaseMod;
import net.torocraft.torobasemod.material.ArmorMaterials;
import net.torocraft.torobasemod.util.ToroBaseUtils;

public class ItemSamuraiArmor extends ItemArmor {

	public static final String NAME = "samurai";

	public static ItemSamuraiArmor helmetItem;
	public static ItemSamuraiArmor chestplateItem;
	public static ItemSamuraiArmor leggingsItem;
	public static ItemSamuraiArmor bootsItem;

	public static void init() {
		initHelmet();
		initChestPlate();
		initLeggings();
		initBoots();
	}

	public static void registerRenders() {
		registerRendersHelmet();
		registerRendersChestPlate();
		registerRendersLeggings();
		registerRendersBoots();
	}

	private static void initBoots() {
		bootsItem = new ItemSamuraiArmor(NAME + "_boots", 1, EntityEquipmentSlot.FEET);
		ToroBaseUtils.registerItem(bootsItem, NAME + "_boots");
	}

	private static void registerRendersBoots() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(bootsItem, 0, model("boots"));
	}

	private static void initLeggings() {
		leggingsItem = new ItemSamuraiArmor(NAME + "_leggings", 2, EntityEquipmentSlot.LEGS);
		ToroBaseUtils.registerItem(leggingsItem, NAME + "_leggings");
	}

	private static void registerRendersLeggings() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(leggingsItem, 0, model("leggings"));
	}

	private static void initHelmet() {
		helmetItem = new ItemSamuraiArmor(NAME + "_helmet", 1, EntityEquipmentSlot.HEAD);
		ToroBaseUtils.registerItem(helmetItem, NAME + "_helmet");
	}

	private static void registerRendersHelmet() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(helmetItem, 0, model("helmet"));
	}

	private static void initChestPlate() {
		chestplateItem = new ItemSamuraiArmor(NAME + "_chestplate", 1, EntityEquipmentSlot.CHEST);
		ToroBaseUtils.registerItem(chestplateItem, NAME + "_chestplate");
	}

	private static void registerRendersChestPlate() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(chestplateItem, 0, model("chestplate"));
	}

	private static ModelResourceLocation model(String model) {
		return new ModelResourceLocation(ToroBaseMod.MODID + ":" + NAME + "_" + model, "inventory");
	}

	public ItemSamuraiArmor(String unlocalizedName, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(ArmorMaterials.SAMURAI, renderIndexIn, equipmentSlotIn);
		this.setUnlocalizedName(unlocalizedName);
		setMaxDamage(5000);
	}

}
