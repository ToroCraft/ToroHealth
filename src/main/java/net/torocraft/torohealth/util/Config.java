package net.torocraft.torohealth.util;

public class Config {
  public enum Mode {NONE, WHEN_HOLDING_WEAPON, ALWAYS}
  public enum NumberType {NONE, LAST, CUMULATIVE}

  public Hud hud = new Hud();
  public Bar bar = new Bar();
  public InWorld inWorld = new InWorld();
  //public Particle particle = new Particle();

  public static class Hud {
    public int distance = 60;
    public float x = 4f;
    public float y = 4f;
    public float scale = 1f;
    public int hideDelay = 20;
//    public boolean showEntity = true;
  }

  public static class Particle {
    public boolean show = true;
    public String statusDisplayPosition = "TOP LEFT";
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
  }

}
