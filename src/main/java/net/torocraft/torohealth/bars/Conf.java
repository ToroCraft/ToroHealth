package net.torocraft.torohealth.bars;

public class Conf {
    public enum Mode {NONE, WHEN_HOLDING_WEAPON, ALWAYS, WHEN_HURT, WHEN_HURT_TEMP}
    public enum NumberType {NONE, LAST, CUMULATIVE}

    public static Mode showBarsAboveEntities = Mode.ALWAYS;
    public static float distance = 60f;
    public static NumberType numberType = NumberType.LAST;
}
