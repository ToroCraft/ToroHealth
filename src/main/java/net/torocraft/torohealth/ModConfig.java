package net.torocraft.torohealth;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;


@Config(name = "torohealth")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public static ModConfig INSTANCE;

    public static void init()
    {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    @ConfigEntry.Gui.CollapsibleObject
    public HudOptions hudOptions = new HudOptions();
    public static class HudOptions {
        @ConfigEntry.Gui.Tooltip
        public boolean showEntity = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showBar = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showSkin = true;

        @ConfigEntry.Gui.Tooltip
        public boolean onlyWhenHurt = false;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
        public AnchorPoint anchorPoint = AnchorPoint.TOP_LEFT;

        @ConfigEntry.Gui.Tooltip
        public int hudHideDelay = 20;

        @ConfigEntry.Gui.Tooltip
        public float hudDistance = 60f;

        @ConfigEntry.Gui.Tooltip
        public int hudXPosition = 4;

        @ConfigEntry.Gui.Tooltip
        public int hudYPosition = 4;

        @ConfigEntry.Gui.Tooltip
        public float hudScale = 1f;
    }


    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    public ParticleOptions particleOptions = new ParticleOptions();
    public static class ParticleOptions {
        @ConfigEntry.Gui.Tooltip
        public boolean show = true;

        @ConfigEntry.Gui.Tooltip
        public float particleDistance = 60f;

        @ConfigEntry.Gui.Excluded
        public transient float particleDistanceSquared = 0;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker
        public int damageColor = 0xff0000;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker
        public int healColor = 0x00ff00;
    }


    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    public InWorldBarOptions inWorldBarOptions = new InWorldBarOptions();
    public static class InWorldBarOptions {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
        public InWorldBarVisibilityMode inWorldBarVisibilityMode = InWorldBarVisibilityMode.NONE;

        @ConfigEntry.Gui.Tooltip
        public boolean onlyWhenLookingAt = false;

        @ConfigEntry.Gui.Tooltip
        public boolean onlyWhenHurt = false;

        @ConfigEntry.Gui.Tooltip
        public float inWorldBarDistance = 60f;
    }

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    public BarOptions barOptions = new BarOptions();
    public static class BarOptions {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
        public HealthChangeType healthChangeType= HealthChangeType.LAST;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker
        public int friendColor = 0x00ff00;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker
        public int friendColorSecondary = 0x008000;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker
        public int foeColor = 0xff0000;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.ColorPicker
        public int foeColorSecondary = 0x800000;
    }


    public enum AnchorPoint {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    }

    public enum InWorldBarVisibilityMode {
        NONE, WHEN_HOLDING_WEAPON, ALWAYS
    }

    public enum HealthChangeType {
        NONE, LAST, CUMULATIVE
    }


    @Override
    public void validatePostLoad() {
        // Recalculate dependent field
        particleOptions.particleDistanceSquared = particleOptions.particleDistance * particleOptions.particleDistance;
    }
}