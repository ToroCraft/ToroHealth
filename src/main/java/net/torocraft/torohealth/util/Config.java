package net.torocraft.torohealth.util;

public class Config {

  private final static String[] acceptedColors = new String[]{"RED", "GREEN", "BLUE", "YELLOW", "ORANGE", "WHITE", "BLACK", "PURPLE"};

  public enum Mode {NONE, WHEN_HOLDING_WEAPON, ALWAYS, WHEN_HURT, WHEN_HURT_TEMP}

  public enum NumberType {NONE, LAST, CUMULATIVE}

  public Hud hud = new Hud();
  public Bar bar = new Bar();
  public InWorld inWorld = new InWorld();
  public Particle particle = new Particle();

  public static class Hud {
    public int distance = 60;
    public Integer x = 0;
    public Integer y = 0;
    public String skin = "BASIC";
    public int hideDelay = 400;
    public boolean showEntity = true;
  }

  public static class Particle {
    public boolean show = true;
    public String statusDisplayPosition = "TOP LEFT";
    public Integer damageColor = 0xff0000;
    public Integer healColor = 0x00ff00;
  }

  public static class Bar {
    public NumberType damageNumberType = NumberType.LAST;
  }

  public static class InWorld {
    public Mode mode = Mode.ALWAYS;
    public float distance = 60f;
  }

  private static int mapColor(String color) {
    if (color.equals("RED")) {
      return 0xff0000;
    } else if (color.equals("GREEN")) {
      return 0x00ff00;
    } else if (color.equals("BLUE")) {
      return 0x0000ff;
    } else if (color.equals("YELLOW")) {
      return 0xffff00;
    } else if (color.equals("ORANGE")) {
      return 0xffa500;
    } else if (color.equals("BLACK")) {
      return 0x000000;
    } else if (color.equals("PURPLE")) {
      return 0x960096;
    } else {
      return 0xffffff;
    }
  }


}
