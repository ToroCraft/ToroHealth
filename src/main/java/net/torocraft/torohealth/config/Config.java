package net.torocraft.torohealth.config;

import net.torocraft.torohealth.config.loader.IConfig;

public class Config implements IConfig {
  public enum Mode {
    NONE, WHEN_HOLDING_WEAPON, ALWAYS
  }

  public enum NumberType {
    NONE, LAST, CUMULATIVE
  }

  public enum AnchorPoint {
    TOP_LEFT, TOP_CENTER, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
  }

  public boolean watchForChanges = true;
  public Hud hud = new Hud();
  public Bar bar = new Bar();
  public InWorld inWorld = new InWorld();
  public Particle particle = new Particle();

  public static class Hud {
    public int distance = 60;
    public float x = 4f;
    public float y = 4f;
    public float scale = 1f;
    public int hideDelay = 20;
    public AnchorPoint anchorPoint = AnchorPoint.TOP_LEFT;
    public boolean showEntity = true;
    public boolean showBar = true;
    public boolean showSkin = true;
    public boolean onlyWhenHurt = false;
  }

  public static class Particle {
    public boolean show = true;
    public Integer damageColor = 0xff0000;
    public Integer healColor = 0x00ff00;
  }

  public static class Bar {
    public NumberType damageNumberType = NumberType.LAST;
    public int friendColor = 0x00ff00ff;
    public int friendColorSecondary = 0x008000ff;
    public int foeColor = 0xff0000ff;
    public int foeColorSecondary = 0x800000ff;
  }

  public static class InWorld {
    public Mode mode = Mode.NONE;
    public float distance = 60f;
    public boolean onlyWhenLookingAt = false;
    public boolean onlyWhenHurt = false;
  }

  @Override
  public void update() {
  }

  @Override
  public boolean shouldWatch() {
    return watchForChanges;
  }

}
