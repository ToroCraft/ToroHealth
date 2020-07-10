package net.torocraft.torohealth.util;

import io.github.prospector.modmenu.api.ModMenuApi;
import java.util.function.Function;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.torocraft.torohealth.ToroHealth;
import net.torocraft.torohealth.util.Config.Mode;
import net.torocraft.torohealth.util.Config.NumberType;

@Environment(EnvType.CLIENT)
public class ModMenuEntry implements ModMenuApi {

  @Override
  public String getModId() {
    return ToroHealth.MODID;
  }
  
  private static Text title(String text) {
	  return new TranslatableText(text);
  } 

  private static final Config DEFAULTS = new Config();

  @Override
  public Function<Screen, ? extends Screen> getConfigScreenFactory() {
    return (screen) -> {
      ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle(title("config.title"));

      ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
      Config newConfig = ConfigLoader.get();

      ConfigCategory hudCategory = builder.getOrCreateCategory(title("config.category.hud"));

      hudCategory.addEntry(entryBuilder
          .startIntField(title("config.hud.hideDelay"), newConfig.hud.hideDelay)
          .setSaveConsumer(val -> newConfig.hud.hideDelay = val)
          .setDefaultValue(DEFAULTS.hud.hideDelay)
          .build());

      hudCategory.addEntry(entryBuilder
          .startFloatField(title("config.hud.x"), newConfig.hud.x)
          .setSaveConsumer(val -> newConfig.hud.x = val)
          .setDefaultValue(DEFAULTS.hud.x)
          .build());

      hudCategory.addEntry(entryBuilder
          .startFloatField(title("config.hud.y"), newConfig.hud.y)
          .setSaveConsumer(val -> newConfig.hud.y = val)
          .setDefaultValue(DEFAULTS.hud.y)
          .build());

      hudCategory.addEntry(entryBuilder
          .startFloatField(title("config.hud.scale"), newConfig.hud.scale)
          .setSaveConsumer(val -> newConfig.hud.scale = val)
          .setDefaultValue(DEFAULTS.hud.scale)
          .build());

      ConfigCategory barCategory = builder.getOrCreateCategory(title("config.torohealth.category.bar"));

      barCategory.addEntry(entryBuilder
          .startEnumSelector(title("config.bar.damageNumberType"), NumberType.class, newConfig.bar.damageNumberType)
          .setSaveConsumer(val -> newConfig.bar.damageNumberType = val)
          .setDefaultValue(DEFAULTS.bar.damageNumberType)
          .build());

      ColorSection friendColor = new ColorSection(entryBuilder, barCategory, "config.torohealth.friendColor.title", newConfig.bar.friendColor, DEFAULTS.bar.friendColor);
      ColorSection friendColorSecondary = new ColorSection(entryBuilder, barCategory, "config.torohealth.friendColorSecondary.title", newConfig.bar.friendColorSecondary, DEFAULTS.bar.friendColorSecondary);
      ColorSection foeColor = new ColorSection(entryBuilder, barCategory, "config.torohealth.foeColor.title", newConfig.bar.foeColor, DEFAULTS.bar.foeColor);
      ColorSection foeColorSecondary = new ColorSection(entryBuilder, barCategory, "config.torohealth.foeColorSecondary.title", newConfig.bar.foeColorSecondary, DEFAULTS.bar.foeColorSecondary);

      friendColor.build();
      friendColorSecondary.build();
      foeColor.build();
      foeColorSecondary.build();

      ConfigCategory inWorldCategory = builder.getOrCreateCategory(title("config.torohealth.category.inWorld"));

      inWorldCategory.addEntry(entryBuilder
          .startEnumSelector(title("config.inWorld.mode"), Mode.class, newConfig.inWorld.mode)
          .setSaveConsumer(val -> newConfig.inWorld.mode = val)
          .setDefaultValue(DEFAULTS.inWorld.mode)
          .build());

      inWorldCategory.addEntry(entryBuilder
          .startFloatField(title("config.inWorld.distance"), newConfig.inWorld.distance)
          .setSaveConsumer(val -> newConfig.inWorld.distance = val)
          .setDefaultValue(DEFAULTS.inWorld.distance)
          .build());


      builder.setSavingRunnable(() -> {

        newConfig.bar.friendColor = friendColor.getColor();
        newConfig.bar.friendColorSecondary = friendColorSecondary.getColor();
        newConfig.bar.foeColor = foeColor.getColor();
        newConfig.bar.foeColorSecondary = foeColorSecondary.getColor();

        ConfigLoader.set(newConfig);
      });

      return builder.build();
    };
  }

  private static int getRed(int color) {
    return getByte(color, 3);
  }

  private static int getGreen(int color) {
    return getByte(color, 2);
  }

  private static int getBlue(int color) {
    return getByte(color, 1);
  }

  private static int getAlpha(int color) {
    return getByte(color, 0);
  }

  private static int setRed(int color, int value) {
    return setByte(color, (byte) value, 3);
  }

  private static int setGreen(int color, int value) {
    return setByte(color, (byte) value, 2);
  }

  private static int setBlue(int color, int value) {
    return setByte(color, (byte) value, 1);
  }

  private static int setAlpha(int color, int value) {
    return setByte(color, (byte) value, 0);
  }

  public static int getByte(int l, int position) {
    return (l >> 8 * position) & 0x0ff;
  }

  public static int setByte(int i, byte b, int position) {
    if (position > 3) {
      throw new IndexOutOfBoundsException("byte position of " + position);
    }
    int mask = ~(0xff << position * 8);
    int insert = (b << position * 8) & ~mask;
    return i & mask | insert;
  }

  private static class ColorSection {

    private ConfigEntryBuilder entryBuilder;
    private ConfigCategory category;
    private Text title;
    private int color;
    private int defaultColor;

    public ColorSection(ConfigEntryBuilder entryBuilder, ConfigCategory category, String title, int color, int defaultColor) {
      this.entryBuilder = entryBuilder;
      this.category = category;
      this.title = new TranslatableText(title);
      this.color = color;
      this.defaultColor = defaultColor;
    }

    public void build() {
      SubCategoryBuilder sub = entryBuilder.startSubCategory(title);
      sub.add(entryBuilder.startIntSlider(title("config.torohealth.red"), getRed(color), 0, 255).setSaveConsumer(v -> color = setRed(color, v)).setDefaultValue(getRed(defaultColor)).build());
      sub.add(entryBuilder.startIntSlider(title("config.torohealth.green"), getGreen(color), 0, 255).setSaveConsumer(v -> color = setGreen(color, v)).setDefaultValue(getGreen(defaultColor)).build());
      sub.add(entryBuilder.startIntSlider(title("config.torohealth.blue"), getBlue(color), 0, 255).setSaveConsumer(v -> color = setBlue(color, v)).setDefaultValue(getBlue(defaultColor)).build());
      sub.add(entryBuilder.startIntSlider(title("config.torohealth.alpha"), getAlpha(color), 0, 255).setSaveConsumer(v -> color = setAlpha(color, v)).setDefaultValue(getAlpha(defaultColor)).build());
      category.addEntry(sub.build());
    }

    public int getColor() {
      return color;
    }
  }

}