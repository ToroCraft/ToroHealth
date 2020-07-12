package net.torocraft.torohealth.util;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class Config {
  public enum Mode {
    NONE, WHEN_HOLDING_WEAPON, ALWAYS
  }
  public enum NumberType {
    NONE, LAST, CUMULATIVE
  }

  public Hud hud;
  public Bar bar;
  public InWorld inWorld;

  public Config(ForgeConfigSpec.Builder builder) {
    hud = new Hud(builder);
    bar = new Bar(builder);
    inWorld = new InWorld(builder);
  }

  public static class Hud {
    public IntValue distance; // = 60;
    public DoubleValue x; // = 4f;
    public DoubleValue y; // = 4f;
    public DoubleValue scale; // = 1f;
    public IntValue hideDelay; // = 20;

    public Hud(ForgeConfigSpec.Builder builder) {
      builder.comment("ToroHealth HUD Configurations").push("hud");

      distance = builder.comment("Max distance HUD will show when looking at entity")
          .defineInRange("distance", 60, 1, 256);

      x = builder.defineInRange("x", 4d, -2000, 2000);

      y = builder.defineInRange("y", 4d, -2000, 2000);

      scale = builder.defineInRange("scale", 1d, 0, 20);

      hideDelay = builder.defineInRange("hideDelay", 20, 0, 1000);

      builder.pop();
    }
  }

  public static class Bar {
    public EnumValue<NumberType> damageNumberType;// = NumberType.LAST;
    public int friendColor = 0x00ff00ff;
    public int friendColorSecondary = 0x008000ff;
    public int foeColor = 0xff0000ff;
    public int foeColorSecondary = 0x800000ff;

    public Bar(ForgeConfigSpec.Builder builder) {
      builder.comment("Health bar settings").push("bar");

      damageNumberType = builder.defineEnum("damageNumberType", NumberType.NONE);

      // friendColor = builder
      // .defineInRange("friendColor", 0x00ff00ff, 0x000000ff, 0xffffffff);

      // friendColorSecondary = builder
      // .defineInRange("friendColorSecondary", 0x008000ff, 0x000000ff, 0xffffffff);

      // foeColor = builder
      // .defineInRange("foeColor", 0xff0000ff, 0x000000ff, 0xffffffff);

      // foeColorSecondary = builder
      // .define("foeColorSecondary", 0x800000ff);

      builder.pop();
    }
  }

  public static class InWorld {
    public EnumValue<Mode> mode; // = Mode.NONE;
    public DoubleValue distance; // = 60f;

    public InWorld(ForgeConfigSpec.Builder builder) {
      builder.comment("Health bars in-world over entities").push("inworld");

      mode = builder.defineEnum("mode", Mode.NONE);

      distance = builder.defineInRange("distance", 60f, 1, 256);

      builder.pop();
    }
  }

}
